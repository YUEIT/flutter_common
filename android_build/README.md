##### android 打包配置

    通常情况下没有升级flutter sdk以及引用新的或者升级第三方插件情况下，只需要执行flutter目录下的build.sh脚本即可，反之如下实现。
    注意android编译工程使用的是简易工程还是完整工程，考虑到渠道包的因数，在编译打包时生成的产物路径是不一样的，下面以简易工程为例

####### 打包flutter编译产物为aar

    1、通过flutter目录下运行flutter build apk --no-sound-null-safety。编译生成flutter编译产物，
        在/build/app/intermediates/flutter/release下可以看到；
        build/app/intermediates/merged_native_libs/release/out/lib下的libflutter.so、libapp.so
    2、android_build下./gradlew buildFlutter，实际使用的是copyBuildFlutterFile将flutter产物copy到app/libs以及
        app/src/main/assets路径下；copyBuildAarFile将其打包为aar输出到flutter目录build/flutter_aar下

    完整工程打包为flutter build apk --no-sound-null-safety --flavor=auto

####### flutter引用第三方插件

    1、其他使用的插件（如flutter_boost）可以在android目录下AS窗口中通过Gradle 运行bundleReleaseAar
        获取生成的aar
    2、建立library，或者直接copy以plugin开始的library，放入生成的aar，并修改gradle.properties文件中的类库等信息
        通过运行./gradlew uploadArchives将其上传到maven库中
    3、在plugin这个library中的build.gradle中引用刚才上传到maven库中的依赖，在修改版本信息后将plugin上传到maven库中


    