package cn.yue.base.common.photo.perview;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.yue.base.common.R;
import cn.yue.base.common.activity.BaseActivity;
import cn.yue.base.common.activity.BaseFragmentActivity;
import cn.yue.base.common.photo.data.MediaType;
import cn.yue.base.common.utils.app.FragmentUtils;
import cn.yue.base.common.video.ViewVideoFragment;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

public class ViewMediaActivity extends BaseFragmentActivity{

    @Override
    public Fragment getFragment() {
        ArrayList<Uri> photoUriList = getIntent().getParcelableArrayListExtra("uris");
        int currentIndex = getIntent().getIntExtra("position", 0);
        String mediaTypeStr = getIntent().getStringExtra("mediaType");
        if (TextUtils.isEmpty(mediaTypeStr)) {
            mediaTypeStr = MediaType.PHOTO.name();
        }
        MediaType mediaType = MediaType.valueOf(mediaTypeStr);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("uris", photoUriList);
        bundle.putInt("position", currentIndex);
        if (mediaType == MediaType.VIDEO) {
            return FragmentUtils.instantiate(this, ViewVideoFragment.class.getName(), bundle);
        } else {
            return FragmentUtils.instantiate(this, ViewPhotoFragment.class.getName(), bundle);
        }
    }
}
