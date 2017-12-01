import com.liferay.counter.service.CounterLocalServiceUtil
import com.liferay.portal.kernel.dao.db.DB
import com.liferay.portal.kernel.dao.db.DBFactoryUtil
import com.liferay.portal.kernel.dao.jdbc.DataAccess
import com.liferay.portal.kernel.log.Log
import com.liferay.portal.kernel.log.LogFactoryUtil
import com.liferay.portal.kernel.util.StringBundler

import java.sql.Connection

public class UpdateJournalId {

	public void doUpdate() {

		Connection con = null

		try {
			con = DataAccess.getConnection()

			long newJournalArticleId = CounterLocalServiceUtil.increment(
				"com.liferay.counter.model.Counter")

			long oldJournalArticleId = 6627080

			StringBundler sb = new StringBundler(4)

			sb.append("update JournalArticle set id_ = ")
			sb.append(newJournalArticleId)
			sb.append(" where id_ = ")
			sb.append(oldJournalArticleId)

			DB db = DBFactoryUtil.getDB()

			db.runSQL(con, sb.toString())

			_log.info(sb.toString())
			_log.info("New JournalArticle.id_ is: " + newJournalArticleId)

		}
		finally {
			DataAccess.cleanUp(con)
		}

	}

	private static Log _log = LogFactoryUtil.getLog(UpdateJournalId.class)
}

new UpdateJournalId().doUpdate()