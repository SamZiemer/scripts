import com.liferay.counter.kernel.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;

import java.sql.Connection;

String uuid = PortalUUIDUtil.generate();
long storageLinkId = CounterLocalServiceUtil.increment("com.liferay.counter.kernel.model.Counter");
long classNameId = 1231050;
long classPK = 6627080;
long structureID = 5624476;

StringBundler sb = new StringBundler(12);

sb.append("INSERT INTO DDMStorageLink (uuid_, storageLinkId, classNameId, classPK, structureId) ");
sb.append("VALUES ('");
sb.append(uuid);
sb.append("', ");
sb.append(storageLinkId);
sb.append(", ");
sb.append(classNameId);
sb.append(", ");
sb.append(classPK);
sb.append(", ");
sb.append(structureID);
sb.append(");");

out.println(sb.toString());

Connection connection = DataAccess.getConnection();

DB db = DBManagerUtil.getDB();

db.runSQL(connection, sb.toString());