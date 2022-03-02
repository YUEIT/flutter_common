package cn.yue.base.common.photo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import cn.yue.base.common.R;
import cn.yue.base.common.activity.BaseFragment;
import cn.yue.base.common.image.ImageLoader;
import cn.yue.base.common.photo.data.MediaFolderVO;
import cn.yue.base.common.photo.data.MediaType;
import cn.yue.base.common.photo.data.MediaVO;
import cn.yue.base.common.utils.code.ThreadUtils;
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
    protected void initTopBar(TopBar topBar) { }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_photo;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        RecyclerView rv = findViewById(R.id.photoRV);
        rv.setLayoutManager(new LinearLayoutManager(mActivity));
        rv.setAdapter(commonAdapter = new CommonAdapter<MediaFolderVO>(mActivity, allFolder) {
            @Override
            public int getLayoutIdByType(int viewType) {
                return R.layout.item_select_photo_folder;
            }

            @Override
            public void bindData(CommonViewHolder holder, int position, MediaFolderVO mediaFolderVO) {
                holder.setText(R.id.folderTV, mediaFolderVO.getName() + "（" + mediaFolderVO.getCount() +"）");
                ImageLoader.getLoader().loadImage((ImageView) holder.getView(R.id.folderIV), mediaFolderVO.getCoverUri());
                holder.setOnItemClickListener(position, mediaFolderVO, new CommonViewHolder.OnItemClickListener<MediaFolderVO>() {
                    @Override
                    public void onItemClick(int position, MediaFolderVO mediaFolderVO1) {
                        ((SelectPhotoActivity)mActivity).changeToSelectPhotoFragment(mediaFolderVO1.getId(), mediaFolderVO1.getName());
                    }
                });
            }
        });
        getAllPhotoFolder();
    }

    private List<MediaFolderVO> allFolder = new ArrayList<>();

    private void getAllPhotoFolder() {
        ExecutorService executorService = ThreadUtils.getSinglePool();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<MediaFolderVO> allFolder = PhotoUtils.getAllMediasFolder(mActivity);
                List<MediaVO> lastPhotos = PhotoUtils.getTheLastMedias(mActivity, 100, getMediaType());
                if (!lastPhotos.isEmpty()){
                    MediaFolderVO lastMediaFolderVO = new MediaFolderVO();
                    lastMediaFolderVO.setId("");
                    lastMediaFolderVO.setCoverUri(lastPhotos.get(0).getUri());
                    lastMediaFolderVO.setCount(lastPhotos.size());
                    lastMediaFolderVO.setName("最近照片");
                    allFolder.add(0, lastMediaFolderVO);
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
                if ((List<MediaFolderVO>) msg.obj != null) {
                    allFolder.addAll((List<MediaFolderVO>) msg.obj);
                }
                if (commonAdapter != null) {
                    commonAdapter.setList(allFolder);
                }
            }
            return false;
        }
    });

    private MediaType getMediaType() {
        return ((SelectPhotoActivity)mActivity).getMediaType();
    }
}
