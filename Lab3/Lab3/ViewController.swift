//
//  ViewController.swift
//  Lab3
//
//  Created by Ashlyn Duy on 9/22/21.
//

import UIKit

class ViewController: UIViewController {

    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var imageLabel: UILabel!
    @IBOutlet weak var fontSliderControl: UISlider!
    @IBOutlet weak var capitalSwitchControl: UISwitch!
    
    @IBAction func segmentedControl(_ sender: UISegmentedControl) {
        if(sender.selectedSegmentIndex == 0){
            imageView.image = UIImage(named: "Minecraft");
            imageLabel.text = "Minecraft";
        }
        else {
            imageView.image = UIImage(named: "Roblox");
            imageLabel.text = "Roblox";
        }
        imageLabel.text = capitalSwitchControl.isOn ? imageLabel.text?.uppercased() : imageLabel.text?.lowercased();
    }
    
    @IBAction func fontSlider(_ sender: UISlider) {
        imageLabel.font = UIFont.systemFont(ofSize: CGFloat(sender.value))
    }
    
    @IBAction func capitalSwitch(_ sender: UISwitch) {
        if (sender.isOn) {
            imageLabel.text = imageLabel.text?.uppercased();
        }
        else {
            imageLabel.text = imageLabel.text?.lowercased();
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad();
        fontSliderControl.value = 17;
    }


}

