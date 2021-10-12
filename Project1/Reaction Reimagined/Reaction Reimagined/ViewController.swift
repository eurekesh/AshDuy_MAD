//
//  ViewController.swift
//  Reaction Reimagined
//
//  Created by Ashlyn Duy on 9/27/21.
//

import UIKit
import Foundation

class ViewController: UIViewController {
    // persistent storage setup
    let userDefaults = UserDefaults.standard
    let userDefaultsKey = "highScores"
    
    // timer help graciously provided by (and modified from) https://stackoverflow.com/a/52459742
    var currentTimerInterval = 5.00
    var currentTimeRemaining = 0.00
    var timerVar: Timer?
    var currentScore = 0
    var randButtonX: CGFloat?
    var randButtonY: CGFloat?
    
    // for fast prototyping
    let buttonRadius = 6;
    
    // make sure new random labels don't bump into the timer!
    var timerLabelX: CGFloat?
    var timerLabelY: CGFloat?
    
    // just in case nothing's in persistent storage yet
    var currentHighScore = 0;
    
    // button labels – can control text
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var playButton: UIButton!
    @IBOutlet weak var hitMeButton: UIButton!
    @IBOutlet weak var highScoreButton: UIButton!
    @IBOutlet weak var titleImageOutlet: UIImageView!
    @IBOutlet weak var instructionsButton: UIButton!
    
    // ------ overrides ------
    override func viewDidLoad() {
        super.viewDidLoad()
        overrideUserInterfaceStyle = .light; // oops, I'm only going to support light mode, looked weird when testing on my phone!
        
        doRoundedCorners();
        timerLabelX = titleLabel.center.x;
        timerLabelY = titleLabel.center.y;
        currentHighScore = userDefaults.integer(forKey: userDefaultsKey)
        print("Current high score is " + currentHighScore.description)
        print("Title X " + timerLabelX!.description + " Title Y " + timerLabelY!.description)
    }
    
    override func viewDidLayoutSubviews() {
        // override auto layout, make sure the buttons actually move! fight auto layout for control and WIN!
        if let overrideX = randButtonX {
            hitMeButton.center.x = overrideX;
        }
        if let overrideY = randButtonY {
            hitMeButton.center.y = overrideY;
        }
    }
    
    // ------ IBActions ------
    // button actions – make them do things
    @IBAction func playButtonAction(_ sender: UIButton) {
        // hide the buttons that aren't being used, and show first button
        currentScore = 0;
        hitMeButton.isHidden = false
        hitMeButton.isEnabled = true
        toggleButtonVisibilites(true);
        titleImageOutlet.isHidden = true // only show on first play of session
        titleLabel.isHidden = false; // time to bring it back!
        runGameIteration()
    }
    
    @IBAction func hitMeButtonAction(_ sender: UIButton) {
        // get the candidate for location and reset it
        (randButtonX, randButtonY) = newRandomXY(sender);
        (randButtonX, randButtonY) = (randButtonX! + (sender.frame.width / 2), randButtonY! + (sender.frame.height / 2))
        print("X " + randButtonX!.description + " Y " + randButtonY!.description);
        sender.center.x = randButtonX!;
        sender.center.y = randButtonY!;
        
        // game loop updates
        currentTimerInterval -= 0.2;
        currentScore += 1;
        runGameIteration();
    }
    
    @IBAction func highScoreButtonAction(_ sender: UIButton) {
        let message = UIAlertController(title: "Current High Score", message: String(currentHighScore), preferredStyle: .alert);
        let okAction = UIAlertAction(title: "OK", style: .default, handler: nil);
        let resetAction = UIAlertAction(title: "Reset", style: .default, handler: { action in self.resetHighScore() })
        message.addAction(okAction);
        message.addAction(resetAction);
        present(message, animated: true, completion: nil)
    }
    
