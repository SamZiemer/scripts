import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetTag;
import com.liferay.portlet.asset.model.AssetVocabulary;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetTagLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetVocabularyLocalServiceUtil;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.model.MBThread;
import com.liferay.portlet.messageboards.model.MBThreadFlag;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;
import com.liferay.portlet.messageboards.service.MBThreadFlagLocalServiceUtil;
import com.liferay.portlet.messageboards.service.MBThreadLocalServiceUtil;
import com.liferay.portlet.social.model.SocialActivity;
import com.liferay.portlet.social.service.SocialActivityLocalServiceUtil;

//long companyId = FIND USING QUERIES and uncomment;
//long groupId = FIND USING QUERIES and uncomment;

/////////////////////BEGIN ASSET CATEGORIES////////////////////////////
List<AssetCategory> assetCategories;

List<AssetCategory> categoriesToRemove = new ArrayList<AssetCategory>();

assetCategories = AssetCategoryLocalServiceUtil.getAssetCategories(-1, -1);

for (AssetCategory category: assetCategories) {
	if (category.getGroupId() == groupId) {
		categoriesToRemove.add(category);
	}
}

for (AssetCategory category: categoriesToRemove) {
	AssetCategoryLocalServiceUtil.deleteAssetCategory(category);
}
/////////////////////END ASSET CATEGORIES////////////////////////////


/////////////////////BEGIN ASSET ENTRIES////////////////////////////
List<AssetEntry> assetEntries;

List<AssetEntry> entriesToRemove = new ArrayList<AssetEntry>();

assetEntries = AssetEntryLocalServiceUtil.getCompanyEntries(companyId, -1, -1);

for (AssetEntry entry: assetEntries) {
	if (entry.getGroupId() == groupId) {
		entriesToRemove.add(entry);
	}
}

for (AssetEntry entry: entriesToRemove) {
	AssetEntryLocalServiceUtil.deleteAssetEntry(entry);
}
/////////////////////END ASSET ENTRIES////////////////////////////


/////////////////////BEGIN ASSET TAGS////////////////////////////
List<AssetTag> assetTagsToRemove;

assetTagsToRemove = AssetTagLocalServiceUtil.getGroupTags(groupId);

for (AssetTag tag: assetTagsToRemove) {
	AssetTagLocalServiceUtil.deleteAssetTag(tag);
}
/////////////////////END ASSET TAGS////////////////////////////


/////////////////////BEGIN ASSET VOCABULARIES////////////////////////////
List<AssetVocabulary> assetVocabulariesToRemove;

assetVocabulariesToRemove = AssetVocabularyLocalServiceUtil.getGroupVocabularies(groupId);

for (AssetVocabulary vocab: assetVocabulariesToRemove) {
	AssetVocabularyLocalServiceUtil.deleteAssetVocabulary(vocab);
}
/////////////////////END ASSET VOCABULARIES////////////////////////////


/////////////////////BEGIN MBMESSAGE////////////////////////////
List<MBMessage> mbMessagesToRemove;

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


/////////////////////BEGIN SOCIAL ACTIVITY////////////////////////////

List<SocialActivity> activitiesToRemove;

activitiesToRemove = SocialActivityLocalServiceUtil.getGroupActivities(groupId, -1, -1);

for (SocialActivity activity: activitiesToRemove) {
	SocialActivityLocalServiceUtil.deleteActivity(activity);
}
/////////////////////END SOCIAL ACTIVITY////////////////////////////