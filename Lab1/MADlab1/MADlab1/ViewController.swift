//
//  ViewController.swift
//  MADlab1
//
//  Created by Ashlyn Duy on 8/31/21.
//

import UIKit

class ViewController: UIViewController {
    
    @IBOutlet weak var centerLabel: UILabel!
    @IBOutlet weak var gallery: UIImageView!
    
    @IBAction func leftButton(_ sender: UIButton) {
        if(sender.tag == 0){
            centerLabel.text = "Minecraft"
            gallery.image = UIImage(named: "Minecraft")
        }
        else if(sender.tag == 1){
            centerLabel.text = "Roblox"
            gallery.image = UIImage(named: "Roblox")
        }
    }
    

    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }


}