    @IBAction func instructionButtonAction(_ sender: UIButton) {
        let instructionMessage = "Welcome to Reaction Reimagined! This is a fast-paced game with a ticking clock – every tap of the button will send it to a new location, try to tap as many times as you can before the time runs out!";
        let message = UIAlertController(title: "Instructions", message: instructionMessage, preferredStyle: .alert);
        let okAction = UIAlertAction(title: "OK", style: .default, handler: nil);
        message.addAction(okAction);
        present(message, animated: true, completion: nil)
    }
    
    // ------ Game logic ------
    
    private func runGameIteration() -> Void {
        currentTimeRemaining = currentTimerInterval;
        createTimer();
    }
    
    private func doGameOver() -> Void {
        hitMeButton.isHidden = true;
        hitMeButton.cancelTracking(with: nil);
        print("Is tracking: " + String(hitMeButton.isTracking))
        print("You got " + String(currentScore) + " score!");
        titleLabel.text = "You got " + String(currentScore) + " score!"
        playButton.setTitle("Play Again", for: .normal);
        toggleButtonVisibilites(false);
        currentTimerInterval = 5.00;
        calcHighScore()
        currentScore = 0;
    }
    
    // persistent storage check, much thanks to dev documentation for this
    private func calcHighScore() -> Void {
        if currentScore > currentHighScore {
            currentHighScore = currentScore;
            userDefaults.set(currentHighScore, forKey: userDefaultsKey)
        }
    }
    
    private func createTimer() -> Void {
        if(self.timerVar == nil){ // needed because timers are weird, and are apparently not thread-safe
            self.timerVar = Timer.scheduledTimer(timeInterval: 0.01, target: self, selector: #selector(updateTimerLabel), userInfo: nil, repeats: true)
        }
        
    }
    
    @objc private func updateTimerLabel() {
        if(self.currentTimeRemaining <= 0.01){
            self.timerVar?.invalidate();
            self.timerVar = nil;
            print("all done")
            doGameOver();
        }
        else {
            currentTimeRemaining -= 0.01
            self.titleLabel.text = buildTimerString();
        }
    }
    
    private func buildTimerString() -> String {
        return String(format: "%.2f", currentTimeRemaining)
    }
    
    // randomness help graciously provided by https://stackoverflow.com/a/26075459 – needed some modification to solve bezel issue
    private func newRandomXY(_ button: UIButton) -> (CGFloat, CGFloat){
        // finally fixes going out of bounds issue, safe are to the rescue!
        let heightY = button.superview!.safeAreaLayoutGuide.layoutFrame.minY;
        let xbounds = button.superview!.safeAreaLayoutGuide.layoutFrame.width - button.frame.width;
        let ybounds = button.superview!.safeAreaLayoutGuide.layoutFrame.height;
        
        var count = 0;
        while(true){ // keep generating candidates until we are above the bezel, tried many different things but ultimately this is what worked!
            let (resX, resY) = generateRandomCandidate(xbound: xbounds, ybound: ybounds);
            if (resY > heightY) {
                print("Required " + count.description + " generations") // always interesting to see how many times a candidate needed to be generated
                return (resX, resY);
            }
            count+=1;
        }
//        return generateRandomCandidate(xbound: xbounds, ybound: ybounds)
    }
    
    // generates random tuple when giving bounds
    private func generateRandomCandidate(xbound: CGFloat, ybound: CGFloat) -> (CGFloat, CGFloat){
        return (CGFloat(arc4random_uniform(UInt32(xbound))), CGFloat(arc4random_uniform(UInt32(ybound))))
    }
    
    // make buttons look a little nicer
    private func doRoundedCorners() -> Void {
        highScoreButton.layer.cornerRadius = CGFloat(buttonRadius);
        playButton.layer.cornerRadius = CGFloat(buttonRadius);
        instructionsButton.layer.cornerRadius = CGFloat(buttonRadius);
    }
    
    private func toggleButtonVisibilites(_ show: Bool) -> Void {
        playButton.isHidden = show;
        highScoreButton.isHidden = show;
        instructionsButton.isHidden = show;
    }
    
    // just in case the user wants a fresh coat of paint
    private func resetHighScore() -> Void {
        userDefaults.set(0, forKey: userDefaultsKey);
        currentHighScore = 0;
    }


}

