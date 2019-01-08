import com.liferay.portal.kernel.dao.jdbc.DataAccess
import com.liferay.portal.kernel.service.PortletPreferencesLocalServiceUtil

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

import javax.portlet.PortletPreferences

Connection connection = DataAccess.getConnection()

PreparedStatement ps = connection.prepareStatement("SELECT * FROM portletPreferences " +
	"WHERE portletId like 'com_liferay_document_library_web_portlet_DLPortlet%'")

ResultSet rs = ps.executeQuery()

while (rs.next()) {
	long portletPreferencesId = rs.getLong("portletPreferencesId")
	long companyId = rs.getLong("companyId")
	long ownerId = rs.getLong("ownerId")
	int ownerType = rs.getInt("ownerType")
	long plid = rs.getLong("plid")
	String portletId = rs.getString("portletId")

	PortletPreferences preferences =
		PortletPreferencesLocalServiceUtil.getPreferences(companyId, ownerId, ownerType, plid, portletId)

	String showActions = preferences.getValue("showActions", null)

	out.println(showActions)

	if (showActions != null && !showActions.equals("true")) {
		preferences.setValue("showActions", "true")
		out.println("setting true for " + portletPreferencesId)

		PortletPreferencesLocalServiceUtil.updatePreferences(
				ownerId, ownerType, plid, portletId, preferences)
	}
}

connection.close()
rs.close()
ps.close()