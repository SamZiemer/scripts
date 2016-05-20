import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.StringBundler
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFolderLocalServiceUtil;

// Populate cache to speed up script

fileEntries = DLFileEntryLocalServiceUtil.getDLFileEntries(QueryUtil.ALL_POS, QueryUtil.ALL_POS);

StringBundler sb = new StringBundler();

sb.append("<h1>Recovered Files</h1>");
sb.append("<table border=1><tr><th>FileEntryId</th><th>FileEntry Title</th><th>Old FolderId</th><th>New FolderId</th><th>GroupId</th></tr>");

// Check folders that are now missing a folder, as the document will no longer
// be accessible to users normally. This hints at a folder that was deleted but
// where the folders failed to be deleted.

Map newFoldersMap = new HashMap();

long movedFileCount = 0;
long userId = PermissionThreadLocal.getPermissionChecker().getUserId();

for (fileEntry in fileEntries) {
	if (fileEntry.folderId == DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
		continue
	}

	folder = DLFolderLocalServiceUtil.fetchDLFolder(fileEntry.folderId);

	if (folder == null) {
		long oldFolderId = fileEntry.getFolderId();

		try {
			DLFolder lost_found = null;

			if (!newFoldersMap.containsKey(fileEntry.getGroupId())) {
				lost_found = DLFolderLocalServiceUtil.addFolder(userId, fileEntry.getGroupId(),
						fileEntry.getRepositoryId(), false, 0, "lost+found", "", false, new ServiceContext());

				newFoldersMap.put(fileEntry.getGroupId(), lost_found);
			} else {
				lost_found = newFoldersMap.get(fileEntry.getGroupId());
			}

			DLFileEntryLocalServiceUtil.moveFileEntry(userId, fileEntry.getFileEntryId(),
				lost_found.getFolderId(), new ServiceContext());

			sb.append("<tr>");
			sb.append("<td>" + fileEntry.fileEntryId + "</td>");
			sb.append("<td>" + fileEntry.title + "</td>");
			sb.append("<td>" + oldFolderId + "</td>");
			sb.append("<td>" + lost_found.getFolderId() + "</td>");
			sb.append("<td>" + fileEntry.groupId + "</td>");
			sb.append("</tr>");
			movedFileCount++;
		}
		catch (Exception e) {
			out.println(e);
		}
	}

}

sb.append("<tr><td colspan=2><b>Number of recovered files</b></td><td>" + movedFileCount + "</td></tr>");

sb.append("</table>");

out.print("<a id=\"downloadReport\" download=\"lost+found_moved_files_");
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