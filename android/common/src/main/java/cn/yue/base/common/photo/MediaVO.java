package cn.yue.base.common.photo;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

public class MediaVO {

    private long modifyTime;

    private String url;

    public MediaVO(long modifyTime, String url) {
        this.modifyTime = modifyTime;
        this.url = url;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
