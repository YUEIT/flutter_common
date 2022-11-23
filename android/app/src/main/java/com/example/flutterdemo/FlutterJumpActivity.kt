package com.example.flutterdemo

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import cn.yue.base.flutter.activity.CommonActivity
import cn.yue.base.flutter.router.PlatformRouter

import com.alibaba.android.arouter.facade.annotation.Route
import java.util.*

@Route(path = "/app/flutterJump")
class FlutterJumpActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)
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

