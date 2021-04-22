package cn.yue.base.middle.activity;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

public class CommonTransparentActivity extends CommonActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        removeTopBar();
        setContentBackground(Color.TRANSPARENT);
    }
}