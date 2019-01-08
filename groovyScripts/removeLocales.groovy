import com.liferay.portal.service.PortalPreferencesLocalServiceUtil

import javax.portlet.PortletPreferences

/*
 *From the PortalPreferences table that was supplied to us earlier on this ticket, we believe the companyId
 * you need to use is 10201. but please double check what the companyId is for your company.
 */

long ownerId = companyId = //INSERT COMPANY ID HERE!
int ownerType = 1

PortletPreferences preferences = PortalPreferencesLocalServiceUtil.getPreferences(
		companyId, ownerId, ownerType)

String newLocales = "en_US,es_ES"

String oldLocales = preferences.getValue("locales", null)

if (!oldLocales.equals(newLocales)) {
	preferences.setValue("locales", newLocales)
	preferences.store()
}