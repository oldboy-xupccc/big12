package com.oldboy.ssm.web.controller;

import com.oldboy.ssm.domain.User;
import com.oldboy.ssm.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2018/9/18.
 */
@Controller
@RequestMapping("/user")
public class UserController {

	@Resource(name="userService")
	private UserService us ;

	/**
	 * 查看用户列表
	 */
	@RequestMapping("/findlist")
	public String findList(@RequestParam(value="pid" ,defaultValue="1") int pno , Model m){
		//查询总的记录数
		int count = us.selectCount() ;

		//页面的数量
		int pages = 0 ;

		//每页记录数
		int recPerPage = 5 ;

		if(count % recPerPage == 0){
			pages = count / recPerPage ;
		}
		else{
			pages = count / recPerPage + 1 ;
		}

		List<User> list = us.findPage((pno - 1) * recPerPage , recPerPage);

		m.addAttribute("pages" , pages) ;
		m.addAttribute("users" , list) ;
		return "user/userList" ;
	}

	@RequestMapping("/delete")
	public String delete(Integer uid){
		System.out.println(uid);
		us.delete(uid);
		return "forward:/user/findlist";
	}

	@RequestMapping("/toadd")
	public String toadd(){
		return "user/userAdd" ;
	}

	@RequestMapping("/doadd")
	public String doadd(User user){
		us.insertUser(user);
		return "redirect:/user/findlist" ;
	}

	@RequestMapping("/edit")
	public String edit(Model m , Integer uid){
		User u = us.findById(uid) ;
		m.addAttribute("user" , u);
		return "/user/userEdit" ;
	}

	@RequestMapping("/update")
	public String update(User user){
		us.update(user) ;
		return "redirect:/user/findlist" ;
	}
}
