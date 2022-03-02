package cn.yue.base.common.photo.loader;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.core.content.ContentResolverCompat;
import androidx.core.os.CancellationSignal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.yue.base.common.photo.data.MediaType;
import cn.yue.base.common.photo.data.MimeType;

public class MediaFolderLoader {

    public static final String COLUMN_BUCKET_ID = "bucket_id";
    public static final String COLUMN_BUCKET_DISPLAY_NAME = "bucket_display_name";
    public static final String COLUMN_URI = "uri";
    public static final String COLUMN_COUNT = "count";
    public static final String COLUMN_DATA = "_data";
    private static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");

    private static final String[] COLUMNS = {
            MediaStore.Files.FileColumns._ID,
            COLUMN_BUCKET_ID,
            COLUMN_BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE,
            COLUMN_URI,
            COLUMN_COUNT,
            COLUMN_DATA};

    private static final String[] PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            COLUMN_BUCKET_ID,
            COLUMN_BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE,
            "COUNT(*) AS " + COLUMN_COUNT,
            COLUMN_DATA
    };

    private static final String[] PROJECTION_29 = {
            MediaStore.Files.FileColumns._ID,
            COLUMN_BUCKET_ID,
            COLUMN_BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE,
            COLUMN_DATA
    };

    // === params for showSingleMediaType: false ===
    private static final String SELECTION =
            "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " OR "
                    + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0"
                    + ") GROUP BY (bucket_id";
    private static final String SELECTION_29 =
            "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " OR "
                    + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0";
    private static final String[] SELECTION_ARGS = {
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
    };
    // =============================================

    // === params for showSingleMediaType: true ===
    private static final String SELECTION_FOR_SINGLE_MEDIA_TYPE =
            MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0"
                    + ") GROUP BY (bucket_id";
    private static final String SELECTION_FOR_SINGLE_MEDIA_TYPE_29 =
            MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0";

    private static String[] getSelectionArgsForSingleMediaType(int mediaType) {
        return new String[]{String.valueOf(mediaType)};
    }
    // =============================================

    private static String getSelection(MediaType mediaType) {
        if (MediaType.onlyShowImages(mediaType)) {
            return beforeAndroidTen()
                    ? SELECTION_FOR_SINGLE_MEDIA_TYPE : SELECTION_FOR_SINGLE_MEDIA_TYPE_29;
        } else if (MediaType.onlyShowVideos(mediaType)) {
            return beforeAndroidTen()
                    ? SELECTION_FOR_SINGLE_MEDIA_TYPE : SELECTION_FOR_SINGLE_MEDIA_TYPE_29;
        } else {
            return beforeAndroidTen() ? SELECTION : SELECTION_29;
        }
    }

    private static String[] getSelectionArgs(MediaType mediaType) {
        if (MediaType.onlyShowImages(mediaType)) {
            return getSelectionArgsForSingleMediaType(
                    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);
        } else if (MediaType.onlyShowVideos(mediaType)) {
            return getSelectionArgsForSingleMediaType(
                    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO);
        } else {
            return SELECTION_ARGS;
        }
    }
    // =============================================

    private static final String BUCKET_ORDER_BY = "datetaken DESC";
    private static final String ALBUM_ID_ALL = String.valueOf(-1);
    private static final String ALBUM_NAME_ALL = "All";

    public static Cursor load(Context context, MediaType mediaType) {

        Cursor albums = ContentResolverCompat.query(context.getContentResolver(),
                QUERY_URI, beforeAndroidTen() ? PROJECTION : PROJECTION_29, getSelection(mediaType), getSelectionArgs(mediaType), BUCKET_ORDER_BY,
                new CancellationSignal());

        MatrixCursor allAlbum = new MatrixCursor(COLUMNS);

        if (beforeAndroidTen()) {
            int totalCount = 0;
            Uri allAlbumCoverUri = null;
            MatrixCursor otherAlbums = new MatrixCursor(COLUMNS);
            if (albums != null) {
                while (albums.moveToNext()) {
                    long fileId = albums.getLong(
                            albums.getColumnIndex(MediaStore.Files.FileColumns._ID));
                    long bucketId = albums.getLong(
                            albums.getColumnIndex(COLUMN_BUCKET_ID));
                    String bucketDisplayName = albums.getString(
                            albums.getColumnIndex(COLUMN_BUCKET_DISPLAY_NAME));
                    String mimeType = albums.getString(
                            albums.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE));
                    Uri uri = getUri(albums);
                    int count = albums.getInt(albums.getColumnIndex(COLUMN_COUNT));
                    String _data = albums.getString(albums.getColumnIndex(COLUMN_DATA));
                    otherAlbums.addRow(new String[]{
                            Long.toString(fileId),
                            Long.toString(bucketId),
                            bucketDisplayName,
                            mimeType,
                            uri.toString(),
                            String.valueOf(count),
                            _data});
                    totalCount += count;
                }
                if (albums.moveToFirst()) {
                    allAlbumCoverUri = getUri(albums);
                }
            }

            allAlbum.addRow(new String[]{
                    ALBUM_ID_ALL, ALBUM_ID_ALL, ALBUM_NAME_ALL, null,
                    allAlbumCoverUri == null ? null : allAlbumCoverUri.toString(),
                    String.valueOf(totalCount), ""});

            return new MergeCursor(new Cursor[]{allAlbum, otherAlbums});
        } else {
            int totalCount = 0;
            Uri allAlbumCoverUri = null;

            // Pseudo GROUP BY
            Map<Long, Long> countMap = new HashMap<>();
            if (albums != null) {
                while (albums.moveToNext()) {
                    long bucketId = albums.getLong(albums.getColumnIndex(COLUMN_BUCKET_ID));

                    Long count = countMap.get(bucketId);
                    if (count == null) {
                        count = 1L;
                    } else {
                        count++;
                    }
                    countMap.put(bucketId, count);
                }
            }

            MatrixCursor otherAlbums = new MatrixCursor(COLUMNS);
            if (albums != null) {
                if (albums.moveToFirst()) {
                    allAlbumCoverUri = getUri(albums);

                    Set<Long> done = new HashSet<>();

                    do {
                        long bucketId = albums.getLong(albums.getColumnIndex(COLUMN_BUCKET_ID));

                        if (done.contains(bucketId)) {
                            continue;
                        }

                        long fileId = albums.getLong(
                                albums.getColumnIndex(MediaStore.Files.FileColumns._ID));
                        String bucketDisplayName = albums.getString(
                                albums.getColumnIndex(COLUMN_BUCKET_DISPLAY_NAME));
                        String mimeType = albums.getString(
                                albums.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE));
                        Uri uri = getUri(albums);
                        long count = countMap.get(bucketId);
                        String _data = albums.getString(albums.getColumnIndex(COLUMN_DATA));
                        otherAlbums.addRow(new String[]{
                                Long.toString(fileId),
                                Long.toString(bucketId),
                                bucketDisplayName,
                                mimeType,
                                uri.toString(),
                                String.valueOf(count),
                                _data});
                        done.add(bucketId);

                        totalCount += count;
                    } while (albums.moveToNext());
                }
            }

            allAlbum.addRow(new String[]{
                    ALBUM_ID_ALL,
                    ALBUM_ID_ALL,
                    ALBUM_NAME_ALL,
                    null,
                    allAlbumCoverUri == null ? null : allAlbumCoverUri.toString(),
                    String.valueOf(totalCount),
                    ""
            });

            return new MergeCursor(new Cursor[]{allAlbum, otherAlbums});
        }
    }

    private static Uri getUri(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
        String mimeType = cursor.getString(
                cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE));
        Uri contentUri;

        if (MimeType.isImage(mimeType)) {
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if (MimeType.isVideo(mimeType)) {
            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else {
            // ?
            contentUri = MediaStore.Files.getContentUri("external");
        }

        Uri uri = ContentUris.withAppendedId(contentUri, id);
        return uri;
    }

    /**
     * @return 是否是 Android 10 （Q） 之前的版本
     */
    private static boolean beforeAndroidTen() {
        return android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.Q;
    }
}
