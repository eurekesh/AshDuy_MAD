//
//  ViewController.swift
//  Lab4
//
//  Created by Ashlyn Duy on 9/29/21.
//

import UIKit

class ViewController: UIViewController, UITextFieldDelegate {
    
    @IBOutlet weak var peopleCount: UILabel!
    @IBOutlet weak var minuteCount: UILabel!
    @IBOutlet weak var resultLabel: UILabel!
    @IBOutlet weak var peopleCountOutlet: UIStepper!
    @IBOutlet weak var minutesTextField: UITextField!
    
    @IBAction func peopleStepper(_ sender: UIStepper) {
        if sender.value == 1 {
            peopleCount.text = "1 Person";
        } else {
            let newVal = sender.value;
            peopleCount.text = String(format: "%.0f", newVal) + " People";
            
        }
        calculateAverage();
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        minutesTextField.resignFirstResponder()
        return true
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        calculateAverage();
    }
    
    private func calculateAverage(){
        let convertTest = Int(minutesTextField.text!) // make sure it's actually a number
        
        if minutesTextField.text == nil || minutesTextField.text == "" || convertTest == nil {
            resultLabel.text = "Please enter in a valid number of minutes."
            return;
        }
        if Int(minutesTextField.text!)! == 0 {
            let divideByZeroAlert = UIAlertController(title: "Warning", message: "The number of minutes must be greater than or equal to 1", preferredStyle: .alert);
            let okDismiss = UIAlertAction(title: "OK", style:.cancel, handler: nil);
            divideByZeroAlert.addAction(okDismiss);
            present(divideByZeroAlert, animated: true, completion: nil);
            return;
        }
        
        let average = peopleCountOutlet.value / Double(minutesTextField.text!)!; // quirky!
        resultLabel.text = "On average, you saw " + String(format: "%.2f", average) + " people per minute."
    }
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        minutesTextField.delegate = self;
        // source for this was chapter 7 of the book
        view.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(obliterateKeyboard)))
    }
    
    @objc func obliterateKeyboard() {
        view.endEditing(true);
        calculateAverage();
    }
    



}

