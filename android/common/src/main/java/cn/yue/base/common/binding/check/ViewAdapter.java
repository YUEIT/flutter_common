package cn.yue.base.common.binding.check;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import androidx.databinding.BindingAdapter;

import cn.yue.base.common.binding.action.Consumer2;

public class ViewAdapter {

    @BindingAdapter({"onCheckedChangeListener"})
    public static void setOnCheckedChangeListener(CheckBox checkBox, Consumer2<CompoundButton, Boolean> onCheckedChangeListener) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onCheckedChangeListener.accept(buttonView, isChecked);
            }
        });
    }

    @BindingAdapter({"onCheckedChangeListener"})
    public static void setOnCheckedChangeListener(RadioGroup radioGroup, Consumer2<RadioGroup, Integer> onCheckedChangeListener) {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onCheckedChangeListener.accept(group, checkedId);
            }
        });
    }
}
