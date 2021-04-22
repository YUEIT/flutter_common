//
//  NetContectPlugin.swift
//  Runner
//
//  Created by YPC on 2021/4/6.
//

import Foundation
import flutter_boost

class NetContectPlugin:NSObject {
        
    static func bind(binaryMessenger: FlutterBinaryMessenger?) {
        if (binaryMessenger == nil) {
            return
        }
        let channelName = "plugins.flutter.ypc.com/net_connect"
        let messageChannel = FlutterMethodChannel.init(name: channelName, binaryMessenger: binaryMessenger!)
        messageChannel.setMethodCallHandler { (call, result) in
            print("call ios method")
            switch call.method {
                case "isNetConnect" :
                    result(true);
                case "isWifi" :
                    result(true);
                case "isMobile":
                    result(true);
                default:
                    result(false)
            }
        }
    
    }
}
