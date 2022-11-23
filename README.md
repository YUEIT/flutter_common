# flutter 工程结构

    flutter 3.3.0版本 support null safety

##### flutter 模式下

    路由生成命令
            flutter packages pub run build_runner build --delete-conflicting-outputs
    建议先执行
            flutter packages pub run build_runner clean

##### android 打包

    目录下运行build.sh。编译信息从local.properties配置，注意flutter.build为产物生成路径关联，
    通常简易工程下为release，完整工程下考虑到渠道包因数修改为productRelease

##### ios 打包

    目录下运行flutter build framework，将生成的app.framework已经第三方插件引入项目中
