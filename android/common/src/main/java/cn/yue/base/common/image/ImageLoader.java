package cn.yue.base.common.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;
import android.widget.ImageView;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

public class ImageLoader {

    private static Loader loader;

    public static Loader getLoader(){
        if(loader == null){
            loader = new GlideImageLoader();
        }
        loader.clearCache();
        return loader;
    }

    public interface Loader{

        void loadImage(ImageView imageView, String url);

        void loadImage(ImageView imageView, String url, boolean fitCenter);

        void loadImage(ImageView imageView, @DrawableRes int resId);

        void loadImage(ImageView imageView, @DrawableRes int resId, boolean fitCenter);

        void loadImage(ImageView imageView, Drawable drawable);

        void loadImage(ImageView imageView, Drawable drawable, boolean fitCenter);

        void loadGif(ImageView imageView, String url);

        void loadGif(ImageView imageView, @DrawableRes int resId);

        void loadRoundImage(ImageView imageView, String url, int radius);

        void loadCircleImage(ImageView imageView, String url);

        void loadAsBitmap(Context context, String url, LoadBitmapCallBack callBack);

        Loader setPlaceholder(@DrawableRes int resId);

        void loadImageNoCache(ImageView imageView, String url);

        void clearCache();
    }
}