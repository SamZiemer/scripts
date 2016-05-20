import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFolderLocalServiceUtil

// Populate cache to speed up script

fileEntries = DLFileEntryLocalServiceUtil.getDLFileEntries(QueryUtil.ALL_POS, QueryUtil.ALL_POS);

StringBundler sb = new StringBundler();

sb.append("<h1>Files without folder</h1>");
sb.append("<table border=1><tr><th>FileEntryId</th><th>FileEntry Title</th><th>FolderId</th></tr>");

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
		sb.append("<td>" + fileEntry.fileEntryId + "</td>");
		sb.append("<td>" + fileEntry.title + "</td>");
		sb.append("<td>" + fileEntry.folderId + "</td>");
		sb.append("</tr>");
		missingFolderCount++;
		continue;
	}

}

sb.append("<tr><td colspan=2><b>Number of missing Folders</b></td><td>" + missingFolderCount + "</td></tr>");

sb.append("</table>");

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