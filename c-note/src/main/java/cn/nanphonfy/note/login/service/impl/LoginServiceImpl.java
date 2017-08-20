package cn.nanphonfy.note.login.service.impl;

import cn.nanphonfy.note.login.dao.LoginDao;
import cn.nanphonfy.note.login.service.LoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LoginServiceImpl implements LoginService {
    @Resource(name = "loginDaoImpl")
    private LoginDao loginDao;

    @Override
    public boolean login(String userName, String password) throws Exception {
        return loginDao.getLoginInfo(userName, password);
    }
}
