import com.liferay.portal.kernel.dao.jdbc.DataAccess
import com.liferay.portal.kernel.log.Log
import com.liferay.portal.kernel.log.LogFactoryUtil
import com.liferay.portal.kernel.util.PropsKeys
import com.liferay.portal.kernel.util.PropsUtil

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData

public class find_excessive_length_rows {

	public void runQueries(out) {

		String filePath =
			PropsUtil.get(PropsKeys.LIFERAY_HOME) + "/excessive_length_rows.csv"

		PrintWriter logFile = new PrintWriter(filePath)

		Connection connection = DataAccess.getConnection()

		try {

			for (String string : _queries) {
				PreparedStatement ps = connection.prepareStatement(string);

				ResultSet rs = ps.executeQuery()
				ResultSetMetaData rsmd = rs.getMetaData()

				String tableName = string.replace("SELECT * FROM ", "")
				if (rs.isBeforeFirst()) {
					logFile.println(tableName)
					for (int i = 1; i <= 4; i++) {
						String columnName = rsmd.getColumnName(i);
						logFile.print(columnName + ",")
					}
				}

				while (rs.next()) {
					logFile.println("")
					for (int i = 1; i <= 4; i++) {
						String columnName = rsmd.getColumnName(i);
						String columnValue = rs.getString(columnName)
						logFile.print(columnValue + ",")

					}
					logFile.println("")
				}

				DataAccess.cleanUp(ps)
				DataAccess.cleanUp(rs)
			}
		}
		finally {
			DataAccess.cleanUp(connection)

			logFile.close()

			out.println("Check the results in " + filePath)
		}
	}

