# flutter 工程结构

    flutter 2.0.1版本

    尺寸适配以ios设计稿（宽度：375pt）为基准适配，项目内使用扩展方法".rdp"、".rsp"为基本尺寸。
    debug.dart文件中isDebug根据编译环境来确认，release下会会关闭所有debug配置
    网络接口抓包需要在debug.dart文件中配置开启，并配置主机IP
    
    图片名生成使用FlutterAssetsGenerator(AS plugin搜索下载)，并在preferences -> Tools -> FlutterAssetsGenerator
    中设置文件为"main/assets"，类名为"ImageAssets"；图片放置在assets/image路径下后点击Build -> Generate Assets class
    参考(https://blog.csdn.net/weixin_41779718/article/details/110109456)

##### flutter 模式下
    Edit Configurations 配置参数：
    --no-sound-null-safety --flavor=auto
    
    直接编译运行
    注意，如果修改过android gradle配置项，可能会报一些奇怪的错误，这时如果在android下编译正常的话，删除掉build目录后重新编译。
    如果发现flutter代码未生效再点下运行，flutter hot Reload.
    
    路由生成命令
            flutter packages pub run build_runner build --delete-conflicting-outputs
    建议先执行
            flutter packages pub run build_runner clean

##### android 打包

    目录下运行build.sh。编译信息从local.properties配置，注意flutter.build为产物生成路径关联，
    通常简易工程下为release，完整工程下考虑到渠道包因数修改为productRelease

##### ios 打包

    目录下运行flutter build framework --no-sound-null-safety，将生成的app.framework已经第三方插件引入项目中

##### 各种适配问题
    1、build.gradle productFlavors outputFileName 都会修改apk生成路径或apk名，从而导致flutter打包时找不到对应文件而出错
        解决方式需要配置Edit Configurations；在Additional arguments:输入“--flavor=auto”，Build flavor:输入“auto”；其中test为配置渠道
    2、ndk 配置，不能通过abiFilters设置仅使用部分硬件架构，libflutter.so 需要全架构支持（有时配置了也能编译，那么就不需要处理了）
    3、app 路径下GeneratedPluginRegistrant 不能删除，且build.gradle中 引用的"flutter.gradle" 不能删除
        AndroidManifest中flutterEmbedding 不能删除
    4、app AndroidManifest 必须包含启动页LAUNCHER
    5、gradle版本，不支持高版本的，具体需要对应flutter版本
    6、flutter 工程目录下编译对native修改内容有时不会编译，需要重新clean后编译或者在native下编译后再编译
    7、出现莫名编译报错，clean工程后再试；出现"xxxx is not directory"时删除android/.gradle后再编译
    8、flutter编译产物和flutter embedding的生成环境必须一直，debug和release下编译的flutter产物是不一样的
    