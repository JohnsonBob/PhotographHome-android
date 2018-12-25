package cc.yelinvan.photographhome.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cc.yelinvan.photographhome.Constant;
import cc.yelinvan.photographhome.R;
import cc.yelinvan.photographhome.activity.base.BaseActivity;
import cc.yelinvan.photographhome.utils.EditTextClearTools;
import cc.yelinvan.photographhome.utils.SharedPreferencesHelper;

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

//        sharedPreferencesHelper.getSharedPreference(Constant.PASSWORD,"");
//        sharedPreferencesHelper.getSharedPreference(Constant.USERNAME,"");

    }

}