	private String[] _queries = [
		"SELECT * FROM AnnouncementsEntry WHERE LENGTH(content) > 2000",
		"SELECT * FROM AnnouncementsEntry WHERE LENGTH(url) > 2000",
		"SELECT * FROM AssetCategory WHERE LENGTH(title) > 2000",
		"SELECT * FROM AssetCategory WHERE LENGTH(description) > 2000",
		"SELECT * FROM AssetEntry WHERE LENGTH(title) > 2000",
		"SELECT * FROM AssetEntry WHERE LENGTH(description) > 2000",
		"SELECT * FROM AssetEntry WHERE LENGTH(summary) > 2000",
		"SELECT * FROM AssetEntry WHERE LENGTH(url) > 2000",
		"SELECT * FROM AssetVocabulary WHERE LENGTH(title) > 2000",
		"SELECT * FROM AssetVocabulary WHERE LENGTH(description) > 2000",
		"SELECT * FROM AssetVocabulary WHERE LENGTH(settings_) > 2000",
		"SELECT * FROM BlogsEntry WHERE LENGTH(smallImageURL) > 2000",
		"SELECT * FROM BookmarksEntry WHERE LENGTH(url) > 2000",
		"SELECT * FROM BookmarksEntry WHERE LENGTH(description) > 2000",
		"SELECT * FROM BookmarksFolder WHERE LENGTH(description) > 2000",
		"SELECT * FROM CalEvent WHERE LENGTH(description) > 2000",
		"SELECT * FROM CalEvent WHERE LENGTH(location) > 2000",
		"SELECT * FROM Company WHERE LENGTH(homeURL) > 2000",
		"SELECT * FROM DDLRecordSet WHERE LENGTH(name) > 2000",
		"SELECT * FROM DDLRecordSet WHERE LENGTH(description) > 2000",
		"SELECT * FROM DDMContent WHERE LENGTH(name) > 2000",
		"SELECT * FROM DDMContent WHERE LENGTH(description) > 2000",
		"SELECT * FROM DDMStructure WHERE LENGTH(name) > 2000",
		"SELECT * FROM DDMStructure WHERE LENGTH(description) > 2000",
		"SELECT * FROM DDMTemplate WHERE LENGTH(name) > 2000",
		"SELECT * FROM DDMTemplate WHERE LENGTH(description) > 2000",
		"SELECT * FROM DLFileEntry WHERE LENGTH(description) > 2000",
		"SELECT * FROM DLFileEntryType WHERE LENGTH(description) > 2000",
		"SELECT * FROM DLFileVersion WHERE LENGTH(description) > 2000",
		"SELECT * FROM DLFolder WHERE LENGTH(description) > 2000",
		"SELECT * FROM DLSync WHERE LENGTH(description) > 2000",
		"SELECT * FROM ExpandoColumn WHERE LENGTH(defaultData) > 2000",
		"SELECT * FROM ExpandoValue WHERE LENGTH(data_) > 2000",
		"SELECT * FROM Group_ WHERE LENGTH(description) > 2000",
		"SELECT * FROM Group_ WHERE LENGTH(typeSettings) > 2000",
		"SELECT * FROM JournalArticle WHERE LENGTH(title) > 2000",
		"SELECT * FROM JournalArticle WHERE LENGTH(description) > 2000",
		"SELECT * FROM JournalArticle WHERE LENGTH(smallImageURL) > 2000",
		"SELECT * FROM JournalFeed WHERE LENGTH(description) > 2000",
		"SELECT * FROM JournalStructure WHERE LENGTH(name) > 2000",
		"SELECT * FROM JournalStructure WHERE LENGTH(description) > 2000",
		"SELECT * FROM JournalTemplate WHERE LENGTH(name) > 2000",
		"SELECT * FROM JournalTemplate WHERE LENGTH(description) > 2000",
		"SELECT * FROM JournalTemplate WHERE LENGTH(smallImageURL) > 2000",
		"SELECT * FROM Layout WHERE LENGTH(name) > 2000",
		"SELECT * FROM Layout WHERE LENGTH(title) > 2000",
		"SELECT * FROM Layout WHERE LENGTH(description) > 2000",
		"SELECT * FROM Layout WHERE LENGTH(keywords) > 2000",
		"SELECT * FROM Layout WHERE LENGTH(robots) > 2000",
		"SELECT * FROM Layout WHERE LENGTH(css) > 2000",
		"SELECT * FROM LayoutBranch WHERE LENGTH(description) > 2000",
		"SELECT * FROM LayoutPrototype WHERE LENGTH(name) > 2000",
		"SELECT * FROM LayoutPrototype WHERE LENGTH(description) > 2000",
		"SELECT * FROM LayoutPrototype WHERE LENGTH(settings_) > 2000",
		"SELECT * FROM LayoutRevision WHERE LENGTH(name) > 2000",
		"SELECT * FROM LayoutRevision WHERE LENGTH(title) > 2000",
		"SELECT * FROM LayoutRevision WHERE LENGTH(description) > 2000",
		"SELECT * FROM LayoutRevision WHERE LENGTH(keywords) > 2000",
		"SELECT * FROM LayoutRevision WHERE LENGTH(robots) > 2000",
		"SELECT * FROM LayoutRevision WHERE LENGTH(css) > 2000",
		"SELECT * FROM LayoutSet WHERE LENGTH(css) > 2000",
		"SELECT * FROM LayoutSet WHERE LENGTH(settings_) > 2000",
		"SELECT * FROM LayoutSetBranch WHERE LENGTH(description) > 2000",
		"SELECT * FROM LayoutSetBranch WHERE LENGTH(css) > 2000",
		"SELECT * FROM LayoutSetBranch WHERE LENGTH(settings_) > 2000",
		"SELECT * FROM LayoutSetPrototype WHERE LENGTH(name) > 2000",
		"SELECT * FROM LayoutSetPrototype WHERE LENGTH(description) > 2000",
		"SELECT * FROM LayoutSetPrototype WHERE LENGTH(settings_) > 2000",
		"SELECT * FROM MBCategory WHERE LENGTH(description) > 2000",
		"SELECT * FROM MDRAction WHERE LENGTH(name) > 2000",
		"SELECT * FROM MDRAction WHERE LENGTH(description) > 2000",
		"SELECT * FROM MDRRule WHERE LENGTH(name) > 2000",
		"SELECT * FROM MDRRule WHERE LENGTH(description) > 2000",
		"SELECT * FROM MDRRuleGroup WHERE LENGTH(name) > 2000",
		"SELECT * FROM MDRRuleGroup WHERE LENGTH(description) > 2000",
		"SELECT * FROM MembershipRequest WHERE LENGTH(comments) > 2000",
		"SELECT * FROM MembershipRequest WHERE LENGTH(replyComments) > 2000",
		"SELECT * FROM Organization_ WHERE LENGTH(treePath) > 2000",
		"SELECT * FROM Organization_ WHERE LENGTH(comments) > 2000",
		"SELECT * FROM PasswordPolicy WHERE LENGTH(description) > 2000",
		"SELECT * FROM PluginSetting WHERE LENGTH(roles) > 2000",
		"SELECT * FROM PollsChoice WHERE LENGTH(description) > 2000",
		"SELECT * FROM PollsQuestion WHERE LENGTH(title) > 2000",
		"SELECT * FROM PollsQuestion WHERE LENGTH(description) > 2000",
		"SELECT * FROM Portlet WHERE LENGTH(roles) > 2000",
		"SELECT * FROM Repository WHERE LENGTH(description) > 2000",
		"SELECT * FROM Role_ WHERE LENGTH(title) > 2000",
		"SELECT * FROM Role_ WHERE LENGTH(description) > 2000",
		"SELECT * FROM SCFrameworkVersion WHERE LENGTH(url) > 2000",
		"SELECT * FROM SCLicense WHERE LENGTH(url) > 2000",
		"SELECT * FROM SCProductEntry WHERE LENGTH(shortDescription) > 2000",
		"SELECT * FROM SCProductEntry WHERE LENGTH(longDescription) > 2000",
		"SELECT * FROM SCProductEntry WHERE LENGTH(pageURL) > 2000",
		"SELECT * FROM SCProductVersion WHERE LENGTH(changeLog) > 2000",
		"SELECT * FROM SCProductVersion WHERE LENGTH(downloadPageURL) > 2000",
		"SELECT * FROM SCProductVersion WHERE LENGTH(directDownloadURL) > 2000",
		"SELECT * FROM ShoppingCart WHERE LENGTH(itemIds) > 2000",
		"SELECT * FROM ShoppingCategory WHERE LENGTH(description) > 2000",
		"SELECT * FROM ShoppingCoupon WHERE LENGTH(description) > 2000",
		"SELECT * FROM ShoppingCoupon WHERE LENGTH(limitCategories) > 2000",
		"SELECT * FROM ShoppingCoupon WHERE LENGTH(limitSkus) > 2000",
		"SELECT * FROM ShoppingItem WHERE LENGTH(description) > 2000",
		"SELECT * FROM ShoppingItem WHERE LENGTH(properties) > 2000",
		"SELECT * FROM ShoppingItem WHERE LENGTH(fieldsQuantities) > 2000",
		"SELECT * FROM ShoppingItem WHERE LENGTH(smallImageURL) > 2000",
		"SELECT * FROM ShoppingItem WHERE LENGTH(mediumImageURL) > 2000",
		"SELECT * FROM ShoppingItem WHERE LENGTH(largeImageURL) > 2000",
		"SELECT * FROM ShoppingItemField WHERE LENGTH(values_) > 2000",
		"SELECT * FROM ShoppingItemField WHERE LENGTH(description) > 2000",
		"SELECT * FROM ShoppingOrder WHERE LENGTH(comments) > 2000",
		"SELECT * FROM ShoppingOrderItem WHERE LENGTH(description) > 2000",
		"SELECT * FROM ShoppingOrderItem WHERE LENGTH(properties) > 2000",
		"SELECT * FROM SocialActivity WHERE LENGTH(extraData) > 2000",
		"SELECT * FROM SocialRequest WHERE LENGTH(extraData) > 2000",
		"SELECT * FROM Team WHERE LENGTH(description) > 2000",
		"SELECT * FROM User_ WHERE LENGTH(comments) > 2000",
		"SELECT * FROM UserGroup WHERE LENGTH(description) > 2000",
		"SELECT * FROM UserTrackerPath WHERE LENGTH(path_) > 2000",
		"SELECT * FROM Website WHERE LENGTH(url) > 2000",
		"SELECT * FROM WikiNode WHERE LENGTH(description) > 2000",
		"SELECT * FROM WikiPage WHERE LENGTH(summary) > 2000"
	]

	private static Log _log = LogFactoryUtil.getLog(
		find_excessive_length_rows.class);
}
new find_excessive_length_rows().runQueries(out);