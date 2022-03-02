package cn.yue.base.common.photo;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import cn.yue.base.common.photo.data.MediaType;
import cn.yue.base.common.photo.data.MediaVO;
import cn.yue.base.common.photo.data.MimeType;
import cn.yue.base.common.photo.data.MediaFolderVO;
import cn.yue.base.common.photo.loader.MediaFolderLoader;
import cn.yue.base.common.photo.loader.MediaLoader;

import static cn.yue.base.common.photo.loader.MediaFolderLoader.COLUMN_BUCKET_DISPLAY_NAME;
import static cn.yue.base.common.photo.loader.MediaFolderLoader.COLUMN_BUCKET_ID;
import static cn.yue.base.common.photo.loader.MediaFolderLoader.COLUMN_COUNT;
import static cn.yue.base.common.photo.loader.MediaFolderLoader.COLUMN_DATA;
import static cn.yue.base.common.photo.loader.MediaFolderLoader.COLUMN_URI;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

public class PhotoUtils {

    /**
     * 获取所有视频和图片集合
     * @param context
     * @return
     */
    public static List<MediaVO> getAllMedia(Context context) {
        return getMediaByFolder(context, true, null, MediaType.ALL);
    }

    /**
     * 获取所有图片文件
     * @param context
     * @return
     */
    public static List<MediaVO> getAllMediaPhotos(Context context) {
        return getMediaByFolder(context, true, null, MediaType.PHOTO);
    }

    /**
     * 获取所有视频
     * @param context
     * @return
     */
    public static List<MediaVO> getAllMediaVideos(Context context) {
        return getMediaByFolder(context, true, null, MediaType.VIDEO);
    }

    /**
     * 获取最近num张照片
     * @param num
     * @return
     */
    public static ArrayList<MediaVO> getTheLastMedias(Context context, int num, MediaType mediaType) {
        ArrayList<MediaVO> list = new ArrayList<>();
        ArrayList<MediaVO> photos = getMediaByFolder(context, true, null, mediaType);
        if (photos.size() > num) {
            list.addAll(photos.subList(0, num));
        } else {
            list.addAll(photos);
        }
        return list;
    }

    /**
     * 获取对应路径下的所有图片
     * @param context
     * @param isAll
     * @param folderId
     * @return
     */
    public static ArrayList<MediaVO> getPhotosByFolder(Context context, boolean isAll, String folderId) {
        return getMediaByFolder(context, isAll, folderId, MediaType.PHOTO);
    }

    /**
     * 获取对应路径下的所有资源
     * @param context
     * @param isAll
     * @param folderId
     * @param mediaType
     * @return
     */
    @SuppressLint("Range")
    public static ArrayList<MediaVO> getMediaByFolder(Context context, boolean isAll, String folderId, MediaType mediaType) {
        ArrayList<MediaVO> list = new ArrayList<>();
        Cursor cursor = MediaLoader.load(context, isAll, folderId, mediaType);
        while (cursor.moveToNext()) {
            MediaVO mediaVO = new MediaVO();
            String id = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            mediaVO.setId(id);
            String mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE));
            mediaVO.setMimeType(mimeType);
            mediaVO.setSize(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE));
            mediaVO.setDuration(cursor.getLong(cursor.getColumnIndex("duration")));
            int orientation = cursor.getInt(cursor.getColumnIndex("orientation"));
            if (orientation == 90 || orientation == 270) {
                mediaVO.setHeight(cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns.WIDTH)));
                mediaVO.setWidth(cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns.HEIGHT)));
            } else {
                mediaVO.setHeight(cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns.HEIGHT)));
                mediaVO.setWidth(cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns.WIDTH)));
            }
            Uri contentUri;
            if (MimeType.isImage(mimeType)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if (MimeType.isVideo(mimeType)) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else {
                // ?
                contentUri = MediaStore.Files.getContentUri("external");
            }
            mediaVO.setUri(ContentUris.withAppendedId(contentUri, Long.parseLong(id)));
            String data = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            mediaVO.setUrl(data);
            list.add(mediaVO);
        }
        return list;
    }

    /**
     * 获取照片文件夹
     * @param context
     * @return
     */
    public static List<MediaFolderVO> getAllMediasFolder(Context context) {
        return getAllMediaFolder(context, MediaType.ALL);
    }

    /**
     * 获取资源文件夹
     * @param context
     * @return
     */
    @SuppressLint("Range")
    public static List<MediaFolderVO> getAllMediaFolder(Context context, MediaType mediaType) {
        List<MediaFolderVO> list = new ArrayList<MediaFolderVO>();
        Cursor cursor = MediaFolderLoader.load(context, mediaType);
        while (cursor.moveToNext()) {
            String column = cursor.getString(cursor.getColumnIndex(COLUMN_URI));
            MediaFolderVO folderVO = new MediaFolderVO();
            folderVO.setId(cursor.getString(cursor.getColumnIndex(COLUMN_BUCKET_ID)));
            folderVO.setName(cursor.getString(cursor.getColumnIndex(COLUMN_BUCKET_DISPLAY_NAME)));
            folderVO.setCount(cursor.getInt(cursor.getColumnIndex(COLUMN_COUNT)));
            folderVO.setCoverUri(Uri.parse(column != null ? column : ""));
            folderVO.setPath(cursor.getString(cursor.getColumnIndex(COLUMN_DATA)));
            list.add(folderVO);
        }
        cursor.close();
        return list;
    }
}
