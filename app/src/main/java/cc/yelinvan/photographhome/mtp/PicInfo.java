package cc.yelinvan.photographhome.mtp;

import java.io.Serializable;

/**
 * Created by apple on 2018/3/26.
 */

public class PicInfo implements Serializable{

    private String mThumbnailPath;      //手机中缩略图路径
    private long mDateCreated;      //照片创建时间
    private int mThumbPixWidth;     //缩略图宽度
    private int mThumbPixHeight;    //缩略图高度
    private int mImagePixWidth;     //照片宽度
    private int mImagePixHeight;    //照片高度
    private int sequenceNumber;     //照片对象序列号
    private int objectHandler;      //照片文件对象句柄
    //暂时放图片在相机中的路径
    private String  keyWords;
    private String mSerialNumber;   //mtp设备唯一序列号

    public String getmSerialNumber() {
        return mSerialNumber;
    }

    public void setmSerialNumber(String mSerialNumber) {
        this.mSerialNumber = mSerialNumber;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }


    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }



    public int getObjectHandler() {
        return objectHandler;
    }

    public void setObjectHandler(int objectHandler) {
        this.objectHandler = objectHandler;
    }

    public String getmThumbnailPath() {
        return mThumbnailPath;
    }

    public void setmThumbnailPath(String mThumbnailPath) {
        this.mThumbnailPath = mThumbnailPath;
    }

    public long getmDateCreated() {
        return mDateCreated;
    }

    public void setmDateCreated(long mDateCreated) {
        this.mDateCreated = mDateCreated;
    }

    public int getmThumbPixWidth() {
        return mThumbPixWidth;
    }

    public void setmThumbPixWidth(int mThumbPixWidth) {
        this.mThumbPixWidth = mThumbPixWidth;
    }

    public int getmThumbPixHeight() {
        return mThumbPixHeight;
    }

    public void setmThumbPixHeight(int mThumbPixHeight) {
        this.mThumbPixHeight = mThumbPixHeight;
    }

    public int getmImagePixWidth() {
        return mImagePixWidth;
    }

    public void setmImagePixWidth(int mImagePixWidth) {
        this.mImagePixWidth = mImagePixWidth;
    }

    public int getmImagePixHeight() {
        return mImagePixHeight;
    }

    public void setmImagePixHeight(int mImagePixHeight) {
        this.mImagePixHeight = mImagePixHeight;
    }

    public int getmImagePixDepth() {
        return mImagePixDepth;
    }

    public void setmImagePixDepth(int mImagePixDepth) {
        this.mImagePixDepth = mImagePixDepth;
    }

    private int mImagePixDepth;


    @Override
    public int hashCode() {
        return (int)(mDateCreated/1000);
    }

    @Override
    public boolean equals(Object obj) {
        PicInfo pic=(PicInfo)obj;
        if(keyWords.equals(pic.getKeyWords())){
            return true;
        }else{
            return false;
        }
    }
}
