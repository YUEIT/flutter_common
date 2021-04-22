package cn.yue.base.common.photo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

public class PhotoFolderVO implements Parcelable{

    private String path;
    private String cover;
    private String name;
    private int count;

    public PhotoFolderVO(String path, String cover, String name, int count) {
        this.path = path;
        this.cover = cover;
        this.name = name;
        this.count = count;
    }

    protected PhotoFolderVO(Parcel in) {
        path = in.readString();
        cover = in.readString();
        name = in.readString();
        count = in.readInt();
    }

    public static final Creator<PhotoFolderVO> CREATOR = new Creator<PhotoFolderVO>() {
        @Override
        public PhotoFolderVO createFromParcel(Parcel in) {
            return new PhotoFolderVO(in);
        }

        @Override
        public PhotoFolderVO[] newArray(int size) {
            return new PhotoFolderVO[size];
        }
    };

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(cover);
        dest.writeString(name);
        dest.writeInt(count);
    }
}
