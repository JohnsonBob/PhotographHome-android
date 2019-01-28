package cc.yelinvan.photographhome.service;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.mtp.MtpConstants;
import android.mtp.MtpDevice;
import android.mtp.MtpDeviceInfo;
import android.mtp.MtpObjectInfo;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import cc.yelinvan.photographhome.Constant;
import cc.yelinvan.photographhome.mtp.PicInfo;
import cc.yelinvan.photographhome.utils.FileUtils;
import cc.yelinvan.photographhome.utils.ToastUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static io.reactivex.Flowable.interval;

/**
 * Create by johnson on 2019/1/27 下午2:23
 */
public class MTPGetPhotoService extends Service {
    private static String TAG="MTPGetPhotoService";
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private StringBuilder filePath = new StringBuilder();
    private Context mContext;
    private UsbDevice mUsbDevice;
    private MtpDevice mMtpDevice;
    private Disposable disposable;
    private boolean isRegister=false;
    private AlertDialog mAlert;
    //通过binder实现了 调用者（client）与 service之间的通信
    private MyBinder binder = new MyBinder();


    BroadcastReceiver mtpReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent data) {
            switch (data.getAction()) {
                case UsbManager.ACTION_USB_DEVICE_ATTACHED:
                    UsbDevice usbDevice = data.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    checkMtpDevice(usbDevice, 1);
                    Constant.usbDeviceName = usbDevice.getDeviceName();
                    //attachedUsb(data);
                    break;
                case UsbManager.ACTION_USB_DEVICE_DETACHED:
                    Constant.usbDeviceName = "";
                    Constant.mtpDevice=null;
                    if (mMtpDevice != null) {
                        mMtpDevice.close();
                        disposable.dispose();
                    }
                    break;
                case ACTION_USB_PERMISSION:
                    if (data.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        usbDevice = data.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        checkMtpDevice(usbDevice, 2);
                    }
                    break;
            }
        }
    };

    public class MyBinder extends Binder{
        public MTPGetPhotoService getService(){
            return MTPGetPhotoService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AlertDialog.Builder builder = new AlertDialog.Builder(MTPGetPhotoService.this);
        mContext = MTPGetPhotoService.this;
        mAlert = builder.create();
        if(Constant.mtpDevice!=null) {
            mMtpDevice=Constant.mtpDevice;
            startScanPic();

        }
        else
        ToastUtil.showLong(mContext,"请重新插拔连接设备");
        registerReceiverMtp();

    }

    private void startScanPic(){
        disposable = interval(8, TimeUnit.SECONDS)
                .onBackpressureDrop()
                .map(new Function<Long, List>() {
                    @Override
                    public List apply(Long aLong) throws Exception {
                        Log.d(TAG,"start1===" + aLong);
                        List list = new ArrayList();
                        if (mMtpDevice != null) {
                            MtpDeviceInfo mtpDeviceInfo = mMtpDevice.getDeviceInfo();
                            String deviceSeriNumber = null;
                            if (mtpDeviceInfo != null)
                                //获取MTP设备的唯一序列号
                                deviceSeriNumber = mtpDeviceInfo.getSerialNumber();
                            else
                                deviceSeriNumber = "xx";
                            //获取存储单元id 也就是几张存储卡
                            int[] storageIds = mMtpDevice.getStorageIds();
                            if (storageIds == null) {
                                ToastUtil.showLong(mContext,"获取相机存储空间失败");

                                return list;
                            }
                            for (int storageId : storageIds) {
                                //获取 jpeg文件在 给定存储单元上所有对象的对象句柄列表
                                int[] objectHandles = mMtpDevice.getObjectHandles(storageId, MtpConstants.FORMAT_EXIF_JPEG, 0);
                                if(objectHandles==null){
                                    ToastUtil.showLong(mContext,"获取照片失败");
                                    return list;
                                }
                                for (int objectHandle : objectHandles) {
                                    MtpObjectInfo mtpobj = mMtpDevice.getObjectInfo(objectHandle);
                                    if (mtpobj == null) {
                                        continue;
                                    }
                                    //返回文件的创建日期 该值自1970年1月1日起以毫秒表示
                                    long dateCreated=mtpobj.getDateCreated();

                                    //以数组形式返回一个对象的jpeg格式缩略图
                                    byte[] bytes = mMtpDevice.getThumbnail(objectHandle);

                                    filePath.setLength(0);
                                    filePath.append(Environment.getExternalStorageDirectory().getAbsolutePath())
                                            .append(File.separator)
                                            .append("thumbCache")
                                            .append(File.separator)
                                            .append(String.valueOf(dateCreated))
                                            .append(".jpg");
                                    File fileJpg = new File(filePath.toString());
                                    if (!fileJpg.exists() && bytes != null)
                                        FileUtils.bytes2File(bytes, filePath.toString());

                                    PicInfo info = new PicInfo();
                                    info.setObjectHandler(objectHandle);
                                    info.setmThumbnailPath(fileJpg.getAbsolutePath());
                                    info.setmDateCreated(dateCreated);
                                    info.setmImagePixWidth(mtpobj.getImagePixWidth());
                                    info.setmImagePixHeight(mtpobj.getImagePixHeight());
                                    info.setmImagePixDepth(mtpobj.getImagePixDepth());
                                    info.setmThumbPixHeight(mtpobj.getThumbPixHeight());
                                    info.setmThumbPixWidth(mtpobj.getThumbPixWidth());
                                    info.setSequenceNumber(mtpobj.getSequenceNumber());
                                    info.setKeyWords(mtpobj.getKeywords());
                                    info.setmSerialNumber(deviceSeriNumber);

                                    list.add(info);
                                }
                            }
                        }
                        return list;
                    }
                }).subscribeOn(Schedulers.io())               //线程调度器,将发送者运行在子线程
                .observeOn(AndroidSchedulers.mainThread())          //接受者运行在主线程
                .subscribe((Consumer<? super List>) mContext);
    }

    private void registerReceiverMtp() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        intentFilter.addAction(ACTION_USB_PERMISSION);
        mContext.registerReceiver(mtpReceiver, intentFilter);
        isRegister=true;
    }

    private void checkMtpDevice(UsbDevice usbDevice, int key) {
        UsbManager manager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        boolean isOpenMtp;
        if (manager.hasPermission(usbDevice)) {
            UsbDeviceConnection usbDeviceConnection = manager.openDevice(usbDevice);
            mUsbDevice = usbDevice;
            mMtpDevice = new MtpDevice(usbDevice);
            isOpenMtp = mMtpDevice.open(usbDeviceConnection);
            Constant.usbDeviceName = mUsbDevice.getDeviceName();
        } else {
            PendingIntent mPermissionIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
            manager.requestPermission(usbDevice, mPermissionIntent);
            return;
        }

        Log.d("","isOpenMtp===" + isOpenMtp + usbDevice.getDeviceName());
        if (isOpenMtp) {
            Constant.mtpDevice = mMtpDevice;
            mAlert.hide();
            startScanPic();
        } else {
            //            handleMtpDevice(usbDevice, 3);
            mAlert.setMessage("与MTP建立连接失败，请重新插入MTP设备" + key );
            mAlert.show();
        }

    }




}
