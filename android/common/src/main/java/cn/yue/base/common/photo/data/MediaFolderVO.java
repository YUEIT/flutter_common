package cn.yue.base.common.photo.data;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

public class MediaFolderVO implements Parcelable{

    private String id;
    private String name;
    private int count;
    private Uri coverUri;
    private String path;

    public MediaFolderVO() {
    }

    protected MediaFolderVO(Parcel in) {
        path = in.readString();
        name = in.readString();
        count = in.readInt();
        id = in.readString();
        coverUri = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<MediaFolderVO> CREATOR = new Creator<MediaFolderVO>() {
        @Override
        public MediaFolderVO createFromParcel(Parcel in) {
            return new MediaFolderVO(in);
        }

        @Override
        public MediaFolderVO[] newArray(int size) {
            return new MediaFolderVO[size];
        }
    };

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Uri getCoverUri() {
        return coverUri;
    }

    public void setCoverUri(Uri coverUri) {
        this.coverUri = coverUri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(name);
        dest.writeInt(count);
        dest.writeString(id);
        dest.writeParcelable(coverUri, flags);
    }
}
