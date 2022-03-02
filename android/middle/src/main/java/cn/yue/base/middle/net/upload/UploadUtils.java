package cn.yue.base.middle.net.upload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.yue.base.common.activity.rx.ILifecycleProvider;
import cn.yue.base.middle.init.BaseUrlAddress;
import cn.yue.base.middle.init.InitConstant;
import cn.yue.base.middle.net.RetrofitManager;
import cn.yue.base.middle.net.observer.BaseUploadObserver;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Description :
 * Created by yue on 2019/3/13
 */
public class UploadUtils {

    private static final UploadServer uploadServer = RetrofitManager.getInstance().getRetrofit("http://upload").create(UploadServer.class);

    public static UploadServer getUploadServer() {
        return uploadServer;
    }
    public static <E> void upload(List<String> files, ILifecycleProvider<E> lifecycleProvider, BaseUploadObserver uploadObserver) {
        UploadUtils.getCompressFileList(files)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<List<File>, SingleSource<ImageResultListData>>() {
                    @Override
                    public SingleSource<ImageResultListData> apply(List<File> files) throws Exception {
                        String url;
                        if (InitConstant.isDebug()) {
                            url = "4tpBNVAu7iPQgmQetUXvXA";
                        } else {
                            url = getUploadKey();
                        }
                        return UploadUtils.getUploadServer().upload(url,filesToMultipartBodyParts(files));
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .compose(lifecycleProvider.<ImageResultListData>toBindLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(uploadObserver);
    }

    private static List<MultipartBody.Part> filesToMultipartBodyParts(List<File> files) {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (File file : files) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            parts.add(part);
        }
        return parts;
    }

    public static Single<List<File>> getCompressFileList(List<String> list) {
        return Single.just(list)
                .flatMap(new Function<List<String>, SingleSource<? extends List<File>>>() {
                    @Override
                    public SingleSource<? extends List<File>> apply(List<String> strings) throws Exception {
                        List<File> files = new ArrayList<>();
                        for (String url : strings) {
//                            File file = BitmapFileUtil.getCompressBitmapFile(url);
//                            if (file != null) {
//                                files.add(file);
//                            }
                        }
                        return Single.just(files);
                    }
                });
    }

    private static String getUploadKey() {
        return "LKdCxyqv0tvAuyT2NJmelw";
    }

}
