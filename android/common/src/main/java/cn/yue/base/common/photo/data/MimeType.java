package cn.yue.base.common.photo.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import androidx.collection.ArraySet;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import cn.yue.base.common.utils.file.AndroidQFileUtils;

/**
 * MIME Type enumeration to restrict selectable media on the selection activity. Matisse only supports images and
 * videos.
 * <p>
 * Good example of mime types Android supports:
 * https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/media/java/android/media/MediaFile.java
 */
@SuppressWarnings("unused")
public enum MimeType {

    // ============== images ==============
    JPEG("image/jpeg", arraySetOf(
            "jpg",
            "jpeg"
    )),
    PNG("image/png", arraySetOf(
            "png"
    )),
    GIF("image/gif", arraySetOf(
            "gif"
    )),
    BMP("image/x-ms-bmp", arraySetOf(
            "bmp"
    )),
    WEBP("image/webp", arraySetOf(
            "webp"
    )),

    // ============== videos ==============
    MPEG("video/mpeg", arraySetOf(
            "mpeg",
            "mpg"
    )),
    MP4("video/mp4", arraySetOf(
            "mp4",
            "m4v"
    )),
    QUICKTIME("video/quicktime", arraySetOf(
            "mov"
    )),
    THREEGPP("video/3gpp", arraySetOf(
            "3gp",
            "3gpp"
    )),
    THREEGPP2("video/3gpp2", arraySetOf(
            "3g2",
            "3gpp2"
    )),
    MKV("video/x-matroska", arraySetOf(
            "mkv"
    )),
    WEBM("video/webm", arraySetOf(
            "webm"
    )),
    TS("video/mp2ts", arraySetOf(
            "ts"
    )),
    AVI("video/avi", arraySetOf(
            "avi"
    )),
    APK("application/vnd.android.package-archive", arraySetOf(
            "apk"
    )),
    TEXT("text/plain", arraySetOf(
            "txt",
            "log"
    )),
    ZIP("application/x-zip-compressed", arraySetOf(
            "zip"
    ));

    private final String mMimeTypeName;
    private final Set<String> mExtensions;

    MimeType(String mimeTypeName, Set<String> extensions) {
        mMimeTypeName = mimeTypeName;
        mExtensions = extensions;
    }

    public static Set<MimeType> ofAll() {
        return EnumSet.allOf(MimeType.class);
    }

    public static Set<MimeType> of(MimeType type, MimeType... rest) {
        return EnumSet.of(type, rest);
    }

    public static Set<MimeType> ofImage() {
        return EnumSet.of(JPEG, PNG, GIF, BMP, WEBP);
    }

    public static Set<MimeType> ofImage(boolean onlyGif) {
        return EnumSet.of(GIF);
    }

    public static Set<MimeType> ofGif() {
        return ofImage(true);
    }

    public static Set<MimeType> ofVideo() {
        return EnumSet.of(MPEG, MP4, QUICKTIME, THREEGPP, THREEGPP2, MKV, WEBM, TS, AVI);
    }

    public static boolean isImage(String mimeType) {
        if (mimeType == null) return false;
        return mimeType.startsWith("image");
    }

    public static boolean isVideo(String mimeType) {
        if (mimeType == null) return false;
        return mimeType.startsWith("video");
    }

    public static boolean isGif(String mimeType) {
        if (mimeType == null) return false;
        return mimeType.equals(MimeType.GIF.toString());
    }

    public static boolean isAudio(String mimeType) {
        return mimeType.startsWith("audio");
    }

    public static boolean isFile(String mimeType) {
        return mimeType.startsWith("text");
    }

    private static Set<String> arraySetOf(String... suffixes) {
        return new ArraySet<>(Arrays.asList(suffixes));
    }

    @Override
    public String toString() {
        return mMimeTypeName;
    }

    public String getMimeTypeName() {
        return mMimeTypeName;
    }

    public static String getMimeType(String fileName) {
        if (fileName == null) {
            return "";
        }
        String suffix;
        String[] str = fileName.split(".");
        if (str.length > 1) {
            suffix = str[str.length-1];
        } else {
            return "";
        }
        for (Iterator<MimeType> iterator = ofAll ().iterator(); iterator.hasNext();) {
            MimeType mimeType = iterator.next();
            if (mimeType.getExtensions().contains(suffix)) {
                return mimeType.mMimeTypeName;
            }
        }
        return "";
    }

    public Set<String> getExtensions() {
        return mExtensions;
    }

    public boolean checkType(ContentResolver resolver, Uri uri) {
        MimeTypeMap map = MimeTypeMap.getSingleton();
        if (uri == null) {
            return false;
        }
        String type = map.getExtensionFromMimeType(resolver.getType(uri));
        String path = null;
        // lazy load the path and prevent resolve for multiple times
        boolean pathParsed = false;
        for (String extension : mExtensions) {
            if (extension.equals(type)) {
                return true;
            }
            if (!pathParsed) {
                // we only resolve the path for one time
                path = AndroidQFileUtils.getPath(uri);
                if (!TextUtils.isEmpty(path)) {
                    path = path.toLowerCase(Locale.US);
                }
                pathParsed = true;
            }
            if (path != null && path.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isServerImage(String url) {
        return url.startsWith("http");
    }
}
