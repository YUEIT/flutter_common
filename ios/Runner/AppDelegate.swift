import UIKit
import Flutter
import flutter_boost

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {
  override func application(
    _ app: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
  ) -> Bool {
    let delegate = FlutterDelegate.init()
    self.window = UIWindow.init(frame: UIScreen.main.bounds)
    let viewController = DemoViewController.init()
    let navi = UINavigationController.init(rootViewController: viewController)
    navi.navigationBar.isHidden = true
    delegate.navigationController = navi;
    self.window.rootViewController = navi
    self.window.makeKeyAndVisible()
    
    //flutterboost 注册
    FlutterBoost.instance().setup(app, delegate: delegate) { (engine: FlutterEngine?) in
        //自定义消息交互插件实现
        NetContectPlugin.bind(binaryMessenger: engine?.binaryMessenger)
        ConstantPlugin.bind(binaryMessenger: engine?.binaryMessenger)
        LogPlugin.bind(binaryMessenger: engine?.binaryMessenger)
    }
//    return super.application(app, didFinishLaunchingWithOptions: launchOptions)
    return true
  }
}
