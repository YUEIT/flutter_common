package cn.yue.base.middle.net.observer;

import java.util.List;
import java.util.concurrent.CancellationException;

import cn.yue.base.middle.net.NetworkConfig;
import cn.yue.base.middle.net.ResultException;
import cn.yue.base.middle.net.upload.ImageResult;
import cn.yue.base.middle.net.upload.ImageResultListData;
import io.reactivex.observers.DisposableSingleObserver;

/**
 * Description :
 * Created by yue on 2019/3/6
 */

public abstract class BaseUploadObserver extends DisposableSingleObserver<ImageResultListData> {

    @Override
    public void onError(Throwable e) {
        ResultException resultException;
        if (e instanceof ResultException) {
            resultException = (ResultException) e;
            onException(resultException);
        } else if (e instanceof CancellationException) {
            onCancel(new ResultException(NetworkConfig.ERROR_CANCEL, e.getMessage()));
        } else {
            onException(new ResultException(NetworkConfig.ERROR_OPERATION, e.getMessage()));
        }
    }

    public abstract void onException(ResultException e);

    public abstract void onSuccess(List<ImageResult> imageList);

    protected void  onCancel(ResultException e) {}

    @Override
    public void onSuccess(ImageResultListData imageResultListData) {
        if (imageResultListData == null || imageResultListData.getData() == null || imageResultListData.getData().isEmpty()) {
            onException(new ResultException(NetworkConfig.ERROR_SERVER, "上传失败"));
            return;
        }
        onSuccess(imageResultListData.getData());
    }
}
