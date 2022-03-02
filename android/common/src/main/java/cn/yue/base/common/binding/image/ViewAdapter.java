package cn.yue.base.common.binding.image;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import cn.yue.base.common.image.ImageLoader;

public class ViewAdapter {

    @BindingAdapter(value = {"url", "imageRes"} , requireAll = false)
    public static void setImageResource(ImageView imageView, String url, int imageRes) {
        if (url != null && !url.isEmpty()) {
            ImageLoader.getLoader().loadImage(imageView, url);
            return;
        }
        if (imageRes != 0) {
            ImageLoader.getLoader().loadImage(imageView, imageRes);
        }
    }

}
