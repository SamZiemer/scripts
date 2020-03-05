import com.liferay.dynamic.data.mapping.io.internal.DDMFormValuesJSONDeserializerImpl
import com.liferay.dynamic.data.mapping.io.internal.DDMFormValuesJSONSerializerImpl
import com.liferay.dynamic.data.mapping.model.DDMForm
import com.liferay.dynamic.data.mapping.model.DDMFormField
import com.liferay.dynamic.data.mapping.model.DDMStructure
import com.liferay.dynamic.data.mapping.model.Value
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue
import com.liferay.dynamic.data.mapping.storage.DDMFormValues
import com.liferay.dynamic.data.mapping.util.DDMFormFieldValueTransformer
import com.liferay.dynamic.data.mapping.util.DDMFormValuesTransformer
import com.liferay.dynamic.data.mapping.util.impl.DDMImpl
import com.liferay.portal.json.JSONFactoryImpl
import com.liferay.portal.kernel.dao.jdbc.DataAccess
import com.liferay.portal.kernel.exception.PortalException
import com.liferay.portal.kernel.json.JSONFactory
import com.liferay.portal.kernel.util.DateFormatFactoryUtil
import com.liferay.portal.kernel.util.GetterUtil
import com.liferay.portal.kernel.util.PropsKeys
import com.liferay.portal.kernel.util.PropsUtil
import com.liferay.portal.kernel.util.Validator

import java.sql.PreparedStatement
import java.sql.Connection
import java.sql.ResultSet
import java.text.DateFormat

String filePath =
		PropsUtil.get(PropsKeys.LIFERAY_HOME) + "/updatedDateFields.info"

PrintWriter logFile = new PrintWriter(filePath)

Boolean dryRun = true

Connection con = DataAccess.getConnection()

PreparedStatement ps1 = con.prepareStatement("Select contentId, data_ from DDMContent")
PreparedStatement ps2 = con.prepareStatement("select structureId from DDMStorageLink where classPK = ?")
PreparedStatement ps3 = con.prepareStatement("update DDMContent set data_ = ? where contentId = ?")

ResultSet rs1 = ps1.executeQuery()

while (rs1.next()) {
	long contentId = rs1.getLong("contentId")
	String data = rs1.getString("data_")

	long structureId = 0

	ps2.setLong(1, contentId)

	ResultSet rs2 = ps2.executeQuery()

	while (rs2.next()) {
		structureId = rs2.getLong("structureId")
		break
	}

	DDMFormValuesJSONDeserializerImpl ddmFormValuesJSONDeserializer = new DDMFormValuesJSONDeserializerImpl()
	DDMFormValuesJSONSerializerImpl ddmFormValuesJSONSerializer = new DDMFormValuesJSONSerializerImpl()

	JSONFactory jsonFactory = new JSONFactoryImpl()

	ddmFormValuesJSONDeserializer.setJSONFactory(jsonFactory)
	ddmFormValuesJSONSerializer.setJSONFactory(jsonFactory)

	DDMStructure ddmStructure = DDMStructureLocalServiceUtil.fetchDDMStructure(structureId)

	DDMForm ddmForm = ddmStructure.getDDMForm()

	List<DDMFormField> ddmFormFields = ddmForm.getDDMFormFields()

	containsDate = false

	for (DDMFormField ddmFormField: ddmFormFields) {
		if (ddmFormField.getType().equals("ddm-date")) {
			containsDate = true
			break
		}
	}

	if (containsDate) {
		DDMFormValues ddmFormValues = ddmFormValuesJSONDeserializer.deserialize(
				ddmForm, data)

		String oldValues = ddmFormValuesJSONSerializer.serialize(ddmFormValues)

		DDMFormValuesTransformer ddmFormValuesTransformer =
				new DDMFormValuesTransformer(ddmFormValues)

		ddmFormValuesTransformer.addTransformer(
				new DateDDMFormFieldValueTransformer())

		ddmFormValuesTransformer.transform()

		newValues = ddmFormValuesJSONSerializer.serialize(ddmFormValues)

		if (!oldValues.equals(newValues)) {

			logFile.println("\n")
			logFile.println("DDMContent Before transform")
			logFile.println(oldValues)

			logFile.println("DDMContent after transform")
			logFile.println(newValues)

			if (!dryRun) {
				ps3.setString(1, newValues)
				ps3.setLong(2, contentId)
				ps3.execute()
			}
		}
	}
}

if (dryRun) {
	out.println(
			"The changes have not been persisted to the database. Please review the proposed changes in " +
			filePath)
	out.println(
			"If the changes are acceptable, please re-run this script after changing dryRun to false on line 33.")
}
else {
	out.println("Check the results in " + filePath)
}

class DateDDMFormFieldValueTransformer
		implements DDMFormFieldValueTransformer {

	@Override
	public String getFieldType() {
		return DDMImpl.TYPE_DDM_DATE
	}

	@Override
	public void transform(DDMFormFieldValue ddmFormFieldValue)
			throws PortalException {

		Value value = ddmFormFieldValue.getValue();

		if (value != null) {
			for (Locale locale : value.getAvailableLocales()) {
				String valueString = value.getString(locale);

				if (Validator.isNull(valueString) ||
					!Validator.isNumber(valueString)) {

					continue;
				}

				Date dateValue = new Date(GetterUtil.getLong(valueString));

				value.addString(locale, _dateFormat.format(dateValue));
			}
		}
	}

	private final DateFormat _dateFormat =
			DateFormatFactoryUtil.getSimpleDateFormat("yyyy-MM-dd");

}