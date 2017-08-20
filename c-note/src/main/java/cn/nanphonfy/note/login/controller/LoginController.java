package cn.nanphonfy.note.login.controller;

import cn.nanphonfy.note.login.service.LoginService;
import cn.nanphonfy.note.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/login")
public class LoginController {
	private static Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	private LoginService loginService;
	/**
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/showloginpage")
	public String login(HttpServletRequest request,String loginName,String password)throws Exception {
		logger.info("enter...");
		loginService.login(loginName,password);
		return "login/login";
	}

	@RequestMapping("/loginnow")
	public String loginin(HttpServletRequest request,String loginName,String password){
		try {
			if (loginName == null || "".equals(loginName) || password == null || "".equals(password)) {
				return "error/404";
			}
			request.getSession().setAttribute(Constants.USER_INFO, loginName.trim());
		} catch (Exception e) {
			logger.error("登陆失败：loginName:" + loginName + ";", e);
			e.printStackTrace();
		}
		return "note/inotecenter";
	}
}
