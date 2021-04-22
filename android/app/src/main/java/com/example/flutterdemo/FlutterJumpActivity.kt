package com.example.flutterdemo

import android.view.View
import cn.yue.base.common.activity.BaseActivity
import cn.yue.base.middle.router.PlatformRouter

import com.alibaba.android.arouter.facade.annotation.Route
import java.util.*

@Route(path = "/app/flutterJump")
class FlutterJumpActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.main_layout
    }

    override fun initView() {
        findViewById<View>(R.id.jump).setOnClickListener {
            val map: MutableMap<String?, String?> = HashMap()
            map["test1"] = "param1"
            map["test2"] = "param2"
            PlatformRouter.getInstance().build("flutter://example/flutterPage")
                    .withString("test1", "param1")
                    .withString("test2", "param2")
                    .navigation(this@FlutterJumpActivity)
        }
    }
}

