import com.liferay.portal.kernel.dao.jdbc.DataAccess
import com.liferay.portal.kernel.util.StringUtil

import java.sql.PreparedStatement
import java.sql.ResultSet

connection = DataAccess.getConnection()
PreparedStatement ps1 = connection.prepareStatement(
	"select id_, content from JournalArticle where content like '<?xml version=\"10\"%'")

ResultSet rs = ps1.executeQuery()

while (rs.next()) {
	long id = rs.getLong("id_")

	PreparedStatement ps2 = connection.prepareStatement(
		"update JournalArticle set content = ? where id_ = ?")

	String updatedContent = StringUtil.replace(content, "<?xml version=\"10\"", "<?xml version=\"1.0\"")

	ps2.setString(1, updatedContent)
	ps2.setLong(2, id)

	ps2.execute()
}
