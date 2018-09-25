package com.oldboy.sm.service;

import com.oldboy.sm.dao.UserDao;
import com.oldboy.sm.domain.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 */
@Service("userService")
public class UserServiceImpl implements UserService {

	//注入dao对象给service
	@Resource(name="userDao")
	private UserDao dao ;

	public void insertUser(User user) {
		dao.insert(user);
	}

	public void delete(Integer id) {
		dao.delete(id);
	}
}
