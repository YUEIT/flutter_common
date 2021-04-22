package cn.yue.base.middle.view;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;

import cn.yue.base.common.activity.BaseFragment;
import cn.yue.base.common.widget.TopBar;
import cn.yue.base.middle.R;

/**
 * Description :
 * Created by yue on 2019/3/27
 */

@Route(path = "/middle/noNet")
public class NoNetFragment extends BaseFragment {

    @Override
    protected void initTopBar(TopBar topBar) {
        super.initTopBar(topBar);
        topBar.setCenterTextStr("网络问题解决方案");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_no_net;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        TextView textView = findViewById(R.id.tv0);
        textView.setText(Html.fromHtml(hint));
    }

    String hint = "<font color='#000000'>如果您的设备确认已连接入网络</font>\n" +
            "<font color='#9b9b9b'>\t\n" +
            "<p>• 已接入4G等移动网络：请确保手机等帐号处于<font color='#000000'>无欠费</font>等正常状态</p>\n" +
            "<p>• 已接入Wi-Fi:请确保所接入Wi-Fi热点正常联网。</p>\n" +
            "<p>• 若设备为iPhone，请前往 <font color='#000000'>设置-移动蜂窝网络</font> 找到麦店 ，设置使用数据选项为<font color='#000000'>无线局域网与蜂窝移动数据 </font></p>\n" +
            "</font>\n" +
            "<font color='000000'>如果您的设备未接入移动网络或Wi-Fi网络</font>\t\n" +
            "<font color='#9b9b9b'>\n" +
            "<p>• 前往设备 <font color='#000000'>设置 - Wi-Fi网络</font> 中选择一个可用的Wi-Fi热点接入。</p>\n" +
            "<p>• 前往设备的 <font color='#000000'>设置 - 蜂窝移动网络</font> 中启用蜂窝移动网络（运营商可能会收取数据通信费用）。</p>\n" +
            "</font>\t";
}
