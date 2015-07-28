package com.wswenyue.db.test;
import com.wswenyue.db.domain.User;
import com.wswenyue.db.service.UserManagementService;
import org.junit.Test;

public class UserManagementServiceTest {
UserManagementService userManagementService = new UserManagementService();
	User user = new User();
	User user1 = new User();

	public void init(){

		user.setUname("张三");
		user.setPassword("1234");
		user.setEmail("zhangsan@163.com");
		user.setPhone("18369956765");

		user1.setUname("张万新");
		user1.setPassword("1234");
		user1.setEmail("zhangsan@163.com");
		user1.setPhone("1234333");
	}
	@Test
	public void test(){
		init();
		userManagementService.Regiester(user1);

	}
	@Test
	public void test2(){
		init();
		userManagementService.Login(user);

	}
	@Test
	public void test3(){
		init();
		userManagementService.Logout(user);

	}
	@Test
	public void test4(){
		init();
		userManagementService.ChangePassword(user1,"123456");

	}
}
