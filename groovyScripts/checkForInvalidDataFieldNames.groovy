import com.liferay.portal.kernel.dao.jdbc.DataAccess
import com.liferay.portal.kernel.json.JSONArray
import com.liferay.portal.kernel.json.JSONObject
import com.liferay.portal.kernel.json.JSONFactoryUtil
import com.liferay.portal.kernel.util.PropsKeys
import com.liferay.portal.kernel.util.PropsUtil

import java.sql.PreparedStatement
import java.sql.Connection
import java.sql.ResultSet

Connection con = DataAccess.getConnection()

PreparedStatement ps = con.prepareStatement("Select contentId, data_ from DDMContent")

ResultSet rs = ps.executeQuery()

String filePath =
		PropsUtil.get(PropsKeys.LIFERAY_HOME) + "/invalidFieldNames.info"

PrintWriter logFile = new PrintWriter(filePath)

logFile.println("ContentId | " + "Field Name")

while (rs.next()) {
	long contentId = rs.getLong("contentId")
	String data = rs.getString("data_")

	JSONObject jsonData = JSONFactoryUtil.createJSONObject(data)
	JSONArray fieldValues = jsonData.getJSONArray("fieldValues")

	for (int i = 0; i < fieldValues.size(); i++) {
		JSONObject fieldValue = fieldValues.getJSONObject(i)

		if (fieldValue.getString("name").contains("-")) {
			logFile.println(contentId + " " + fieldValue.toJSONString())
		}
	}
}

logFile.close()

out.println("Check the results in " + filePath)