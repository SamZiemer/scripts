import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFolderLocalServiceUtil;

import java.util.List;

int batchSize = 10000;
int numFolders;
int numFiles;

try {
	numFolders = DLFolderLocalServiceUtil.getDLFoldersCount();
	long userId = PermissionThreadLocal.getPermissionChecker().getUserId();

	for (int start = 0; start < numFolders; start += batchSize) {

		List<DLFolder> folders = DLFolderLocalServiceUtil.getDLFolders(start, start + batchSize);

		for (DLFolder folder : folders) {
			try {
				DLFolderLocalServiceUtil.updateDLFolder(folder);
			} catch (Exception e) {
				out.println("""<div class="portlet-msg-error">${e}</div>""");
				e.printStackTrace(out);
			}
		}
	}

	String foldersMsg = "processed " + numFolders + " folders.";
	out.println("""<div class="portlet-msg-success">${foldersMsg}</div>""");

	numFiles = DLFileEntryLocalServiceUtil.getFileEntriesCount();

	for (int start = 0; start < numFiles; start += batchSize) {
		List<DLFileEntry> files = DLFileEntryLocalServiceUtil.getFileEntries(start, start + batchSize);

		for (DLFileEntry entry : files) {
			try {
				DLFileEntryLocalServiceUtil.checkOutFileEntry(userId, entry.getFileEntryId(), new ServiceContext());
				DLFileEntryLocalServiceUtil.checkInFileEntry(userId, entry.getFileEntryId(), false, "Sync Update", new ServiceContext());
			} catch (Exception e) {
				out.println("""<div class="portlet-msg-error">${e}</div>""");
				e.printStackTrace(out);
			}
		}
	}

	String filesMsg = "processed " + numFiles + " files.";
	out.println("""<div class="portlet-msg-success">${filesMsg}</div>""");

} catch (Exception e) {
	out.println("""<div class="portlet-msg-error">${e}</div>""");
	e.printStackTrace(out);
}