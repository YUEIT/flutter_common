package cn.yue.base.common.photo.data;

public enum  MediaType {
    ALL,
    PHOTO,
    VIDEO;

    public static boolean onlyShowImages(MediaType mediaType) {
        return mediaType == MediaType.PHOTO;
    }

    public static boolean onlyShowVideos(MediaType mediaType) {
        return mediaType == MediaType.VIDEO;
    }
}
