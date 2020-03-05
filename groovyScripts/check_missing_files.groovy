import com.liferay.portal.kernel.dao.orm.QueryUtil
import com.liferay.portal.kernel.util.DateUtil
import com.liferay.portal.kernel.util.HtmlUtil
import com.liferay.portal.kernel.util.GetterUtil
import com.liferay.portal.kernel.util.StringBundler
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal
import com.liferay.portal.kernel.service.GroupLocalServiceUtil
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLFileEntryLocalServiceUtil;
import com.liferay.document.library.kernel.service.DLFolderLocalServiceUtil
import com.liferay.document.library.kernel.service.DLFileVersionLocalServiceUtil
import com.liferay.document.library.kernel.store.DLStoreUtil

//ADD THE ROOT DIR FOR YOUR DOCUMENT LIBRARY HERE:
rootDir = new File("data/document_library")

StringBundler sb = new StringBundler();
// Populate caches to speed up scripts

folders = DLFolderLocalServiceUtil.getDLFolders(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
fileEntries = DLFileEntryLocalServiceUtil.getDLFileEntries(QueryUtil.ALL_POS, QueryUtil.ALL_POS);

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

if (missingFolderCount == 0) {
	sb.append("<tr><td colspan=\"2\"><b>No Missing Folders</b></td></tr>");
}
else {
	sb.append("<tr><td><b>Number of missing Folders</b></td><td>" + missingFolderCount + "</td></tr>");
}

	sb.append("</table>");
	sb.append("<h1></h1>");

//Check if there are orphaned DLFileVersion in the database

fileVersions = DLFileVersionLocalServiceUtil.getDLFileVersions(QueryUtil.ALL_POS, QueryUtil.ALL_POS)

sb.append("<br /><h1>Versions missing corresponding DLFileEntry </h1>");

sb.append("<table border=1><tr><th>GroupId</th><th>FolderId</th><th>FileEntryId</th><th>FileVersion</th></tr>");

for (fileVersion in fileVersions) {
	fileEntry = DLFileEntryLocalServiceUtil.fetchDLFileEntry(fileVersion.fileEntryId)

	if (fileEntry == null) {
		groupId = fileVersion.groupId;
		folderId = fileVersion.folderId;
		fileEntryId = fileVersion.fileEntryId;
		version = fileVersion.version;

		sb.append("<tr><td>" + groupId + "</td><td>" + folderId + "</td><td>" + fileEntryId + "</td><td>" + version + "</td></tr>");
	}
}

sb.append("</table>")

// Check versions that do not have a downloadable file -- essentially, documents
// that are supposed to exist, but won't be available for download.

sb.append("<br /><h1>Entries missing corresponding file system Entry</h1>");

sb.append("<table border=1><tr><th>GroupId</th><th>FolderId</th><th>FileEntryId</th><th>FileVersion</th><th>FilePath</th></tr>");

for (fileVersion in fileVersions) {
	try {
		if (DLStoreUtil.hasFile(fileVersion.companyId, fileVersion.fileEntry.dataRepositoryId, fileVersion.fileEntry.name, fileVersion.version)) {
			continue;
		}
	}
	catch (NoSuchFileEntryException) {}
		groupId = fileVersion.groupId;
		folderId = fileVersion.folderId;
		fileEntryId = fileVersion.fileEntryId;
		version = fileVersion.version;

		fileEntry = DLFileEntryLocalServiceUtil.getDLFileEntry(fileEntryId)
		name = fileEntry.getName()

		path = StringBundler.concat(
				'/', fileEntry.getCompanyId().toString(), '/', fileEntry.getFolderId().toString(),
				'/', name.substring(0, 2), '/', name.substring(2, 4), '/', name,
				'.afsh')

		sb.append("<tr><td>" + groupId + "</td><td>" + folderId + "</td><td>" + fileEntryId + "</td><td>" + version + "</td><td>" + path + "</td></tr>");
}

sb.append("</table>");

// Check files on the file system that no longer have a corresponding database
// entry -- essentially, documents that were deleted in the database but failed
// to be deleted on the file system.

companyId = CompanyThreadLocal.companyId;
companyDir = new File(rootDir, String.valueOf(companyId));

sb.append("<br /><h1>File System Entries with no corresponding database entry</h1>");

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

out.print("<a id=\"downloadReport\" download=\"orphaned_files_");
out.print(DateUtil.getCurrentDate("yyyyMMddhhmmss", Locale.US));
out.print(".html\">download</a>");
out.println();

out.println("<script>");
out.println("{");

out.print("var blob = new Blob(['");
out.print(HtmlUtil.escapeJS(sb.toString()));
out.print("'], {type: 'text/html'});");
out.println();

out.println("document.getElementById('downloadReport').href = window.URL.createObjectURL(blob);");

out.println("}");
out.println("</script>");