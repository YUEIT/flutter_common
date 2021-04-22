package cn.yue.base.middle.mvp.photo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.yue.base.common.activity.BaseFragmentActivity;
import cn.yue.base.common.activity.PermissionCallBack;
import cn.yue.base.common.utils.app.RunTimePermissionUtil;
import cn.yue.base.common.utils.debug.ToastUtils;
import cn.yue.base.common.utils.file.BitmapFileUtil;
import cn.yue.base.middle.net.ResultException;
import cn.yue.base.middle.net.observer.BaseUploadObserver;
import cn.yue.base.middle.net.upload.ImageResult;
import cn.yue.base.middle.net.upload.UploadUtils;

/**
 * Description :
 * Created by yue on 2019/3/13
 */
public class PhotoHelper {

    private BaseFragmentActivity context;
    private IPhotoView iPhotoView;

    public PhotoHelper(BaseFragmentActivity context, IPhotoView iPhotoView) {
        this.context = context;
        this.iPhotoView = iPhotoView;
    }

    private int maxNum = 1;

    public PhotoHelper setMaxNum(int maxNum) {
        this.maxNum = maxNum;
        return this;
    }

    private boolean noHintText;

    public PhotoHelper setNoHintText(boolean noHintText) {
        this.noHintText = noHintText;
        return this;
    }

    /**
     * 默认选择完成后会缓存选择的图片，下次继续调用时会传入上传缓存的图片
     */
    public void toAlbum() {
        RunTimePermissionUtil.requestPermissions(context, new PermissionCallBack() {
            @Override
            public void requestSuccess(String permission) {
                ARouter.getInstance().build("/common/selectPhoto")
                        .withStringArrayList("photos", (ArrayList<String>) selectCache)
                        .withInt("maxNum", maxNum)
                        .navigation(context, REQUEST_SELECT_PHOTO);
            }

            @Override
            public void requestFailed(String permission) {

            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 需要对选择完成后的图片进行删减操作的，重新传入删减完成后的list
     *
     * @param selectList
     */
    public void toAlbum(List<String> selectList) {
        selectCache.clear();
        if (selectList != null && selectList.size() > 0) {
            selectCache.addAll(selectList);
        }
        RunTimePermissionUtil.requestPermissions(context, new PermissionCallBack() {
            @Override
            public void requestSuccess(String permission) {
                ARouter.getInstance().build("/common/selectPhoto")
                        .withStringArrayList("photos", (ArrayList<String>) selectCache)
                        .withInt("maxNum", maxNum)
                        .navigation(context, REQUEST_SELECT_PHOTO);
            }

            @Override
            public void requestFailed(String permission) {

            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public void toCamera() {
        RunTimePermissionUtil.requestPermissions((BaseFragmentActivity) context, new PermissionCallBack() {
            @Override
            public void requestSuccess(String permission) {
                String cachePath = BitmapFileUtil.getPhotoCameraPath();
                if (TextUtils.isEmpty(cachePath)) {
                    ToastUtils.showShortToast("无可用空间存储相片");
                    return;
                }
                // 允许用户上传多张拍摄图片
                File tempFile = BitmapFileUtil.createRandomFile();
                cachePhotoPath = tempFile.getAbsolutePath();
                targetUri = Uri.fromFile(tempFile);
                if (targetUri != null) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, targetUri);
                    //intent.putExtra("return-data", false);//若为false则表示不返回数据
                    context.startActivityForResult(intent, REQUEST_CAMERA);
                }
            }

            @Override
            public void requestFailed(String permission) {

            }
        }, Manifest.permission.CAMERA);
    }

    public void toAutoCrop() {
        toCrop(true, 0, 0);
    }

    public void toCrop() {
        toCrop(false, 1, 1);
    }

    public void toCrop(boolean autoCrop, int aspectX, int aspectY) {
        Uri outPutUri;
        if (cachePhotoPath != null) {
            targetUri = Uri.fromFile(new File(cachePhotoPath));
            File tempFile = BitmapFileUtil.createRandomFile();
            cachePhotoPath = tempFile.getAbsolutePath();
            outPutUri = Uri.fromFile(tempFile);
        } else {
            ToastUtils.showShortToast("没有裁剪的图片~");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(targetUri, "image/*");
        intent.putExtra("crop", "true");//可裁剪
        if (!autoCrop) {
            intent.putExtra("aspectX", aspectX);
            intent.putExtra("aspectY", aspectY);
        }
        intent.putExtra("scale", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
        //intent.putExtra("return-data", false);//若为false则表示不返回数据
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        context.startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }

    public void upload() {
        upload(cachePhotoPath);
    }

    public void upload(String image) {
        List<String> list = new ArrayList<>();
        list.add(image);
        upload(list);
    }

    public void upload(List<String> imageList) {
        UploadUtils.upload(imageList, iPhotoView, new BaseUploadObserver() {

            @Override
            protected void onStart() {
                super.onStart();
                iPhotoView.showWaitDialog(noHintText ? "" : "上传中~");
            }

            @Override
            public void onException(ResultException e) {
                iPhotoView.dismissWaitDialog();
                ToastUtils.showShortToast("上传失败：" + e.getMessage());
            }

            @Override
            public void onSuccess(List<ImageResult> imageList) {
                iPhotoView.dismissWaitDialog();
                ToastUtils.showShortToast("上传成功~");
                List<String> serverList = new ArrayList<>();
                for (ImageResult imageResult : imageList) {
                    serverList.add(imageResult.getUrl());
                }
                iPhotoView.uploadImageResult(serverList);
            }
        });
    }

    private Uri targetUri;
    private String cachePhotoPath;
    private List<String> selectCache = new ArrayList<>();
    private static final int REQUEST_SELECT_PHOTO = 10001;
    private static final int REQUEST_CAMERA = 10002;
    private static final int REQUEST_CROP_PHOTO = 10003;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_SELECT_PHOTO:
                if (data != null) {
                    List<String> selectList = data.getStringArrayListExtra("photos");
                    selectCache.clear();
                    selectCache.addAll(selectList);
                    if (selectList.size() == 1) { //只选择一张图时可以裁剪
                        cachePhotoPath = selectList.get(0);
                    }
                    iPhotoView.selectImageResult(selectList);
                }
                break;
            case REQUEST_CROP_PHOTO:
                iPhotoView.cropImageResult(cachePhotoPath);
                break;
            case REQUEST_CAMERA:
                List<String> temp = new ArrayList<>();
                temp.add(cachePhotoPath);
                iPhotoView.selectImageResult(temp);
                break;
            default:
                break;
        }
    }
}
