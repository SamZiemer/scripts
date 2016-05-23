import com.liferay.portal.kernel.dao.orm.QueryUtil
import com.liferay.portal.kernel.util.GetterUtil
import com.liferay.portal.kernel.util.StringBundler
import com.liferay.portal.security.auth.CompanyThreadLocal
import com.liferay.portal.util.PropsValues
import com.liferay.portlet.documentlibrary.NoSuchFileException
import com.liferay.portlet.documentlibrary.model.DLFileEntry
import com.liferay.portlet.documentlibrary.model.DLFileVersion
import com.liferay.portlet.documentlibrary.service.DLFileVersionLocalServiceUtil
import com.liferay.portlet.documentlibrary.store.DLStoreUtil

public StringBundler sb = new StringBundler();

// Check versions that do not have a downloadable file -- essentially, documents
// that are supposed to exist, but won't be available for download.

fileVersions = DLFileVersionLocalServiceUtil.getDLFileVersions(QueryUtil.ALL_POS, QueryUtil.ALL_POS)

sb.append("<h1>Entries missing corresponding file system Entry</h1>");

sb.append("<table border=1><tr><th>GroupId</th><th>FolderId</th><th>FileEntryId</th><th>FileVersion</th></tr>");

ArrayList<DLFileVersion> orphanedEntries = new ArrayList<DLFileVersion>();

for (fileVersion in fileVersions) {
	if (DLStoreUtil.hasFile(fileVersion.companyId, fileVersion.fileEntry.dataRepositoryId, fileVersion.fileEntry.name, fileVersion.version)) {
		continue;
	}

	orphanedEntries.add(fileVersion);

	groupId = fileVersion.groupId;
	folderId = fileVersion.folderId;
	fileEntryId = fileVersion.fileEntryId;
	version = fileVersion.version;

	sb.append("<tr><td>" + groupId + "</td><td>" + folderId + "</td><td>" + fileEntryId + "</td><td>" + version + "</td></tr>");
}

sb.append("</table>");

rootDir = new File(PropsValues.DL_STORE_FILE_SYSTEM_ROOT_DIR);
companyId = CompanyThreadLocal.companyId;
companyDir = new File(rootDir, String.valueOf(companyId));

Map<String, File> fileNames = new HashMap();

for (dlFolderDir in companyDir.listFiles()) {
	findFiles(dlFolderDir, sb, fileNames);
}

sb.append("<h1>Entries with file in another location</h1><div style=\" width: 100%; overflow-x:scroll; \"><table border=1><tr><th nowrap='nowrap'>Orphaned Entry Name</th><th nowrap='nowrap'>Orphaned Entry file ID</th><th nowrap='nowrap'>File Name</th><th nowrap='nowrap'>Old File Path</th><th nowrap='nowrap'>New File Path</th></tr>");

for (DLFileVersion entry : orphanedEntries) {
	file = fileNames.get(entry.getFileEntry().name);
	if (index != null) {
		DLFileEntry dlFileEntry= entry.getFileEntry();
		sb.append("<tr><td nowrap='nowrap'>");
		sb.append(entry.getFileEntry().name);
		sb.append("</td><td nowrap='nowrap'>");
		sb.append(entry.fileEntryId);
		sb.append("</td><td nowrap='nowrap'>");
		sb.append(file.getName());
		sb.append("</td><td nowrap='nowrap'>");
		sb.append(file.absolutePath);
		sb.append("</td><td nowrap='nowrap'>");
		try {
			dlFileEntry.getContentStream();
		} catch (NoSuchFileException nsfe) {
			unTrimedNewPath = nsfe.toString();
			index = unTrimedNewPath.indexOf(": ");
			newPath = unTrimedNewPath.substring(index+2);
			sb.append(newPath);
		}
		sb.append("</td></tr>");
	}
}
sb.append("</table></div>");

out.println(sb.toString());

public void findFiles(File file, StringBundler sb, Map<String, File> fileNames) {

	if (file.isDirectory()) {
		if(file.name == (String)0) {
			return;
		} else if (GetterUtil.getLong(file.name) == companyId) {
			return;
		}

		for (dlFile in file.listFiles()) {
			findFiles(dlFile, sb, fileNames);
		}
	} else {
		fileName = file.name;
		index = fileName.indexOf("_");

		if (index == -1) {
			return;
		} else {
			dlFileEntryName = fileName.substring(0, index);
		}

		fileNames.put(dlFileEntryName, file);
	}
}