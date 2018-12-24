package cc.yelinvan.photographhome.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jaeger.library.StatusBarUtil;

import cc.yelinvan.photographhome.R;
import cc.yelinvan.photographhome.activity.base.BaseActivity;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {
        setContentView(R.layout.activity_login);
        //设置状态栏颜色
        StatusBarUtil.setColor(this, getResources().getColor(R.color.logoScreenBackground),0);
        StatusBarUtil.setLightMode(this);
    }

}
