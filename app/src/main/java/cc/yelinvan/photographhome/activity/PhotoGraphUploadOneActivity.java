package cc.yelinvan.photographhome.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import cc.yelinvan.photographhome.R;
import cc.yelinvan.photographhome.Receiver.BatteryReceiver;
import cc.yelinvan.photographhome.activity.base.BaseActivity;
import cc.yelinvan.photographhome.eventbus.NetSpeedEvent;
import cc.yelinvan.photographhome.service.NetSpeedService;

/**
 * Create by Johnson on 2019-1-16 11:56
 * 相册上传activity
 */
public class PhotoGraphUploadOneActivity extends BaseActivity {

    @BindView(R.id.camera_state_text)
    TextView cameraStateText;
    @BindView(R.id.network_speed_text)
    TextView networkSpeedText;
    @BindView(R.id.battery_text)
    TextView batteryText;
    @BindView(R.id.back_text)
    TextView backText;
    @BindView(R.id.flashupload_successsum)
    TextView flashuploadSuccesssum;
    @BindView(R.id.flashupload_photosum)
    TextView flashuploadPhotosum;
    @BindView(R.id.flashupload_failuresum)
    TextView flashuploadFailuresum;
    @BindView(R.id.flashupload_uploadingsum)
    TextView flashuploadUploadingsum;
    @BindView(R.id.flashupload_speed)
    TextView flashuploadSpeed;
    @BindView(R.id.flashupload_to_uploadlist_btn)
    FrameLayout flashuploadToUploadlistBtn;
    @BindView(R.id.album_flashupload_choose_storage_text)
    TextView albumFlashuploadChooseStorageText;
    @BindView(R.id.album_flashupload_choose_storage_icon)
    TextView albumFlashuploadChooseStorageIcon;
    @BindView(R.id.album_flashupload_choose_photo_text)
    TextView albumFlashuploadChoosePhotoText;
    @BindView(R.id.album_flashupload_choose_photo_num_text)
    TextView albumFlashuploadChoosePhotoNumText;
    @BindView(R.id.album_flashupload_choose_photo_layout)
    LinearLayout albumFlashuploadChoosePhotoLayout;
    @BindView(R.id.album_flashupload_choose_photo_icon)
    TextView albumFlashuploadChoosePhotoIcon;
    @BindView(R.id.album_flashupload_choose_upload_setting_text)
    TextView albumFlashuploadChooseUploadSettingText;
    @BindView(R.id.album_flashupload_choose_upload_setting_icon)
    TextView albumFlashuploadChooseUploadSettingIcon;
    @BindView(R.id.album_flashupload_setting_layout)
    LinearLayout albumFlashuploadSettingLayout;
    @BindView(R.id.tip_layout_close)
    TextView tipLayoutClose;
    @BindView(R.id.tip_layout)
    LinearLayout tipLayout;
    @BindView(R.id.album_flashupload_photo_list)
    RecyclerView albumFlashuploadPhotoList;
    @BindView(R.id.list_shade)
    View listShade;
    @BindView(R.id.flash_upload_list_tip_layout)
    LinearLayout flashUploadListTipLayout;
    @BindView(R.id.loading_progress)
    ProgressBar loadingProgress;
    @BindView(R.id.lookbigphoto_background)
    FrameLayout lookbigphotoBackground;
    @BindView(R.id.first_flashupload_tip)
    FrameLayout firstFlashuploadTip;
    @BindView(R.id.first_use_flashupload_tip)
    FrameLayout firstUseFlashuploadTip;

    private BatteryReceiver batteryReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void init() {
        setContentView(R.layout.activity_flashupload_one_layout);
        startService(new Intent(this,NetSpeedService.class));
    }

    private void initView() {
        //监听电量广播
        if(batteryText != null){
            batteryReceiver = new BatteryReceiver(batteryText);
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            registerReceiver(batteryReceiver, intentFilter);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消电量广播注册
        unregisterReceiver(batteryReceiver);
        EventBus.getDefault().unregister(this);
    }

    /**
     *  网速事件处理
     * @param netSpeedEvent
     */
    @Subscribe
    public void netSpeedEvent(NetSpeedEvent netSpeedEvent){
        networkSpeedText.setText(netSpeedEvent.toString());
    }

    /**
     * 返回按钮点击事件
     */
    @OnClick (R.id.back_text)
    public void goBack(){
        finish();
    }


    /**
     * 显示设置界面
     */
    @OnClick (R.id.album_flashupload_choose_upload_setting_icon)
    public void showSetting(){
        View settingItemView = LayoutInflater.from(this).inflate(R.layout.album_flashupload_choose_uploadmode, null);
        PopupWindow popupWindow = new PopupWindow(settingItemView, albumFlashuploadSettingLayout.getWidth(), -2,true);
        popupWindow.showAsDropDown(albumFlashuploadSettingLayout,0,0);

        TextView choosePhotoSize = settingItemView.findViewById(R.id.choose_photo_size_text);
        TextView chooseUploadMode = settingItemView.findViewById(R.id.choose_upload_mode_text);
        LinearLayout photoSizeLayout = settingItemView.findViewById(R.id.photo_size_layout);
        LinearLayout uploadModeLayout = settingItemView.findViewById(R.id.upload_mode_layout);
        LinearLayout choosePhotoSizeLayout = settingItemView.findViewById(R.id.choose_photo_size_layout);
        LinearLayout chooseUploadModeLayout = settingItemView.findViewById(R.id.choose_upload_mode_layout);


        TextView flash_choose_standard_text = settingItemView.findViewById(R.id.flash_choose_standard_text);
        TextView flash_choose_high_text = settingItemView.findViewById(R.id.flash_choose_high_text);
        TextView flash_choose_original_text = settingItemView.findViewById(R.id.flash_choose_original_text);
        TextView flash_choose_standard_icon = settingItemView.findViewById(R.id.flash_choose_standard_icon);
        TextView flash_choose_high_icon = settingItemView.findViewById(R.id.flash_choose_high_icon);
        TextView flash_choose_original_icon = settingItemView.findViewById(R.id.flash_choose_original_icon);


        TextView flash_choose_autoupload_text = settingItemView.findViewById(R.id.flash_choose_autoupload_text);
        TextView flash_choose_manualupload_text = settingItemView.findViewById(R.id.flash_choose_manualupload_text);
        TextView flash_choose_autoupload_icon = settingItemView.findViewById(R.id.flash_choose_autoupload_icon);
        TextView flash_choose_manualupload_icon = settingItemView.findViewById(R.id.flash_choose_manualupload_icon);




        //照片类型按钮点击按钮
        choosePhotoSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoSizeLayout.setVisibility(View.VISIBLE);
                uploadModeLayout.setVisibility(View.GONE);

                choosePhotoSizeLayout.setBackgroundColor(ContextCompat.getColor(
                        PhotoGraphUploadOneActivity.this,R.color.white));
                chooseUploadModeLayout.setBackgroundColor(ContextCompat.getColor(
                        PhotoGraphUploadOneActivity.this,R.color.colorCursor));
            }
        });

        //上传模式按钮点击按钮
        chooseUploadMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoSizeLayout.setVisibility(View.GONE);
                uploadModeLayout.setVisibility(View.VISIBLE);

                choosePhotoSizeLayout.setBackgroundColor(ContextCompat.getColor(
                        PhotoGraphUploadOneActivity.this,R.color.colorCursor));
                chooseUploadModeLayout.setBackgroundColor(ContextCompat.getColor(
                        PhotoGraphUploadOneActivity.this,R.color.white));
            }
        });

    }



}
