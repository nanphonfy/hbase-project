package cn.nanphonfy.note.login.dao;

public interface LoginDao {
    /**
     * 从分布式缓存读取用户信息
     * key = TTSSTU+TTS+sha1(用户名+密码)
     *
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    boolean getLoginInfo(String userName, String password) throws Exception;
}
