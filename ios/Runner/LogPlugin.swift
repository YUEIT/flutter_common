//
//  LogPlugin.swift
//  Runner
//
//  Created by YPC on 2021/4/7.
//

import Foundation
import flutter_boost

class LogPlugin:NSObject {
        
    static func bind(binaryMessenger: FlutterBinaryMessenger?) {
        if (binaryMessenger == nil) {
            return
        }
        let channelName = "plugins.flutter.ypc.com/log"
        let messageChannel = FlutterMethodChannel.init(name: channelName, binaryMessenger: binaryMessenger!)
        messageChannel.setMethodCallHandler { (call, result) in
            let param = call.arguments as! [String:String]
            let tag: String? = param["tag"]
            let msg: String? = param["msg"]
            switch call.method {
                case "logInfo" :
                    logInfo(tag: tag, msg: msg)
                    result(true);
                case "logError" :
                    logError(tag: tag, msg: msg)
                    result(true);
                default:
                    result(false)
            }
        }
    
    }
    
    static func logInfo(tag: String?, msg: String?) {
        print("\(tag) : \(msg)")
    }
    
    static func logError(tag: String?, msg: String?) {
        print("\(tag) : \(msg)")
    }
}

