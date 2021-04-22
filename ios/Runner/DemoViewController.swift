//
//  DemoViewController.swift
//  Runner
//
//  Created by YPC on 2021/4/5.
//


import UIKit
import flutter_boost

class DemoViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }

    @IBAction func onClickPushFlutterPage(_ sender: UIButton, forEvent event: UIEvent){
        FlutterBoost.instance().open("flutter://example/flutterPage",arguments: ["animated":true])
    }
   
}
