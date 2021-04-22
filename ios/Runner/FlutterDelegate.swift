//
//  FlutterDelegate.swift
//  Runner
//
//  Created by YPC on 2021/4/5.
//

import Foundation
import flutter_boost


class FlutterDelegate: NSObject, FlutterBoostDelegate {
    
    var navigationController:UINavigationController!;
    
    //打开flutter页面
    func pushFlutterRoute(_ pageName: String!, uniqueId: String, arguments: [AnyHashable : Any]?) {
        var animated = false
        var present = false
        if arguments != nil {
            if arguments?["animated"] != nil{
                animated = arguments?["animated"] as! Bool;
            }
            
            if arguments?["present"] != nil{
                present = arguments?["present"] as! Bool;
            }
        }
        let vc = FBFlutterViewContainer.init()
        vc!.setName(pageName, uniqueId: uniqueId, params: arguments)
        if (present) {
            self.navigationController.present(vc!, animated: animated, completion: {})
        } else {
            self.navigationController.pushViewController(vc!, animated: animated)
        }
    }
    
    //打开native页面
    func pushNativeRoute(_ pageName: String!, arguments: [AnyHashable : Any]!) {
        
    }
    
    // 退出页面
    func popRoute(_ uniqueId: String!) {
        let vc = navigationController.presentedViewController
        if (vc is FBFlutterViewContainer && (vc as! FBFlutterViewContainer).uniqueIDString() == uniqueId) {
            vc?.dismiss(animated: true, completion: {})
        } else {
            navigationController.popViewController(animated: true)
        }
    }

}




