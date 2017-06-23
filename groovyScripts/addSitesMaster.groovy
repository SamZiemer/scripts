/**
 * @author Samuel Ziemer
 */

import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.model.Group
import com.liferay.portlet.ActionRequestImpl;

import javax.portlet.ActionRequest;
import javax.portlet.PortletRequest;

numGroups =100;

userId = 20153;
parentGroupId = 0;
className = null;
classPk = 0;
liveGroupId = 0;
nameMap = new HashMap<String, String>();
nameMap.put("zh_CN", null);
nameMap.put("es_ES", null);
nameMap.put("ja_JP", null);
nameMap.put("iw_IL", null);
nameMap.put("nl_NL", null);
nameMap.put("fi_FI", null);
nameMap.put("ca_ES", null);
nameMap.put("hu_HU", null);
nameMap.put("fr_FR", null);
nameMap.put("pt_BR", null);
nameMap.put("de_DE", null);

descriptionMap = new HashMap<String, String>();
descriptionMap.put("zh_CN", null);
descriptionMap.put("es_ES", null);
descriptionMap.put("ja_JP", null);
descriptionMap.put("iw_IL", null);
descriptionMap.put("nl_NL", null);
descriptionMap.put("fi_FI", null);
descriptionMap.put("ca_ES", null);
descriptionMap.put("hu_HU", null);
descriptionMap.put("fr_FR", null);
descriptionMap.put("pt_BR", null);
descriptionMap.put("de_DE", null);

type = 1;
manualMembership = true;
membershipRescritcion = 0;
friendlyURL = "";
site = true;
inheritContent = false;
active = true;
ActionRequest actionRequest = new ActionRequestImpl();
ServiceContext serviceContext = ServiceContextFactory.getInstance(Group.class.getName(), actionRequest);


nameRoot = "testing";
descriptionRoot = "test";

for (int i = 0; i < numGroups; i++) {
	name = nameRoot + i;
	description = descriptionRoot +i;
	nameMap.put("en_US", name);
	descriptionMap.put("en_US", description);

	GroupLocalServiceUtil.addGroup(userId, parentGroupId, className, classPk, liveGroupId,
		nameMap, descriptionMap, type, manualMembership, membershipRescritcion, friendlyURL,
		site, inheritContent, active);
}