import com.liferay.portal.kernel.dao.jdbc.DataAccess
import com.liferay.portal.kernel.service.ClassNameLocalServiceUtil
import com.liferay.portal.kernel.util.StringUtil

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

Long CalBookingclassNameId = ClassNameLocalServiceUtil.getClassNameId("com.liferay.calendar.model.CalendarBooking")

Connection connection = DataAccess.getConnection()

PreparedStatement ps = connection.prepareStatement(
	"select vocabularyId, settings_ from AssetVocabulary where settings_ like '%10072%'")

PreparedStatement ps2 = connection.prepareStatement(
	"update AssetVocabulary set settings_ = ? where vocabularyId = ?")

ResultSet rs = ps.executeQuery()

while (rs.next()) {
	String oldSettings = rs.getString("settings_")
	long vocabularyId = rs.getLong("vocabularyId")

	String newSettings = StringUtil.replace(
		oldSettings, "10072", CalBookingclassNameId.toString())

	ps2.setString(1, newSettings)
	ps2.setLong(2, vocabularyId)

	ps2.execute()
}