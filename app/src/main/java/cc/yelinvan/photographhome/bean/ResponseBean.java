package cc.yelinvan.photographhome.bean;

/**
 * Create by Johnson on 2018-12-26 14:39
 */
public  class ResponseBean<T>{
    private boolean code;
    private String msg;
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
