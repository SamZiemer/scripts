import com.liferay.portal.kernel.dao.jdbc.DataAccess
import com.liferay.portal.kernel.util.StringUtil

import java.sql.PreparedStatement
import java.sql.ResultSet

connection = DataAccess.getUpgradeOptimizedConnection()

PreparedStatement ps1 = connection.prepareStatement(
	"select articleId, version, elName from JournalArticleImage where " +
		"(elInstanceId = '' or elInstanceId is null) group by " +
			"articleId, version, elName")

ResultSet rs = ps1.executeQuery()

PreparedStatement ps2 =
		connection.prepareStatement(
			"update JournalArticleImage set elInstanceId = ? " +
				"where articleId = ? and version = ? and elName = ?")

while (rs.next()) {
	String articleId = rs.getString(1)
	String version = rs.getString(2)
	String elName = rs.getString(3)

	ps2.setString(1, StringUtil.randomString(4))
	ps2.setString(2, articleId)
	ps2.setString(3, version)
	ps2.setString(4, elName)

	ps2.execute()
}

