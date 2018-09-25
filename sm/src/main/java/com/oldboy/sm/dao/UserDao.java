package com.oldboy.sm.dao;

import com.oldboy.sm.domain.User;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * userdao
 */
@Repository("userDao")
public class UserDao extends SqlSessionDaoSupport{

	public void insert(User user){
		getSqlSession().insert("users.insert" , user) ;
	}

	@Resource(name="sessionFactory")
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		super.setSqlSessionFactory(sqlSessionFactory);
	}

	public void delete(Integer id){
		getSqlSession().delete("users.delete" , id) ;
	}
}
