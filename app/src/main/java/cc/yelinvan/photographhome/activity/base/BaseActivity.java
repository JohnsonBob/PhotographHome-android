package cc.yelinvan.photographhome.activity.base;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.WindowManager;

import org.xutils.BuildConfig;
import org.xutils.ex.HttpException;
import org.xutils.x;

import java.net.SocketTimeoutException;

import butterknife.ButterKnife;
import cc.yelinvan.photographhome.broadcastreceiver.NetBroadcastReceiver;
import cc.yelinvan.photographhome.utils.ActivityUtil;
import cc.yelinvan.photographhome.utils.ConstantUtil;
import cc.yelinvan.photographhome.utils.LogUtils;
import cc.yelinvan.photographhome.utils.ToastUtil;

/**
 * BaseActivity是所有Activity的基类，把一些公共的方法放到里面，如基础样式设置，权限封装，网络状态监听等
 * 2018年12月24日09:32:16
 */
public abstract class BaseActivity extends AppCompatActivity implements NetBroadcastReceiver.NetChangeListener {

    public static NetBroadcastReceiver.NetChangeListener netEvent;// 网络状态改变监听事件

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        // 沉浸效果
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }*/

        // 添加到Activity工具类
        ActivityUtil.getInstance().addActivity(this);

        // 初始化netEvent
        netEvent = this;

        // 执行初始化方法
        init();

        //执行ButterKnife框架初始化
        ButterKnife.bind(this);

        //日志工具初始化
        LogUtils.init(this);

        //xUtils3 框架初始化
        x.Ext.init(getApplication());
        if(LogUtils.APP_DBG){
            x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
        }
    }

    // 抽象 - 初始化方法，可以对数据进行初始化
    protected abstract void init();

    @Override
    protected void onResume() {
        super.onResume();
        Resources resources = this.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.fontScale = ConstantUtil.TEXTVIEWSIZE;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    @Override
    protected void onDestroy() {
        // Activity销毁时，提示系统回收
        // System.gc();
        netEvent = null;
        // 移除Activity
        ActivityUtil.getInstance().removeActivity(this);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 点击手机上的返回键，返回上一层
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 移除Activity
            ActivityUtil.getInstance().removeActivity(this);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 权限检查方法，false代表没有该权限，ture代表有该权限
     */
    public boolean hasPermission(String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    /**
     * 权限请求方法
     */
        public void requestPermission(int code, String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, code);
    }

    /**
     * 处理请求权限结果事件
     *
     * @param requestCode  请求码
     * @param permissions  权限组
     * @param grantResults 结果集
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doRequestPermissionsResult(requestCode, grantResults);
    }

    /**
     * 处理请求权限结果事件
     *
     * @param requestCode  请求码
     * @param grantResults 结果集
     */
    public void doRequestPermissionsResult(int requestCode, @NonNull int[] grantResults) {
    }

    /**
     * 网络状态改变时间监听
     *
     * @param netWorkState true有网络，false无网络
     */
    @Override
    public void onNetChange(boolean netWorkState) {
    }

    /**
     * xUtils框架网络请求失败处理
     * @param ex
     */
    public void requestError(Throwable ex){
        if(ex instanceof HttpException) { // 网络错误
            ToastUtil.showShort(getApplicationContext(),"网络异常，请检查后重试");
        }else if(ex instanceof SocketTimeoutException){ // 其他错误
            ToastUtil.showShort(getApplicationContext(),"网络请求超时，请检查后重试");
        }
    }
}
