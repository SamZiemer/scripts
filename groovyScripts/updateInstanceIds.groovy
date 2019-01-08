import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.impl.PortletPreferencesModelImpl;
import com.liferay.portal.service.PortletPreferencesLocalServiceUtil;

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet;
import java.sql.Statement;

String _INSTANCE_SEPARATOR = "_INSTANCE_";

PortletPreferencesModelImpl pref = PortletPreferencesLocalServiceUtil.fetchPortletPreferences(1431776);

String oldPortletId = pref.getPortletId();

String newPortletId = oldPortletId + _INSTANCE_SEPARATOR + StringUtil.randomString(12);

Connection con = null;
Statement st = null;
try {
	con = DataAccess.getUpgradeOptimizedConnection();
	PreparedStatement ps1 = con.prepareStatement("update portletPreferences set portletId = ? where portletPreferencesId = 1431776");
	ps1.setString(1, newPortletId);
	ps1.executeUpdate();

	PreparedStatement ps2 = con.prepareStatement("select * from resourcePermission where primKey like '%_LAYOUT_110'");

	PreparedStatement ps3 = con.prepareStatement("update resourcePermission set primKey = ? where resourcePermissionId = ?");

	ResultSet rs = ps2.executeQuery();

	String instanceId = StringUtil.randomString(12);

	while (rs.next()) {
		String primKey = rs.getString("primKey");
		String newPrimKey = primKey + _INSTANCE_SEPARATOR + instanceId;

		ps3.setString(1, newPrimKey);
		ps3.setInt(2, rs.getInt("resourcePermissionId"));

		ps3.executeUpdate();
	}
}
finally {
	DataAccess.cleanUp(con, st);
}


