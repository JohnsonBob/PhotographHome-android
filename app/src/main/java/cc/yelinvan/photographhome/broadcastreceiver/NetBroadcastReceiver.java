package cc.yelinvan.photographhome.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import cc.yelinvan.photographhome.activity.base.BaseActivity;
import cc.yelinvan.photographhome.activity.base.BaseFragment;
import cc.yelinvan.photographhome.activity.base.BaseFragmentActivity;
import cc.yelinvan.photographhome.utils.NetworkUtil;


/**
 * 检查网络状态切换 - 广播接受器
 *
 * @author Johnson 2018年12月24日09:39:19
 */
public class NetBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 如果相等的话就说明网络状态发生了变化  
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            boolean netWorkState = NetworkUtil.isNetworkConnected(context);
            // 接口回调传过去状态的类型
            if (BaseActivity.netEvent != null)
                BaseActivity.netEvent.onNetChange(netWorkState);
            if (BaseFragment.netEvent != null)
                BaseFragment.netEvent.onNetChange(netWorkState);
            if (BaseFragmentActivity.netEvent != null)
                BaseFragmentActivity.netEvent.onNetChange(netWorkState);
        }
    }

    // 网络状态变化接口
    public interface NetChangeListener {
        void onNetChange(boolean netWorkState);
    }
}  