package cn.yue.base.common.utils.file;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.UUID;

import cn.yue.base.common.Constant;
import cn.yue.base.common.utils.debug.ToastUtils;
import cn.yue.base.common.utils.device.ScreenUtils;

/**
 * Description :
 * Created by yue on 2019/3/11
 */
public class BitmapFileUtil {


    //private static Context application;

    public static void init(Context application) {
        File file = new File(Constant.CACHE_PATH);
        if (file != null && !file.exists()) {
            file.mkdirs();
        }

        file = new File(Constant.IMAGE_PATH);
        if (file != null && !file.exists()) {
            file.mkdirs();
        }
    }


    public static void clearCache() {
        File file = new File(Constant.CACHE_PATH);
        if (file.exists()) {
            file.deleteOnExit();
        }
        file = new File(Constant.IMAGE_PATH);
        if (file.exists()) {
            file.deleteOnExit();
        }
    }


    public static String getPhotoCameraPath() {
        return Constant.IMAGE_PATH;
    }

    public static String getCacheFilePath() {
        return Constant.CACHE_PATH;
    }


    private static String getSaveBitmapPath() {
        StringBuilder sb = new StringBuilder();
        sb.append(Constant.IMAGE_PATH);
        sb.append(UUID.randomUUID().toString());
        sb.append(".jpg");
        return sb.toString();
    }


    /**
     * 描述：SD卡是否能用.
     *
     * @return true 可用,false不可用
     */
    public static boolean isCanUseSD() {
        try {
            return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static Bitmap getBitmapFormLoaclPath(String url) {
        try {
            FileInputStream fileInputStream = new FileInputStream(url);
            return getBitmapFormInputStream(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Bitmap getBitmapFormNetUrl(String netUrl) {
        try {
            URL url = new URL(netUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");   //设置请求方法为GET
            conn.setReadTimeout(5 * 1000);    //设置请求过时时间为5秒
            InputStream inputStream = conn.getInputStream();   //通过输入流获得图片数据
            return getBitmapFormInputStream(inputStream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Bitmap getBitmapFormInputStream(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        try {
            byte[] data = readStream(inputStream);
            if (data != null) {
                //return BitmapFactory.decodeByteArray(data, 0, data.length);
                return decodeSampledBitmapFromDataArray(data, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }


    private static Bitmap getBitmapFromUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (url.startsWith("http")) {
            return getBitmapFormNetUrl(url);
        }
        return getBitmapFormLoaclPath(url);
    }

    /**
     * 将图片压缩后存入文件
     *
     * @param image
     * @param fos
     * @throws Exception
     */
    private static void compressImage(Bitmap image, FileOutputStream fos) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int optionsSize = 10;
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= optionsSize;//每次都减少10
            if (options == 10) {
                optionsSize = 5;
            }
            if (options == 5) {
                optionsSize = 1;
            }
            if (options == 0) {
                break;
            }
        }
        image.recycle();
        fos.write(baos.toByteArray());
        fos.flush();
    }

    private static boolean saveCompressImage(Bitmap bitmap, String name) {
        FileOutputStream fos = null;
        try {
            File file = new File(name);
            if (!isCanUseSD()) {
                return false;
            }
            // 文件目录是否存在
            File fileDir = new File(getPhotoCameraPath());
            if (fileDir != null && !fileDir.exists()) {
                fileDir.mkdirs();
            }
            File fileDir1 = new File(Constant.IMAGE_PATH);
            if (fileDir1 != null && !fileDir1.exists()) {
                fileDir1.mkdirs();
            }
            //文件是否存在
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            compressImage(bitmap, fos);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static boolean saveCompressImage(Bitmap bitmap) {
        String saveFile = getSaveBitmapPath();
        return saveCompressImage(bitmap, saveFile);
    }


    public static File getCompressBitmapFile(String url) {
        Bitmap bitmap = getBitmapFromUrl(url);
        if (getBitmapDegree(url) != 0) {
            bitmap = rotateBitmapByDegree(bitmap, getBitmapDegree(url));
        }
        String saveFile = getSaveBitmapPath();
        saveCompressImage(bitmap, saveFile);
        return new File(saveFile);
    }

    public static File getCompressBitmapFile(Bitmap bitmap) {
        String saveFile = getSaveBitmapPath();
        saveCompressImage(bitmap, saveFile);
        return new File(saveFile);
    }


    public static void getBitmapHeightAndWith(String url, int size[]) {
        Bitmap bitmap = getBitmapFromUrl(url);
        if (bitmap != null) {
            size[0] = bitmap.getWidth();
            size[1] = bitmap.getHeight();
            bitmap.recycle();
            bitmap = null;
        } else {
            size[0] = 0;
            size[1] = 0;
        }
    }

    public static void getLocalHeightAndWith(String url, int size[]) {
        if (TextUtils.isEmpty(url) || url.startsWith("http") || url.startsWith("https")) {
            size[0] = 0;
            size[1] = 0;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(url, options);
        if (options != null) {
            size[0] = options.outWidth;
            size[1] = options.outHeight;
            bitmap = null;
        } else {
            size[0] = 0;
            size[1] = 0;
        }
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */

    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.
                    getAttributeInt(ExifInterface.
                                    TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm,
                                              int degree) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm,
                    0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * webview上传图片使用,压缩图片返回uri
     *
     * @param path
     * @return
     */
    public static Uri getCompressBitmapUri(String path) {
        return Uri.fromFile(getCompressBitmapFile(path));
    }


    /*根据传入的宽和高，计算出合适的inSampleSize值：*/
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    //使用这个方法，首先你要将BitmapFactory.Options的inJustDecodeBounds属性设置为true，解析一次图片。然后将BitmapFactory.Options连同期望的宽度和高度一起传递到到calculateInSampleSize方法中，就可以得到合适的inSampleSize值了。之后再解析一次图片，使用新获取到的inSampleSize值，并把inJustDecodeBounds设置为false，就可以得到压缩后的图片了。
    public static Bitmap decodeSampledBitmapFromDataArray(byte[] data,
                                                          int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //BitmapFactory.decodeResource(res, resId, options);
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);

    }

    public static File createRandomFile() {
        String storePath = Constant.CACHE_PATH;
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String uuid = UUID.randomUUID().toString();
        File tempFile = new File(Constant.CACHE_PATH, uuid + ".jpg");
        return tempFile;
    }

    //保存文件到指定路径
    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        String storePath = Constant.IMAGE_PATH;
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                ToastUtils.showShortToast("图片已保存到" + storePath);
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //保存文件到指定路径
    public static String saveBitmapToFile(Context context, Bitmap bmp) {
        // 首先保存图片
        String storePath = Constant.IMAGE_PATH;
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return file.getPath();
            } else {
                return file.getPath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}


