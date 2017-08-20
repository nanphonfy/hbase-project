package cn.nanphonfy.note.login.dao.impl;

import cn.nanphonfy.note.login.dao.LoginDao;
import cn.nanphonfy.note.util.RedisTools;
import cn.nanphonfy.note.util.Constants;
import org.springframework.stereotype.Service;

@Service
public class LoginDaoImpl implements LoginDao {

	@Override
	public boolean getLoginInfo(String userName, String password) throws Exception {
		boolean flag = false;
		String userInfo = RedisTools.get(userName);
		if (userInfo!=null) {
			String[] split = userInfo.split("\\"+ Constants.STRING_SEPARATOR);
			if (password.equals(split[0])) {
				flag=true;
			}
		}
		return flag;
	}
}
