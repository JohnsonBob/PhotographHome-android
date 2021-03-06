package cc.yelinvan.photographhome.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import cc.yelinvan.photographhome.R;


/**
 * Created by Johnson on 2018年12月26日16:44:45
 */

public class CustomDialog extends Dialog {
    protected CustomDialog(Context context, int layout, int style) {
        this(context, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, layout,style, Gravity.CENTER);
    }

    public CustomDialog(Context context, int width, int height
            , int layout, int style, int gravity) {
        this(context,width,height,layout,style,gravity, R.style.pop_anim_style);
    }

    public CustomDialog(Context context, int width, int height,
                        int layout, int style, int gravity, int anim) {
        super(context, style);
        setContentView(layout);
        Window window=getWindow();
        WindowManager.LayoutParams layoutParams=window.getAttributes();
        layoutParams.width=width;
        layoutParams.height=height;
        layoutParams.gravity=gravity;
        window.setAttributes(layoutParams);
        window.setWindowAnimations(anim);

    }
}