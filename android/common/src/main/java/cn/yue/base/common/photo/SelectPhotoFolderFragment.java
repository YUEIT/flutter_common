package cn.yue.base.common.photo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.yue.base.common.R;
import cn.yue.base.common.activity.BaseFragment;
import cn.yue.base.common.image.ImageLoader;
import cn.yue.base.common.utils.code.ThreadPoolUtils;
import cn.yue.base.common.widget.TopBar;
import cn.yue.base.common.widget.recyclerview.CommonAdapter;
import cn.yue.base.common.widget.recyclerview.CommonViewHolder;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

public class SelectPhotoFolderFragment extends BaseFragment {

    private CommonAdapter commonAdapter;

    @Override
    protected void initTopBar(TopBar topBar) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_photo;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        RecyclerView rv = findViewById(R.id.photoRV);
        rv.setLayoutManager(new LinearLayoutManager(mActivity));
        rv.setAdapter(commonAdapter = new CommonAdapter<PhotoFolderVO>(mActivity, allFolder) {
            @Override
            public int getLayoutIdByType(int viewType) {
                return R.layout.item_select_photo_folder;
            }

            @Override
            public void bindData(CommonViewHolder holder, int position, PhotoFolderVO photoFolderVO) {
                holder.setText(R.id.folderTV,photoFolderVO.getName() + "（" + photoFolderVO.getCount() +"）");
                ImageLoader.getLoader().loadImage((ImageView) holder.getView(R.id.folderIV), photoFolderVO.getCover());
                holder.setOnItemClickListener(position, photoFolderVO, new CommonViewHolder.OnItemClickListener<PhotoFolderVO>() {
                    @Override
                    public void onItemClick(int position, PhotoFolderVO photoFolderVO1) {
                        ((SelectPhotoActivity)mActivity).changeToSelectPhotoFragment(photoFolderVO1.getPath(), photoFolderVO1.getName());
                    }
                });
            }
        });
        getAllPhotoFolder();
    }

    private List<PhotoFolderVO> allFolder = new ArrayList<>();

    private void getAllPhotoFolder() {
        ThreadPoolUtils threadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.Type.SingleThread, 1);
        threadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                List<PhotoFolderVO> allFolder = PhotoUtils.getAllPhotoFolder(mActivity);
                List<String> lastPhotos = PhotoUtils.getTheLastPhotos(mActivity, 100);
                if (lastPhotos != null && !lastPhotos.isEmpty()){
                    PhotoFolderVO lastPhotoFolderVO = new PhotoFolderVO("", lastPhotos.get(0), "最近照片", lastPhotos.size());
                    allFolder.add(0, lastPhotoFolderVO);
                }
                handler.sendMessage(Message.obtain(handler, 101, allFolder));
            }
        });
    }

    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 101) {
                allFolder.clear();
                if ((List<PhotoFolderVO>) msg.obj != null) {
                    allFolder.addAll((List<PhotoFolderVO>) msg.obj);
                }
                if (commonAdapter != null) {
                    commonAdapter.setList(allFolder);
                }
            }
            return false;
        }
    });

}
