package cn.yue.base.common.binding.linear;


import android.widget.LinearLayout;

import androidx.databinding.BindingAdapter;

import cn.yue.base.common.widget.linear.LinearFillingHelper;

public class ViewAdapter {

    @BindingAdapter(value = {"adapter"})
    public static void setAdapter(LinearLayout linearLayout, LinearFillingHelper.Adapter adapter) {
        new LinearFillingHelper(linearLayout).setAdapter(adapter);
    }

}
