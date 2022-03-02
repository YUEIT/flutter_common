package cn.yue.base.common.photo.loader;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.core.content.ContentResolverCompat;
import androidx.core.os.CancellationSignal;

import cn.yue.base.common.photo.data.MediaType;

public class MediaLoader {

    private static final String COLUMN_DATA = "_data";
    private static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");
    private static final String[] PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.SIZE,
            "duration",
            MediaStore.MediaColumns.WIDTH,
            MediaStore.MediaColumns.HEIGHT,
            "orientation",
            COLUMN_DATA
    };

    // === params for album ALL && showSingleMediaType: false ===
    private static final String SELECTION_ALL =
            "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " OR "
                    + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0";
    private static final String[] SELECTION_ALL_ARGS = {
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
            String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
    };
    // ===========================================================

    // === params for album ALL && showSingleMediaType: true ===
    private static final String SELECTION_ALL_FOR_SINGLE_MEDIA_TYPE =
            MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0";

    private static String[] getSelectionArgsForSingleMediaType(int mediaType) {
        return new String[]{String.valueOf(mediaType)};
    }
    // =========================================================

    // === params for ordinary album && showSingleMediaType: false ===
    private static final String SELECTION_ALBUM =
            "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " OR "
                    + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
                    + " AND "
                    + " bucket_id=?"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0";

    private static String[] getSelectionAlbumArgs(String albumId) {
        return new String[]{
                String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
                String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
                albumId
        };
    }
    // ===============================================================

    // === params for ordinary album && showSingleMediaType: true ===
    private static final String SELECTION_ALBUM_FOR_SINGLE_MEDIA_TYPE =
            MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " AND "
                    + " bucket_id=?"
                    + " AND " + MediaStore.MediaColumns.SIZE + ">0";

    private static String[] getSelectionAlbumArgsForSingleMediaType(int mediaType, String albumId) {
        return new String[]{String.valueOf(mediaType), albumId};
    }
    // ===============================================================

    private static String getSelection(boolean isAll, MediaType mediaType) {
        if (isAll) {
            if (MediaType.onlyShowImages(mediaType)) {
                return SELECTION_ALL_FOR_SINGLE_MEDIA_TYPE;
            } else if (MediaType.onlyShowVideos(mediaType)) {
                return SELECTION_ALL_FOR_SINGLE_MEDIA_TYPE;
            } else {
                return SELECTION_ALL;
            }
        } else {
            if (MediaType.onlyShowImages(mediaType)) {
                return SELECTION_ALBUM_FOR_SINGLE_MEDIA_TYPE;
            } else if (MediaType.onlyShowVideos(mediaType)) {
                return SELECTION_ALBUM_FOR_SINGLE_MEDIA_TYPE;
            } else {
                return SELECTION_ALBUM;
            }
        }
    }

    private static String[] getSelectionArgs(boolean isAll, String folderId, MediaType mediaType) {
        if (isAll) {
            if (MediaType.onlyShowImages(mediaType)) {
                return getSelectionArgsForSingleMediaType(
                                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);
            } else if (MediaType.onlyShowVideos(mediaType)) {
                return getSelectionArgsForSingleMediaType(
                                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO);
            } else {
                return SELECTION_ALL_ARGS;
            }
        } else {
            if (MediaType.onlyShowImages(mediaType)) {
                return getSelectionAlbumArgsForSingleMediaType(
                                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE,
                        folderId);
            } else if (MediaType.onlyShowVideos(mediaType)) {
                return getSelectionAlbumArgsForSingleMediaType(
                        MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO,
                        folderId);
            } else {
                return getSelectionAlbumArgs(folderId);
            }
        }
    }
    // ===============================================================

    private static final String ORDER_BY = MediaStore.Images.Media.DATE_TAKEN + " DESC";

    public static final long ITEM_ID_CAPTURE = -1;
    public static final String ITEM_DISPLAY_NAME_CAPTURE = "Capture";


    public static Cursor load(Context context, boolean isAll, String folderId, MediaType mediaType) {

        Cursor result = ContentResolverCompat.query(context.getContentResolver(),
                QUERY_URI, PROJECTION, getSelection(isAll, mediaType), getSelectionArgs(isAll, folderId, mediaType), ORDER_BY,
                new CancellationSignal());
        MatrixCursor dummy = new MatrixCursor(PROJECTION);
//        dummy.addRow(new Object[]{ITEM_ID_CAPTURE, ITEM_DISPLAY_NAME_CAPTURE, "", 0, 0});
        return new MergeCursor(new Cursor[]{dummy, result});
    }

}

