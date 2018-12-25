package cc.yelinvan.photographhome.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Create by Johnson on 2018-12-25 10:00
 * 设置文本框清除按钮的显示及点击事件
 */
public class EditTextClearTools {
    public static void addClearListener(final EditText et , final ImageView iv){
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //如果有输入内容长度大于0那么显示clear按钮
                String str = editable.toString();
                if (editable.length() > 0){
                    iv.setVisibility(View.VISIBLE);
                }else{
                    iv.setVisibility(View.INVISIBLE);
                }
            }
        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et.setText("");
            }
        });
    }
}
