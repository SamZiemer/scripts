import com.liferay.portal.kernel.dao.orm.QueryUtil
import com.liferay.portal.kernel.util.GetterUtil
import com.liferay.portal.kernel.util.StringBundler
import com.liferay.portal.security.auth.CompanyThreadLocal
import com.liferay.portal.service.GroupLocalServiceUtil
import com.liferay.portal.util.PropsValues
import com.liferay.portlet.documentlibrary.model.DLFolderConstants
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil
import com.liferay.portlet.documentlibrary.service.DLFileVersionLocalServiceUtil
import com.liferay.portlet.documentlibrary.service.DLFolderLocalServiceUtil
import com.liferay.portlet.documentlibrary.store.DLStoreUtil

// Populate caches to speed up scripts

folders = DLFolderLocalServiceUtil.getDLFolders(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
fileEntries = DLFileEntryLocalServiceUtil.getDLFileEntries(QueryUtil.ALL_POS, QueryUtil.ALL_POS);

StringBundler sb = new StringBundler();

sb.append("<h1>Missing folders</h1>");
sb.append("<table border=1><tr><th>FolderId</th><th>FileEntryId</th><th></th></tr>");

// Check folders that are now missing a folder, as the document will no longer
// be accessible to users normally. This hints at a folder that was deleted but
// where the folders failed to be deleted.


long missingFolderCount = 0;

for (fileEntry in fileEntries) {
	if (fileEntry.folderId == DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
		continue
	}

	folder = DLFolderLocalServiceUtil.fetchDLFolder(fileEntry.folderId);

	if (folder == null) {
		sb.append("<tr>");
		sb.append("<td>" + fileEntry.folderId + "</td>");
		sb.append("<td>" + fileEntry.fileEntryId + "</td>");
		sb.append("</tr>");
		missingFolderCount++;
		continue
	}

}
sb.append("<tr><td><b>Number of missing Folders</b></td><td>" + missingFolderCount + "</td></tr>");
sb.append("</table>");

sb.append("<h1></h1>");

// Check versions that do not have a downloadable file -- essentially, documents
// that are supposed to exist, but won't be available for download.

fileVersions = DLFileVersionLocalServiceUtil.getDLFileVersions(QueryUtil.ALL_POS, QueryUtil.ALL_POS)

sb.append("<h1>Entries missing corresponding file system Entry</h1>");

sb.append("<table border=1><tr><th>GroupId</th><th>FolderId</th><th>FileEntryId</th><th>FileVersion</th></tr>");

for (fileVersion in fileVersions) {
	if (DLStoreUtil.hasFile(fileVersion.companyId, fileVersion.fileEntry.dataRepositoryId, fileVersion.fileEntry.name, fileVersion.version)) {
		continue;
	}

	groupId = fileVersion.groupId;
	folderId = fileVersion.folderId;
	fileEntryId = fileVersion.fileEntryId;
	version = fileVersion.version;

	sb.append("<tr><td>" + groupId + "</td><td>" + folderId + "</td><td>" + fileEntryId + "</td><td>" + version + "</td></tr>");
}

sb.append("</table>");

// Check files on the file system that no longer have a corresponding database
// entry -- essentially, documents that were deleted in the database but failed
// to be deleted on the file system.

rootDir = new File(PropsValues.DL_STORE_FILE_SYSTEM_ROOT_DIR);

companyId = CompanyThreadLocal.companyId;
companyDir = new File(rootDir, String.valueOf(companyId));

sb.append("<h1>File System Entries with no corresponding database entry</h1>");

sb.append("<table border=1><tr><th>CompanyId</th><th>FolderId</th><th>FileEntryId</th></tr>");

for (dlFolderDir in companyDir.listFiles()) {
	folderId = GetterUtil.getLong(dlFolderDir.name);
	folder = DLFolderLocalServiceUtil.fetchDLFolder(folderId);

	if (folder != null) {
		// It might correspond to the root folder for a site, so check to see if
		// there is a site with this id as well before we report that it is
		// a folder that is orphaned

		groupId = folder.groupId;
		group = GroupLocalServiceUtil.fetchGroup(groupId);

		if (group == null) {
			sb.append("<tr><td>" + companyId + "</td><td>" + folderId + "</td></tr>");

			continue;
		}
	}

	for (dlFileEntryDir in dlFolderDir.listFiles()) {
		fileEntryId = GetterUtil.getLong(dlFileEntryDir.name);
		fileEntry = DLFileEntryLocalServiceUtil.fetchDLFileEntry(fileEntryId);

		if (fileEntry == null) {
			sb.append("<tr><td>" + companyId + "</td><td>" + folderId + "</td><td>" + fileEntryId + "</td></tr>")
		}
	}

}

sb.append("<tr /></table>");

out.println(sb.toString());

