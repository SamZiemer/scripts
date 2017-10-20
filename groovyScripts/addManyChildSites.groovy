import com.liferay.portal.kernel.model.Group
import com.liferay.portal.kernel.service.GroupLocalServiceUtil
import com.liferay.portal.kernel.service.ServiceContext
import com.liferay.portal.kernel.service.ServiceContextFactory
import com.liferay.portal.kernel.util.LocaleUtil
import com.liferay.portal.kernel.util.LocalizationUtil

try {
	long userID = 20156
	long parentGroupId = 20143
	String className = null
	long classPK = 0
	long liveGroupId = 0
	String name = "ChildSite"
	Map<Locale, String> descriptionMap = LocalizationUtil.getLocalizationMap(
			actionRequest, "description")
	int type = 1
	boolean manualMembership = true
	int membershipRestriction = 0
	String friendlyURL = ""
	boolean site = true
	boolean inheritContent = false
	boolean active = true
	ServiceContext serviceContext = ServiceContextFactory.getInstance(
		Group.class.getName(), actionRequest)

	for (int i = 0; i < 10000; i++) {
		actionRequest.setAttribute("siteName", name + i)
		Map<Locale, String> nameMap = LocalizationUtil.getLocalizationMap(
				actionRequest, "siteName")


		nameMap.put(LocaleUtil.getDefault(), name + i)

		GroupLocalServiceUtil.addGroup(
				userID, parentGroupId, className, classPK,
				liveGroupId, nameMap, descriptionMap, type, manualMembership,
				membershipRestriction, friendlyURL, site, inheritContent,
				active, serviceContext)

		if (i % 100 == 0) {
			System.out.println("Added " + i + " Child Sites so far")
		}
	}
} catch (Exception e) {
	out.println(e);
}