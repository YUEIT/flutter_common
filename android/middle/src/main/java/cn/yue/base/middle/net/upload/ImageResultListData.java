package cn.yue.base.middle.net.upload;

import java.util.List;

/**
 * Description :
 * Created by yue on 2018/7/24
 */

public class ImageResultListData {
    private int error;
    private List<ImageResult> data;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public List<ImageResult> getData() {
        return data;
    }

    public void setData(List<ImageResult> data) {
        this.data = data;
    }
}
