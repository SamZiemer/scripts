import com.liferay.portal.kernel.dao.jdbc.DataAccess
import com.liferay.portal.kernel.workflow.WorkflowConstants
import com.liferay.portlet.journal.model.JournalArticle
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

Connection con = null;
PreparedStatement ps = null;
ResultSet rs = null;

try {
	con = DataAccess.getConnection();

	String query = "select JournalArticle.* from JournalArticle left join JournalArticle tempJournalArticle on\n" +
			"(JournalArticle.groupId = tempJournalArticle.groupId)\n" +
			"and (JournalArticle.articleId = tempJournalArticle.articleId) and\n" +
			"(JournalArticle.version < tempJournalArticle.version) and \n" +
			"(JournalArticle.status = tempJournalArticle.status) where\n" +
			"(JournalArticle.classNameId = 0) and (tempJournalArticle.version is null) and\n" +
			"(JournalArticle.expirationDate is not null) and\n" +
			"(JournalArticle.status = 3)";

	ps = con.prepareStatement(query);

	rs = ps.executeQuery();

	while (rs.next()) {
		String articleId = rs.getString("articleId");
		long groupId = rs.getLong("groupId");
		Date expirationDate = rs.getTimestamp("expirationDate");

		List<JournalArticle> articles = JournalArticleLocalServiceUtil.getArticles(groupId, articleId);

		for (JournalArticle curArticle : articles) {
			curArticle.setExpirationDate(expirationDate);
			curArticle.setStatus(WorkflowConstants.STATUS_EXPIRED);

			JournalArticleLocalServiceUtil.updateJournalArticle(curArticle);
		}
	}

}
catch (Exception e) {
	out.println(e.toString());
}
finally {
	con.close();
	ps.close();
	rs.close();
}