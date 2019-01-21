package cc.yelinvan.photographhome;

import android.mtp.MtpDevice;

/**
 * Created by Johnson on 2018年12月24日09:40:42
 */

public class Constant {

    public static MtpDevice mtpDevice;
    public static String usbDeviceName="";
    public static boolean isUsbConnected=false;
    public static String LOGININFO = "logininfo";   //保存登录信息文件名
    public static String PASSWORD = "password";   //保存密码
    public static String USERNAME = "username";   //保存账号
    public static String AUTOLOGIN = "autologin";   //是否自动登录
    public static String REMEMBERPASSWORD = "rememberpassword";   //是否记住密码
    public static String TOKEN = "token";   //保存token
    public static final String SERVER = "http://johnson.cross.echosite.cn";
    public static int UPLOADMODE = 1;    //图片上传质量 1、标清 2、高清 3、原图
    public static int AUTOUPLOAD = 1;   // 图片上传方式 1:自动上传 2：手动上传

    public static class Url {
        //登录
        public static String LOGIN = SERVER + "/api/login";
        //获取相册列表
        public static String GETPROJECT = SERVER + "/api/getProject";
        //删除相册
        public static String DELETEPROJECT = SERVER + "/api/deleteProject";
    }
}
