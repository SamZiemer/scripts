import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.persistence.DLFolderActionableDynamicQuery;

StringBundler sb = new StringBundler();

class CheckDLFolderActionableDynamicQuery extends DLFolderActionableDynamicQuery {
	public CheckDLFolderActionableDynamicQuery(StringBundler sb) {
	_sb = sb;
	}

	 @Override
	protected void performAction(Object object) throws PortalException, SystemException {
	DLFolder folder = (DLFolder)object;

		boolean isDefaultRepository = folder.getGroupId() == folder.getRepositoryId();

		if (isDefaultRepository) {
			return;
		}
		_numFolders++;
		_foundNonDefaultRepository = true;

		_sb.append("<tr><td>");
		_sb.append(folder.getName());
		_sb.append("</td><td>");

		Group group = GroupLocalServiceUtil.fetchGroup(folder.getGroupId());

		_sb.append(group.getDescriptiveName());


		_sb.append("</td><td>");
		_sb.append(folder.getRepositoryId());
		_sb.append("</td><td>");
		_sb.append(folder.getGroupId());
		_sb.append("</td><td>");

		long tempNumfiles = DLFileEntryLocalServiceUtil.getFileEntriesCount(folder.getGroupId(), folder.getFolderId());

		_sb.append(tempNumfiles);

		_numFiles += tempNumfiles;

		_sb.append("</td></tr>");


	}

	private boolean foundNonDefaultRepository() {
		return _foundNonDefaultRepository;
	}

	private long numFolders() {
		return _numFolders;
	}

	private long numFiles() {
		return _numFiles;
	}

	private boolean _foundNonDefaultRepository = false;

	private long _numFolders = 0;
	private long _numFiles = 0;

	private StringBundler _sb;
}

sb.append("<h1>Repository ID Check</h1>")
sb.append("<table border=1><tr><th>Folder Name</th><th>Site</th><th>Repository ID</th><th>Group ID</th><th>files in this folder</th></tr>");

CheckDLFolderActionableDynamicQuery query = new CheckDLFolderActionableDynamicQuery(sb);
query.performActions();

if (!query.foundNonDefaultRepository()) {
	sb.append("<tr><td colspan=5>All folders are in a default repository</td></tr>");
}

sb.append("<tr style='border:0px solid #fff;'><td bgcolor=\"#FFFFFF\" style=\"line-height:15px;\" colspan=5>&nbsp;</td></tr><tr><td style='border:0px solid #fff;' /><th>Number of Folders Found:</th><td>");
sb.append(query.numFolders());
sb.append("</td><th>Number of Files Found:</th><td>");
sb.append(query.numFiles());
sb.append("</td></tr>");

sb.append("</table>");

out.print("<a id=\"downloadReport\" download=\"repository_report_");
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