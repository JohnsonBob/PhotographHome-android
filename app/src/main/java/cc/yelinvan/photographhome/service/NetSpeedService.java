package cc.yelinvan.photographhome.service;

import android.app.Service;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.Nullable;
import cc.yelinvan.photographhome.eventbus.NetSpeedEvent;

/**
 * Create by Johnson on 2019-1-16 16:27
 * 实时获取当前网速的service
 */
public class NetSpeedService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private long total_data = TrafficStats.getTotalRxBytes();
    private Handler mHandler;
    //几秒刷新一次
    private final int count = 1;
    private NetSpeedEvent netSpeedEvent = new NetSpeedEvent();

    /**
     * 定义线程周期性地获取网速
     */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            //定时器
            mHandler.postDelayed(mRunnable, count * 1000);
            Message msg = mHandler.obtainMessage();
            msg.what = 1;
            msg.arg1 = getNetSpeed();
            mHandler.sendMessage(msg);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    //float real_data = (float)msg.arg1;
                    if(msg.arg1  > 1024 ){
                        System.out.println(msg.arg1 / 1024 + "kb/s");
                        netSpeedEvent.setSpeed(msg.arg1 / 1024);
                        netSpeedEvent.setUnit("kb/s");
                    }
                    else{
                        System.out.println(msg.arg1 + "b/s");
                        netSpeedEvent.setSpeed(msg.arg1);
                        netSpeedEvent.setUnit("b/s");
                    }
                    EventBus.getDefault().post(netSpeedEvent);
                }
            }
        };

    }

    /**
     * 获取当前网速
     * @return 当前网速
     */
    private int getNetSpeed() {
        long traffic_data = TrafficStats.getTotalRxBytes() - total_data;
        total_data = TrafficStats.getTotalRxBytes();
        return (int)traffic_data /count ;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandler.postDelayed(mRunnable, 0);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在服务结束时候删除消息队列
        mHandler.removeCallbacks(mRunnable);
    }
}
