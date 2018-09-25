package com.oldboy.ssm.service;

import com.oldboy.ssm.dao.UserDao;
import com.oldboy.ssm.domain.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

	public User findById(Integer id) {
		return dao.findById(id);
	}

	public List<User> findAll(){
		return dao.findAll();
	}

	public void update(User user){
		dao.update(user) ;
	}

	public int selectCount(){
		return dao.selectOneInt("users.selectCount") ;
	}

	public List<User> findPage(int offset, int len){
		return dao.findPage(offset, len) ;
	}
}
