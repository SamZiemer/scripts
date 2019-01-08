import com.liferay.portal.kernel.model.Group
import com.liferay.portal.kernel.model.GroupConstants
import com.liferay.portal.kernel.service.GroupLocalServiceUtil
import com.liferay.portal.kernel.util.UnicodeProperties

long groupId = 4323281

Group group = GroupLocalServiceUtil.getGroup(groupId)

UnicodeProperties typeSettings = group.getTypeSettingsProperties()

String inherit = "true"

typeSettings.setProperty(GroupConstants.TYPE_SETTINGS_KEY_INHERIT_LOCALES, inherit)

String locales = "en_US,fr_CA"

typeSettings.setProperty("locales", locales)

group.setTypeSettingsProperties(typeSettings)

GroupLocalServiceUtil.updateGroup(group)
