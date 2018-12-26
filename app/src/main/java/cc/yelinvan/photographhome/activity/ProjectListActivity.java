package cc.yelinvan.photographhome.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jaeger.library.StatusBarUtil;

import cc.yelinvan.photographhome.R;
import cc.yelinvan.photographhome.activity.base.BaseActivity;

/**
 * 相册列表activity
 *  Create by Johnson on 2018年12月26日17:05:06
 */
public class ProjectListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
    }

    @Override
    protected void init() {
        //设置状态栏颜色
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorAccent), 0);

    }
}
