package cn.yue.base.middle.net.upload;

import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Description :
 * Created by yue on 2019/3/13
 */
public interface UploadServer {

    @POST("/img/upload/{uploadUrl}/multifile/multipart")
    @Multipart
    Single<ImageResultListData> upload(
            @Path("uploadUrl") String uploadUrl,
            @Part List<MultipartBody.Part> file);

}
