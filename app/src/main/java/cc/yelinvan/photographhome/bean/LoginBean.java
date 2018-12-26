package cc.yelinvan.photographhome.bean;

/**
 * Create by Johnson on 2018-12-26 14:39
 */
public class LoginBean {
    private boolean code;
    private String msg;
    private DataBean data;

    public boolean isCode() {
        return code;
    }

    public void setCode(boolean code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }
}
