package cn.yue.base.middle.mvp.photo;

import java.util.List;

import cn.yue.base.middle.mvp.IBaseView;

/**
 * Description :
 * Created by yue on 2019/3/13
 */
public interface IPhotoView extends IBaseView {

    void selectImageResult(List<String> selectList);

    void cropImageResult(String image);

    void uploadImageResult(List<String> serverList);

}
