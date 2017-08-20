package cn.nanphonfy.note.login.service;

public interface LoginService {
    /**
     * 登录
     *
     * @param userName
     * @param password
     */
    boolean login(String userName, String password) throws Exception;
}
