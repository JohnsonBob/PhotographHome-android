package cc.yelinvan.photographhome.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

/**
 * Create by Johnson on 2019-1-16 15:58
 * 获取电量信息并显示在textview上
 */
public class BatteryReceiver extends BroadcastReceiver {
    private TextView textView;

    public BatteryReceiver(TextView textView){
        this.textView = textView;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        int current = intent.getExtras().getInt("level");// 获得当前电量
        int total = intent.getExtras().getInt("scale");// 获得总电量
        int percent = current * 100 / total;
        textView.setText("电量："+ percent + "%");
    }
}
