package cn.yue.base.common.video;

import android.net.Uri;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.ArrayList;

import cn.yue.base.common.R;
import cn.yue.base.common.activity.BaseFragment;

/**
 * Description :
 * Created by yue on 2022/1/26
 */
@Route(path = "/common/viewVideo")
public class ViewVideoFragment extends BaseFragment {

    private SimpleExoPlayer simpleExoPlayer;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_view_video;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        PlayerView playerView = findViewById(R.id.playerView);
        simpleExoPlayer = new SimpleExoPlayer.Builder(mActivity).build();
        Bundle bundle = getArguments();
        if (bundle != null) {
            ArrayList<Uri> uris = bundle.getParcelableArrayList("uris");
            if (uris != null) {
                playerView.setPlayer(simpleExoPlayer);
                for (Uri uri : uris) {
                    MediaItem mediaItem = MediaItem.fromUri(uri);
                    simpleExoPlayer.addMediaItem(mediaItem);
                }
                simpleExoPlayer.prepare();
                simpleExoPlayer.play();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!simpleExoPlayer.isPlaying()) {
            simpleExoPlayer.play();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        simpleExoPlayer.pause();
    }
}
