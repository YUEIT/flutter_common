package cn.yue.base.common.photo;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.yue.base.common.R;
import cn.yue.base.common.activity.BaseFragment;
import cn.yue.base.common.activity.BaseFragmentActivity;
import cn.yue.base.common.photo.data.MediaType;
import cn.yue.base.common.photo.data.MediaVO;
import cn.yue.base.common.widget.viewpager.PagerSlidingTabStrip;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

@Route(path = "/common/selectPhoto")
public class SelectPhotoActivity extends BaseFragmentActivity {

    private int maxNum = 1;
    private List<MediaVO> photoList = new ArrayList<>();
    private MediaType mediaType = MediaType.ALL;
    private boolean isPreview = false;

    private ViewPager viewPager;
    private PagerSlidingTabStrip tabs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            maxNum = getIntent().getIntExtra("maxNum", 1);
            List<Uri> defaultList = getIntent().getParcelableArrayListExtra("uris");
            if (defaultList != null) {
                for (Uri uri: defaultList) {
                    MediaVO mediaVO = new MediaVO();
                    mediaVO.setUri(uri);
                    photoList.add(mediaVO);
                }
            }
            List<MediaVO> defaultMediaList = getIntent().getParcelableArrayListExtra("medias");
            if (defaultMediaList != null) {
                photoList.addAll(defaultMediaList);
            }
            String type = getIntent().getStringExtra("mediaType");
            if (type == null) {
                type = MediaType.ALL.name();
            }
            mediaType = MediaType.valueOf(type);
            isPreview = getIntent().getBooleanExtra("isPreview", false);
        }
        initTopBar();
        initView();
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_select_photo;
    }

    private void initTopBar() {
        getTopBar().setLeftImage(R.drawable.app_icon_back)
                .setLeftClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .setRightTextStr(photoList.isEmpty()? "取消" : "确定(" + photoList.size() + "/" + maxNum + ")")
                .setRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (photoList.isEmpty()) {
                            finish();
                        } else {
                            finishAllWithResult((ArrayList<MediaVO>) photoList);
                        }
                    }
                });
    }

    private void initView() {
        viewPager = findViewById(R.id.viewPager);
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return getFragment(SelectPhotoFolderFragment.class.getName());
                } else {
                    return getFragment(SelectPhotoFragment.class.getName());
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0) {
                    return "相册选择";
                } else {
                    return "最近照片";
                }
            }
        };
        viewPager.setAdapter(adapter);
        tabs = findViewById(R.id.tabs);
        tabs.setViewPagerAutoRefresh(viewPager, true);
        viewPager.setCurrentItem(1);
    }

    public void changeToSelectPhotoFragment(String folderId, String name) {
        Fragment fragment = getFragment(fragmentNames[1]);
        if (fragment instanceof SelectPhotoFragment) {
            ((SelectPhotoFragment) fragment).refresh(folderId);
        }
        View textView = tabs.getTab(1);
        if (textView instanceof TextView) {
            ((TextView) textView).setText(name);
        }
        viewPager.setCurrentItem(1);
    }

    private String fragmentNames[] = new String[]{ SelectPhotoFolderFragment.class.getName(), SelectPhotoFragment.class.getName()};
    private Map<String, BaseFragment> fragments = new HashMap();

    private BaseFragment getFragment(String fragmentName) {
        BaseFragment fragment = fragments.get(fragmentName);
        if (fragment == null) {
            Bundle bundle = new Bundle();
            fragment = (BaseFragment) Fragment.instantiate(this, fragmentName, bundle);
            fragments.put(fragmentName, fragment);
        }
        return fragment;
    }

    @Override
    public Fragment getFragment() {
        return null;
    }

    public List<MediaVO> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<MediaVO> photoList) {
        this.photoList = photoList;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public boolean getIsPreview() {
        return isPreview;
    }

    private void finishAllWithResult(ArrayList<MediaVO> selectList) {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("medias", selectList);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void changeTopBar(String title) {
        if (getCurrentFragment() instanceof SelectPhotoFolderFragment) {
            getTopBar().setCenterTextStr(title)
                    .setCenterImage(0)
                    .setCenterClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setLeftImage(R.drawable.app_icon_back)
                    .setLeftTextStr("")
                    .setLeftClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    })
                    .setRightTextStr("取消")
                    .setRightClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
        } else if (getCurrentFragment() instanceof SelectPhotoFragment) {
            getTopBar().setCenterImage(R.drawable.app_icon_search)
                    .setCenterClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeFragment(SelectPhotoFolderFragment.class.getName(), "相册选择");
                        }
                    })
                    .setLeftImage(R.drawable.app_icon_back)
                    .setLeftClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    })
                    .setRightTextStr(photoList.isEmpty()? "取消" : "确定（" + photoList.size() + "/" + maxNum + "）")
                    .setRightClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (photoList.isEmpty()) {
                                finish();
                            } else {
                                finishAllWithResult((ArrayList<MediaVO>) photoList);
                            }
                        }
                    });
        }
    }

    private void changeFragment(String fragmentName, String title) {
        BaseFragment showFragment = getFragment(fragmentName);
        if (showFragment != getCurrentFragment()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            setAnimation(transaction);
            if (null == getCurrentFragment()) {
                transaction.add(R.id.content, showFragment, showFragment.getClass().getName())
                        .commitAllowingStateLoss();
            } else {
                if (showFragment.isAdded()) {
                    transaction.show(showFragment).hide(getCurrentFragment())
                            .commitAllowingStateLoss();
                } else {
                    transaction.add(R.id.content, showFragment, showFragment.getClass().getName())
                            .hide(getCurrentFragment()).commitAllowingStateLoss();
                }
            }
            setCurrentFragment(showFragment);
        }
        changeTopBar(title);
    }

    private void setAnimation(FragmentTransaction transaction) {
        if (getCurrentFragment() instanceof SelectPhotoFragment) {
            transaction.setCustomAnimations(R.anim.left_in, R.anim.right_out);
        } else {
            transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
        }
    }

    @Override
    public void onBackPressed() {
//        if (getCurrentFragment() instanceof SelectPhotoFragment) {
//            changeFragment(SelectPhotoFolderFragment.class.getName(), "相册选择");
//        } else if (getCurrentFragment() instanceof SelectPhotoFolderFragment) {
//            finish();
//        }
        super.onBackPressed();
    }
}
