package cn.yue.base.common.photo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

public class PhotoUtils {

    /**
     * 获取所有视频和图片集合，并按时间排序
     * @param context
     * @return
     */
    public static List<String> getAllMedia(Context context) {
        List<MediaVO> allMedia = new ArrayList<>();
        allMedia.addAll(getAllMediaPhotos(context));
        allMedia.addAll(getAllMediaVideos(context));
        if (allMedia.isEmpty()) {
            return new ArrayList<>();
        }
        Collections.sort(allMedia, new Comparator<MediaVO>() {
            public int compare(MediaVO arg0, MediaVO arg1) {
                long hits0 = arg0.getModifyTime();
                long hits1 = arg1.getModifyTime();
                if (hits1 > hits0) {
                    return 1;
                } else if (hits1 == hits0) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
        List<String> list = new ArrayList<>();
        for (MediaVO mediaVO: allMedia) {
            list.add(mediaVO.getUrl());
        }
        return list;
    }

    //获取所有图片文件
    public static List<MediaVO> getAllMediaPhotos(Context context){
        List<MediaVO> list = new ArrayList<>();
        final Uri IMAGE_URL = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        final String MIME_TYPE = MediaStore.Images.Media.MIME_TYPE;
        final String DATA = MediaStore.Images.Media.DATA;
        final String DATE_MODIFIED = MediaStore.Images.Media.DATE_MODIFIED;
        ContentResolver contentResolver = context.getContentResolver();
        if (null != contentResolver){
            Cursor cursor = contentResolver.query(IMAGE_URL, new String[]{DATA, DATE_MODIFIED},
                    MIME_TYPE + "=? or " + MIME_TYPE + "=? or " + MIME_TYPE + "=?",
                    new String[]{"image/jpg", "image/jpeg", "image/png"},
                    DATE_MODIFIED + " DESC");
            if (null != cursor){
                while (cursor.moveToNext()){
                    String path = cursor.getString(0);
                    long date = cursor.getLong(1);
                    if (!TextUtils.isEmpty(path)){
                        list.add(new MediaVO(date, path));
                    }
                }
                cursor.close();
            }
        }
        return list;
    }

    public static List<MediaVO> getAllMediaVideos(Context context){
        List<MediaVO> list = new ArrayList<>();
        final Uri VIDEO_URL = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        final String MIME_TYPE = MediaStore.Video.Media.MIME_TYPE;
        final String DATA = MediaStore.Video.Media.DATA;
        final String DATE_MODIFIED = MediaStore.Video.Media.DATE_MODIFIED;
        ContentResolver contentResolver = context.getContentResolver();
        if (null != contentResolver){
            Cursor cursor = contentResolver.query(VIDEO_URL, new String[]{DATA, DATE_MODIFIED},
                    MIME_TYPE + "=? or " +
                            MIME_TYPE + "=? or " +
                            MIME_TYPE + "=? or " +
                            MIME_TYPE + "=? or " +
                            MIME_TYPE + "=? or " +
                            MIME_TYPE + "=?",
                    new String[]{"video/mp4", "video/3gp", "video/aiv", "video/rmvb", "video/flv",
                            "video/mkv"},
                    DATE_MODIFIED + " DESC");
            if (null != cursor){
                while (cursor.moveToNext()){
                    String path = cursor.getString(0);
                    long date = cursor.getLong(1);
                    if (!TextUtils.isEmpty(path)){
                        list.add(new MediaVO(date, path));
                    }
                }
                cursor.close();
            }
        }
        return list;
    }


    /**
     * 获取最近num张照片（jpg, jpeg, png）
     * @param num
     * @return
     */
    public static ArrayList<String> getTheLastPhotos(Context context, int num){
        ArrayList<String> list = null;
        final Uri IMAGE_URL = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        final String MIME_TYPE = MediaStore.Images.Media.MIME_TYPE;
        final String DATA = MediaStore.Images.Media.DATA;
        final String DATE_MODIFIED = MediaStore.Images.Media.DATE_MODIFIED;
        ContentResolver contentResolver = context.getContentResolver();
        if (null != contentResolver){
            Cursor cursor = contentResolver.query(IMAGE_URL, new String[]{DATA},
                    MIME_TYPE + "=? or " + MIME_TYPE + "=? or " + MIME_TYPE + "=?",
                    new String[]{"image/jpg", "image/jpeg", "image/png"},
                    DATE_MODIFIED+" DESC limit 0,"+num);
            if (null != cursor){
                list = new ArrayList<String>();
                while (cursor.moveToNext()){
                    list.add(cursor.getString(0));
                }
                cursor.close();
            }
        }
        return list;
    }

    //获取对应路径下的所有图片
    public static ArrayList<String> getAllPhotosPathByFolder(String path) {
        if (!TextUtils.isEmpty(path)) {
            File folder = new File(path);
            String[] files = folder.list();
            int length;
            if (null == files || (length = files.length) < 1) {
                return null;
            }
            ArrayList<String> imageFilePaths = new ArrayList<String>();
            for (int i = length - 1; i >= 0; i--) {
                if (isPhotoByName(files[i])) {
                    imageFilePaths.add(path + File.separator + files[i]);
                }
            }
            return imageFilePaths;
        }
        return null;
    }

    //获取所有图片文件（按目录）
    public static List<String> getPhotosByPage(Context context, int page, int count){
        List<String> list = new ArrayList<>();
        final Uri IMAGE_URL = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        final String MIME_TYPE = MediaStore.Images.Media.MIME_TYPE;
        final String DATA = MediaStore.Images.Media.DATA;
        final String DATE_MODIFIED = MediaStore.Images.Media.DATE_MODIFIED;
        ContentResolver contentResolver = context.getContentResolver();
        if (null != contentResolver){
            Cursor cursor = contentResolver.query(IMAGE_URL, new String[]{DATA},
                    MIME_TYPE + "=? or " + MIME_TYPE + "=? or " + MIME_TYPE + "=?",
                    new String[]{"image/jpg", "image/jpeg", "image/png"},
                    DATE_MODIFIED + " DESC LIMIT " + (page * count) + " , " + count);
            if (null != cursor){
                while (cursor.moveToNext()){
                    String path = cursor.getString(0);
                    if (!TextUtils.isEmpty(path)){
                        list.add(path);
                    }
                }
                cursor.close();
            }
        }
        return list;
    }


    public static List<String> getVideosByPage(Context context, int page, int count){
        List<String> list = new ArrayList<>();
        final Uri VIDEO_URL = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        final String MIME_TYPE = MediaStore.Video.Media.MIME_TYPE;
        final String DATA = MediaStore.Video.Media.DATA;
        final String DATE_MODIFIED = MediaStore.Video.Media.DATE_MODIFIED;
        ContentResolver contentResolver = context.getContentResolver();
        if (null != contentResolver){
            Cursor cursor = contentResolver.query(VIDEO_URL, new String[]{DATA},
                    MIME_TYPE + "=? or " +
                            MIME_TYPE + "=? or " +
                            MIME_TYPE + "=? or " +
                            MIME_TYPE + "=? or " +
                            MIME_TYPE + "=? or " +
                            MIME_TYPE + "=?",
                    new String[]{"video/mp4", "video/3gp", "video/aiv", "video/rmvb", "video/flv",
                            "video/mkv"},
                    DATE_MODIFIED + " DESC LIMIT " + (page * count) + " , " + count);
            if (null != cursor){
                while (cursor.moveToNext()){
                    String path = cursor.getString(0);
                    if (!TextUtils.isEmpty(path)){
                        list.add(path);
                    }
                }
                cursor.close();
            }
        }
        return list;
    }

    //获取所有图片文件
    public static List<String> getAllPhotos(Context context){
        List<String> list = new ArrayList<>();
        final Uri IMAGE_URL = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        final String MIME_TYPE = MediaStore.Images.Media.MIME_TYPE;
        final String DATA = MediaStore.Images.Media.DATA;
        final String DATE_MODIFIED = MediaStore.Images.Media.DATE_MODIFIED;
        ContentResolver contentResolver = context.getContentResolver();
        if (null != contentResolver){
            Cursor cursor = contentResolver.query(IMAGE_URL, new String[]{DATA},
                    MIME_TYPE + "=? or " + MIME_TYPE + "=? or " + MIME_TYPE + "=?",
                    new String[]{"image/jpg", "image/jpeg", "image/png"},
                    DATE_MODIFIED);
            if (null != cursor){
                while (cursor.moveToNext()){
                    String path = cursor.getString(0);
                    if (!TextUtils.isEmpty(path)){
                        list.add(path);
                    }
                }
                cursor.close();
            }
        }
        return list;
    }

    public static List<PhotoFolderVO> getAllPhotoFolder(Context context){
        List<PhotoFolderVO> list = null;
        final Uri IMAGE_URL = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        final String MIME_TYPE = MediaStore.Images.Media.MIME_TYPE;
        final String DATA = MediaStore.Images.Media.DATA;
        final String DATE_MODIFIED = MediaStore.Images.Media.DATE_MODIFIED;
        ContentResolver contentResolver = context.getContentResolver();
        if (null != contentResolver){
            Cursor cursor = contentResolver.query(IMAGE_URL, new String[]{DATA}, MIME_TYPE + "=? or " + MIME_TYPE + "=? or " + MIME_TYPE + "=?", new String[]{"image/jpg", "image/jpeg", "image/png"}, DATE_MODIFIED);
            if (null != cursor){
                if (cursor.moveToLast()){
                    HashSet<String> fileCache = new HashSet<String>();
                    list = new ArrayList<PhotoFolderVO>();
                    while (true){
                        String path = cursor.getString(0);
                        if (!TextUtils.isEmpty(path)){
                            File parentFile = new File(path).getParentFile();
                            String parentFilePath = parentFile.getAbsolutePath();

                            if (!fileCache.contains(parentFilePath)){
                                list.add(new PhotoFolderVO(parentFilePath, getFolderFirstImagePath(parentFile), parentFile.getName(), getFolderImageCount(parentFile)));
                                fileCache.add(parentFilePath);
                            }
                        }
                        if (!cursor.moveToPrevious()){
                            break;
                        }

                    }
                    cursor.close();
                }
            }
        }
        return list;
    }

    //获取目录下文件数量
    private static int getFolderImageCount(File file){
        int count = 0;
        if (null != file){
            File[] files = file.listFiles();
            if (files!=null&&files.length>0) {
                for (File f : files){
                    if(null != f){
                        String name = f.getName();
                        if(null != name && !name.isEmpty()){
                            String nameStr = name.toLowerCase();
                            if(nameStr.endsWith(".jpg") || nameStr.endsWith(".jpeg") || nameStr.endsWith(".png")){
                                count++;
                            }
                        }
                    }
                }
            }
        }
        return count;
    }

    //获取目录下的第一张图片的路径
    private static String getFolderFirstImagePath(File file){
        if (null != file){
            File[] files = file.listFiles();
            int start;
            if (null != files && (start = files.length) > 0){
                start--;
                for (int i = start; i > -1; i--){
                    File f = files[i];
                    if (isPhotoByFile(f)){
                        return f.getAbsolutePath();
                    }
                }
            }
        }
        return null;
    }

    //判断文件是否是图片
    private static boolean isPhotoByName(String name){
        if (!TextUtils.isEmpty(name)){
            String nameStr = name.toLowerCase();
            return nameStr.endsWith("jpg") || nameStr.endsWith("png") || nameStr.endsWith("jpeg");
        }
        return false;
    }

    private static boolean isPhotoByFile(File file) {
        String name;
        if (null != file && !TextUtils.isEmpty(name = file.getName())){
            String nameStr = name.toLowerCase();
            return nameStr.endsWith("jpg") || nameStr.endsWith("png") || nameStr.endsWith("jpeg");
        }
        return false;
    }
}
