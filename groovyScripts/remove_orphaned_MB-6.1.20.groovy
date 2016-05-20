import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portlet.messageboards.model.MBMessage
import com.liferay.portlet.messageboards.model.MBStatsUser;
import com.liferay.portlet.messageboards.model.MBThread;
import com.liferay.portlet.messageboards.model.MBThreadFlag;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil
import com.liferay.portlet.messageboards.service.MBStatsUserLocalServiceUtil;
import com.liferay.portlet.messageboards.service.MBThreadFlagLocalServiceUtil;
import com.liferay.portlet.messageboards.service.MBThreadLocalServiceUtil;

import java.util.ArrayList;
import java.util.List;

long groupId = //FIND USING QUERIES;

/////////////////////BEGIN MBMESSAGE////////////////////////////
List<MBMessage> mbMessagesToRemove = null;

mbMessagesToRemove = MBMessageLocalServiceUtil.getGroupMessages(groupId, -1, -1, -1);

for (MBMessage message: mbMessagesToRemove) {
	MBMessageLocalServiceUtil.deleteMBMessage(message);
}
/////////////////////END MBMESSAGE////////////////////////////


/////////////////////BEGIN MBTHREAD////////////////////////////
List<MBThread> mbThreadsToRemove;

mbThreadsToRemove = MBThreadLocalServiceUtil.getGroupThreads(groupId, -1, -1, -1);

for (MBThread thread: mbThreadsToRemove) {
	MBThreadLocalServiceUtil.deleteMBThread(thread);
}
/////////////////////END MBTHREAD////////////////////////////


/////////////////////BEGIN MBTHREAD FLAG////////////////////////////
List<MBThreadFlag> mbThreadFlags;

List<Long> threadIds = new ArrayList<Long>();

List<MBThreadFlag> flagsToRemove = new ArrayList();

mbThreadFlags = MBThreadFlagLocalServiceUtil.getMBThreadFlags(-1, -1);

for (MBThread thread: mbThreadsToRemove) {
	threadIds.add(thread.getThreadId());
}

for (int i = 0; i < threadIds.size(); i++) {
	long threadId = threadIds.get(i);

	for (MBThreadFlag flag: mbThreadFlags) {
		if (flag.getThreadId() == threadId) {
			flagsToRemove.add(flag);
		}
	}
}

for (MBThreadFlag flag: flagsToRemove) {
	MBThreadFlagLocalServiceUtil.deleteMBThreadFlag(flag);
}
/////////////////////END MBTHREAD FLAG////////////////////////////


////////////////////BEGIN MBSTATSUSER FLAG///////////////////////////
List<MBStatsUser> mbStatsUsers;

List<Long> statsUserId = new ArrayList<Long>();

List<MBStatsUser> statsToRemove = new ArrayList<Long>();

mbStatsUsers = MBStatsUserLocalServiceUtil.getMBStatsUsers(-1, -1);

for (MBStatsUser stat: mbStatsUsers) {
	statsUserId.add(stat.getStatsUserId());
}

for (int i = 0; i < statsUserId.size(); i++) {
	long statId = statsUserId.get(i);

	for (MBStatsUser stat: mbStatsUsers) {
		if (stat.getGroupId() == groupId) {
			statsToRemove.add(stat);
		}
	}
}

for (MBStatsUser stat: statsToRemove) {
	MBStatsUserLocalServiceUtil.deleteMBStatsUser(stat);
}
/////////////////////END MBSTATSUSER FLAG////////////////////////////