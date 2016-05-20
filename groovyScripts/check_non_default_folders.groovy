import com.liferay.portal.kernel.dao.orm.DynamicQuery
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil
import com.liferay.portal.kernel.dao.orm.ProjectionList
import com.liferay.portal.kernel.exception.PortalException
import com.liferay.portal.kernel.exception.SystemException
import com.liferay.portal.kernel.util.DateUtil
import com.liferay.portal.kernel.util.HtmlUtil
import com.liferay.portal.kernel.util.StringBundler
import com.liferay.portal.model.Group
import com.liferay.portal.service.GroupLocalServiceUtil
import com.liferay.portal.template.ServiceLocator
import com.liferay.portlet.documentlibrary.model.DLFolder
import com.liferay.portlet.documentlibrary.service.persistence.DLFolderActionableDynamicQuery

ServiceLocator serviceLocator = new ServiceLocator();
service = serviceLocator.findService("sync-web", "com.liferay.sync.service.SyncDLObjectLocalService");

DynamicQuery dynamicQuery = service.dynamicQuery();
ProjectionList projectionList = ProjectionFactoryUtil.projectionList()
projectionList.add(ProjectionFactoryUtil.groupProperty("repositoryId"));
projectionList.add(ProjectionFactoryUtil.max("modifiedTime"));
dynamicQuery.setProjection(projectionList);

List results = service.dynamicQuery(dynamicQuery);

StringBundler sb = new StringBundler();

sb.append("<h1>Modified Date Check</h1>");
sb.append("<table border=1><tr><th>Site</th><th>Last Modified Time</th></tr>");

Date now = new Date();

for (Object result : results) {
    Object[] resultArray = (Object[])result;

    long groupId = (Long) resultArray[0];
    long modifiedTime = (Long) resultArray[1];
    Date modifiedDate = new Date(modifiedTime);

    Group group = GroupLocalServiceUtil.fetchGroup(groupId);
    String groupName = "UNKNOWN";

    if (group != null) {
        groupName = group.getDescriptiveName(Locale.US);
    }

    sb.append("<tr><td>");
    sb.append(groupName);
    sb.append("</td><td");

    if (modifiedDate.after(now)) {
        sb.append(" style=\"color: firebrick;\"")
   }

    sb.append(">")
    sb.append(modifiedDate);
    sb.append("</td></tr>");
}

sb.append("</table>");

sb.append("<hr />");

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

        _foundNonDefaultRepository = true;

        _sb.append("<tr><td>");
        _sb.append(folder.getName());
        _sb.append("</td><td>");

        Group group = GroupLocalServiceUtil.fetchGroup(folder.getGroupId());

        if (group == null) {
            _sb.append("--ORPHANED--");
        }
        else {
            _sb.append(group.getDescriptiveName());
        }

        _sb.append("</td><td>");
        _sb.append(folder.getRepositoryId());
        _sb.append("</td><td>")
        _sb.append(folder.getGroupId());
        _sb.append("</td></tr>");
    }

    private boolean foundNonDefaultRepository() {
        return _foundNonDefaultRepository;
    }

    private boolean _foundNonDefaultRepository = false;
    private StringBundler _sb;
}

sb.append("<h1>Repository ID Check</h1>")
sb.append("<table border=1><tr><th>Folder Name</th><th>Site</th><th>Repository ID</th><th>Group ID</th></tr>");

CheckDLFolderActionableDynamicQuery query = new CheckDLFolderActionableDynamicQuery(sb);
query.performActions();

if (!query.foundNonDefaultRepository()) {
    sb.append("<tr><td colspan=4>All folders are in a default repository</td></tr>");
}


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