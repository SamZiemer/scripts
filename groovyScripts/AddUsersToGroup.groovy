import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;

List<User> user_list = UserLocalServiceUtil.getUsers(QueryUtil.ALL_POS, QueryUtil.ALL_POS);

long groupId = 10184; // Change this

long[] userIds = new long[user_list.size()];

count = 0;

for (User user : user_list) {
	userIds[count] = user.getUserId();
	count++
	System.out.println(count);
}

UserLocalServiceUtil.addGroupUsers(groupId, userIds);