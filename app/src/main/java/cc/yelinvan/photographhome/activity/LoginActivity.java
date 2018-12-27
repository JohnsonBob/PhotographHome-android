package cc.yelinvan.photographhome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;

import org.xutils.common.Callback;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cc.yelinvan.photographhome.Constant;
import cc.yelinvan.photographhome.R;
import cc.yelinvan.photographhome.activity.base.BaseActivity;
import cc.yelinvan.photographhome.bean.TokenBean;
import cc.yelinvan.photographhome.bean.ResponseBean;
import cc.yelinvan.photographhome.utils.EditTextClearTools;
import cc.yelinvan.photographhome.utils.NetParams;
import cc.yelinvan.photographhome.utils.SharedPreferencesHelper;
import cc.yelinvan.photographhome.utils.ToastUtil;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_userName)
    EditText etUserName;
    @BindView(R.id.iv_unameClear)
    ImageView ivUnameClear;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.iv_pwdClear)
    ImageView ivPwdClear;
    @BindView(R.id.cb_rm_password)
    CheckBox cbRmPassword;
    @BindView(R.id.cb_auto_login)
    CheckBox cbAutoLogin;
    @BindView(R.id.btn_login)
    Button btnLogin;
    private SharedPreferencesHelper sharedPreferencesHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiInit();

    }

    @Override
    protected void init() {
        setContentView(R.layout.activity_login);
        //设置状态栏颜色
        StatusBarUtil.setColor(this, getResources().getColor(R.color.logoScreenBackground), 0);
        StatusBarUtil.setLightMode(this);
        sharedPreferencesHelper = new SharedPreferencesHelper(this,Constant.LOGININFO);
    }

    private void uiInit(){
        EditTextClearTools.addClearListener(etUserName,ivUnameClear);
        EditTextClearTools.addClearListener(etPassword,ivPwdClear);

        String userPassword = (String) sharedPreferencesHelper.getSharedPreference(Constant.PASSWORD, "");
        String userName = (String) sharedPreferencesHelper.getSharedPreference(Constant.USERNAME,"");
        Boolean isautologin = (Boolean) sharedPreferencesHelper.getSharedPreference(Constant.AUTOLOGIN,false);
        Boolean isrememberpassword = (Boolean) sharedPreferencesHelper.getSharedPreference(Constant.REMEMBERPASSWORD,false);
        cbRmPassword.setChecked(isrememberpassword);
        cbAutoLogin.setChecked(isautologin);
        etUserName.setText(userName);
        etPassword.setText(userPassword);

        if(isautologin){
            if(!userPassword.isEmpty() && !userPassword.isEmpty()){
                onLogin();
            }
        }

    }

    /**
     * 自动登录点击事件 当自动登录勾选自动勾选记住密码
     */
    @OnClick (R.id.cb_auto_login)
    public void autoLoginClick(){
        if(cbAutoLogin.isChecked()){
            cbRmPassword.setChecked(true);
        }
    }

    /**
     * 记住密码点击事件 当记住密码选项未勾选设置自动登录为未勾选状态
     */
    @OnClick (R.id.cb_rm_password)
    public void rememberPassword(){
        if(!cbRmPassword.isChecked()){
            cbAutoLogin.setChecked(false);
        }
    }

    /**
     * 登录按钮点击事件
     */
    @OnClick (R.id.btn_login)
    public void onLogin(){
        String username = etUserName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if(username.isEmpty() || password.isEmpty()){
            ToastUtil.showLong(this,"用户名和密码不能为空！");
            return;
        }

        //登录
        NetParams netParams = new NetParams(this, Constant.Url.LOGIN,5000);
        netParams.addQueryStringParameter("mobile",username);
        netParams.addQueryStringParameter("password",password);
        x.http().post(netParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if(!result.isEmpty()){
                    ResponseBean<TokenBean> responseBean = new Gson().fromJson(result,
                            new TypeToken<ResponseBean<TokenBean>>() {}.getType());
                    if(responseBean.isCode() && !responseBean.getData().getToken().isEmpty()){
                        ToastUtil.showShort(LoginActivity.this, responseBean.getMsg());

                        //保存数据
                        if(cbRmPassword.isChecked()){
                            sharedPreferencesHelper.put(Constant.USERNAME, username);
                            sharedPreferencesHelper.put(Constant.PASSWORD, password);
                            sharedPreferencesHelper.put(Constant.TOKEN, responseBean.getData().getToken());
                            sharedPreferencesHelper.put(Constant.AUTOLOGIN, cbAutoLogin.isChecked());
                            sharedPreferencesHelper.put(Constant.REMEMBERPASSWORD, cbRmPassword.isChecked());
                        }
                        Intent intent = new Intent(LoginActivity.this, ProjectListActivity.class);
                        LoginActivity.this.startActivity(intent);
                        LoginActivity.this.finish();
                    }else {
                        ToastUtil.showShort(LoginActivity.this, responseBean.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoginActivity.this.requestError(ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }



}
