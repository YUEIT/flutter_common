package cn.yue.base.common.utils.file;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.WorkerThread;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Objects;

import cn.yue.base.common.photo.data.MimeType;
import cn.yue.base.common.utils.Utils;

/**
 * Description : android Q 沙盒; 外部存储只能通过MediaStore 或者 SAF两种方式操作
 * Created by yue on 2020/5/21
 */
public class AndroidQFileUtils {

    //对于MediaStore下Images、Video、Audio 只能操作在DCIM目录; File只能在Documents、Download;
    private static final String IMAGE_PATH = "DCIM/image/";
    private static final String VIDEO_PATH = "DCIM/video/";
    private static final String AUDIO_PATH = "DCIM/audio/";
    private static final String FILE_PATH = "Documents/file/";

    /**
     * 判断文件是否存在
     *
     * @param uri
     * @return
     */
    public static boolean fileUriIsExists(Uri uri) {
        if (uri == null) {
            return false;
        }
        try {
            ParcelFileDescriptor fileDescriptor =
                    Utils.getApp().getContentResolver().openFileDescriptor(uri, "r");
            if (fileDescriptor != null) {
                return fileDescriptor.getFileDescriptor().valid();
            }
        } catch (SecurityException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @WorkerThread
    public static Bitmap getBitmapFromUri(Uri uri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    Utils.getApp().getContentResolver().openFileDescriptor(uri, "r");
            if (parcelFileDescriptor != null) {
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                parcelFileDescriptor.close();
                return image;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTextFromUri(Uri uri) throws IOException {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            InputStream inputStream =
                    Utils.getApp().getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(Objects.requireNonNull(inputStream)));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Uri saveBitmap(Bitmap bitmap, String fileName) {
        return saveBitmap(bitmap, IMAGE_PATH, fileName);
    }

    /**
     * 保存bitmap
     *
     * @param relativePath
     * @param bitmap
     * @param fileName
     * @return
     */
    public static Uri saveBitmap(Bitmap bitmap, String relativePath, String fileName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, relativePath);
        Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = Utils.getApp().getContentResolver();
        Uri resultUri = contentResolver.insert(contentUri, contentValues);
        try {
            if (resultUri != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, contentResolver.openOutputStream(resultUri));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return resultUri;
    }

    public static Uri saveFile(InputStream inputStream, String fileName, String mimeType) {
        return saveFile(inputStream, FILE_PATH, fileName, mimeType);
    }

    /**
     * 保存文件
     *
     * @param relativePath
     * @param inputStream
     * @param fileName
     * @return
     */
    public static Uri saveFile(InputStream inputStream, String relativePath, String fileName, String mimeType) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.Video.Media.MIME_TYPE, mimeType);
        contentValues.put(MediaStore.Files.FileColumns.RELATIVE_PATH, relativePath);
        Uri contentUri = MediaStore.Files.getContentUri("external");
        ContentResolver contentResolver = Utils.getApp().getContentResolver();
        Uri resultUri = contentResolver.insert(contentUri, contentValues);
        try {
            if (resultUri != null) {
                int read = -1;
                byte[] buffer = new byte[2048];
                OutputStream outputStream = contentResolver.openOutputStream(resultUri);
                if (outputStream != null) {
                    while ((read = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, read);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultUri;
    }

    public static Uri saveVideo(InputStream inputStream, String fileName, String mimeType) {
        return saveVideo(inputStream, VIDEO_PATH, fileName, mimeType);
    }

    /**
     * 保存视频
     *
     * @param inputStream
     * @param fileName
     * @param mimeType
     * @return
     */
    public static Uri saveVideo(InputStream inputStream, String relativePath, String fileName, String mimeType) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Video.Media.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.Video.Media.MIME_TYPE, mimeType);
        contentValues.put(MediaStore.Video.Media.RELATIVE_PATH, relativePath);
        Uri contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = Utils.getApp().getContentResolver();
        Uri resultUri = contentResolver.insert(contentUri, contentValues);
        try {
            if (resultUri != null) {
                int read = -1;
                byte[] buffer = new byte[2048];
                OutputStream outputStream = contentResolver.openOutputStream(resultUri);
                if (outputStream != null) {
                    while ((read = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, read);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultUri;
    }

    public static Uri saveAudio(InputStream inputStream, String fileName, String mimeType) {
        return saveVideo(inputStream, AUDIO_PATH, fileName, mimeType);
    }

    /**
     * 保存音频
     *
     * @param relativePath
     * @param inputStream
     * @param fileName
     * @param mimeType
     * @return
     */
    public static Uri saveAudio(InputStream inputStream, String relativePath, String fileName, String mimeType) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Audio.Media.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.Audio.Media.MIME_TYPE, mimeType);
        contentValues.put(MediaStore.Audio.Media.RELATIVE_PATH, relativePath);
        Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = Utils.getApp().getContentResolver();
        Uri resultUri = contentResolver.insert(contentUri, contentValues);
        try {
            if (resultUri != null) {
                int read = -1;
                byte[] buffer = new byte[2048];
                OutputStream outputStream = contentResolver.openOutputStream(resultUri);
                if (outputStream != null) {
                    while ((read = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, read);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultUri;
    }


    public static Uri saveImage(InputStream inputStream, String fileName, String mimeType) {
        return saveImage(inputStream, IMAGE_PATH, fileName, mimeType);
    }

    /**
     * 保存图片
     *
     * @param relativePath
     * @param inputStream
     * @param fileName
     * @param mimeType
     * @return
     */
    public static Uri saveImage(InputStream inputStream, String relativePath, String fileName, String mimeType) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, relativePath);
        Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = Utils.getApp().getContentResolver();
        Uri resultUri = contentResolver.insert(contentUri, contentValues);
        try {
            if (resultUri != null) {
                int read = -1;
                byte[] buffer = new byte[2048];
                OutputStream outputStream = contentResolver.openOutputStream(resultUri);
                if (outputStream != null) {
                    while ((read = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, read);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultUri;
    }

    /**
     * 获取uri的文件路径 （DATA 已经被Deprecated，慎用！）
     *
     * @param uri
     * @return
     */
    public static String getPath(Uri uri) {
        if (uri == null) {
            return null;
        }
        if ("content".equals(uri.getScheme())) {
            Cursor cursor = null;
            try {
                ContentResolver contentResolver = Utils.getApp().getContentResolver();
                cursor = contentResolver.query(uri, new String[]{"_data"},
                        null, null, null);
                if (cursor == null || !cursor.moveToFirst()) {
                    return null;
                }
                return cursor.getString(cursor.getColumnIndex("_data"));
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return uri.getPath();
    }

    public static Uri getMediaUriFromName(String fileName, String mimeType) {
        return getMediaUriFromName("", fileName, mimeType);
    }

    /**
     * 获取Uri
     *
     * @param relativePath
     * @param fileName
     * @param mimeType
     * @return
     */
    public static Uri getMediaUriFromName(String relativePath, String fileName, String mimeType) {
        final Uri mediaUri;
        String selection;
        final String mediaId;
        String[] selectionArgs;
        boolean findInPath = !TextUtils.isEmpty(relativePath);
        if (MimeType.isImage(mimeType)) {
            mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            selection = MediaStore.Images.Media.DISPLAY_NAME + "=?";
            if (findInPath) {
                selection = selection + " AND " + MediaStore.Images.Media.RELATIVE_PATH + "=?";
            }
            mediaId = MediaStore.Images.Media._ID;
        } else if (MimeType.isVideo(mimeType)) {
            mediaUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            selection = MediaStore.Video.Media.DISPLAY_NAME + "=?";
            if (findInPath) {
                selection = selection + " AND " + MediaStore.Video.Media.RELATIVE_PATH + "=?";
            }
            mediaId = MediaStore.Video.Media._ID;
        } else if (MimeType.isAudio(mimeType)) {
            mediaUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            selection = MediaStore.Audio.Media.DISPLAY_NAME + "=?";
            if (findInPath) {
                selection = selection + " AND " + MediaStore.Video.Media.RELATIVE_PATH + "=?";
            }
            mediaId = MediaStore.Audio.Media._ID;
        } else if (MimeType.isFile(mimeType)) {
            mediaUri = MediaStore.Files.getContentUri("external");
            selection = MediaStore.Files.FileColumns.DISPLAY_NAME + "=?";
            if (findInPath) {
                selection = selection + " AND " + MediaStore.Video.Media.RELATIVE_PATH + "=?";
            }
            mediaId = MediaStore.Files.FileColumns._ID;
        } else {
            return null;
        }
        selectionArgs = new String[]{fileName};
        if (findInPath) {
            selectionArgs = new String[]{fileName, relativePath};
        }
        Cursor cursor = Utils.getApp().getContentResolver().query(mediaUri,
                null,
                selection,
                selectionArgs,
                null);

        Uri uri = null;
        if (cursor != null && cursor.moveToFirst()) {
            uri = ContentUris.withAppendedId(mediaUri,
                    cursor.getLong(cursor.getColumnIndex(mediaId)));
            cursor.close();
        }
        return uri;
    }
}
