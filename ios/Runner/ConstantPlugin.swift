//
//  ConstantPlugin.swift
//  Runner
//
//  Created by YPC on 2021/4/7.
//

import Foundation
import flutter_boost

class ConstantPlugin:NSObject {
        
    static func bind(binaryMessenger: FlutterBinaryMessenger?) {
        if (binaryMessenger == nil) {
            return
        }
        let channelName = "plugins.flutter.ypc.com/constant"
        let messageChannel = FlutterMethodChannel.init(name: channelName, binaryMessenger: binaryMessenger!)
        messageChannel.setMethodCallHandler { (call, result) in
            switch call.method {
                case "getDevice" :
                    result(getDevice())
                case "getDeviceId" :
                    result(getDeviceId())
                case "getVersionName" :
                    result(getVersionName())
                case "getVersionCode":
                    result(getVersionCode())
                case "getToken" :
                    result(getToken());
                case "getChannel" :
                    result(getChannel())
                case "getSystemVersion" :
                    result(getSystemVersion())
                case "getTimestamp" :
                    result(getTimeStamp())
                default:
                    result(false)
            }
        }
    
    }
    
    static func getDevice() -> String {
        return "ios"
    }
    
    static func getDeviceId() -> String {
        return "Xg0yixb9rmMDAPY3kRcCSW3G"
    }
    
    static func getVersionName() -> String {
        return "3.19.0"
    }
    
    static func getVersionCode() -> String {
        return "031900"
    }
    
    static func getToken() -> String {
        return "56cd89bc6ab327a9.a3fc09c4bb5f0a91abf5b15d25fe232d"
    }
    
    static func getChannel() -> String {
        return "iphone 12 pro max"
    }
    
    static func getSystemVersion() -> String {
        return "ios 12"
    }
    
    static func getTimeStamp() -> String {
        let time = Int(Date().timeIntervalSince1970)
        return "\(time)"
    }
}

