//
//  ViewController.swift
//  Reaction Reimagined
//
//  Created by Ashlyn Duy on 9/27/21.
//

import UIKit

class ViewController: UIViewController {
    
    // timer help graciously provided by (and modified from) https://stackoverflow.com/a/52459742
    var currentTimerInterval = 5.00
    var currentTimeRemaining = 0.00
    var timerVar: Timer?
    var currentScore = 0
    var randButtonX: CGFloat?
    var randButtonY: CGFloat?
    
    //make sure new random labels don't bump into the timer!
    var timerLabelX: CGFloat?
    var timerLabelY: CGFloat?
    
    var currentHighScore = 0;
    
    // button labels – can control text
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var playButton: UIButton!
    @IBOutlet weak var hitMeButton: UIButton!
    @IBOutlet weak var highScoreButton: UIButton!
    
    // ------ overrides ------
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        timerLabelX = titleLabel.center.x;
        timerLabelY = titleLabel.center.y;
    }
    
    override func viewDidLayoutSubviews() {
        // override auto layout, make sure the buttons actually move!
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
        playButton.isHidden = true
        highScoreButton.isHidden = true
        runGameIteration()
    }
    
    @IBAction func hitMeButtonAction(_ sender: UIButton) {
        (randButtonX, randButtonY) = newRandomXY(sender);
        (randButtonX, randButtonY) = (randButtonX! + (sender.frame.width / 2), randButtonY! + (sender.frame.height / 2))
        print("X " + randButtonX!.description + " Y " + randButtonY!.description);
        sender.center.x = randButtonX!;
        sender.center.y = randButtonY!;
        
        currentTimerInterval -= 0.2;
        currentScore += 1;
        runGameIteration();
    }
    
    @IBAction func highScoreButtonAction(_ sender: UIButton) {
        let message = UIAlertController(title: "Current High Score", message: String(currentHighScore), preferredStyle: .alert);
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
        print("You got " + String(currentScore) + " score!");
        titleLabel.text = "You got " + String(currentScore) + " score!"
        playButton.setTitle("Play Again", for: .normal);
        playButton.isHidden = false;
        highScoreButton.isHidden = false;
        currentTimerInterval = 5.00;
        calcHighScore()
        currentScore = 0;
    }
    
    private func calcHighScore() -> Void {
        if currentScore > currentHighScore {
            currentHighScore = currentScore;
        }
    }
    
    private func createTimer() -> Void {
        if(self.timerVar == nil){ // needed because timers are weird
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
    
    // we need a new button position, but we need to make sure that it's NOT overlapping with the timer! TODO: implement this fully, not needed for initial MVP
    private func newRandomXY(_ button: UIButton) -> (CGFloat, CGFloat){
        let xbounds = button.superview!.bounds.width - button.frame.width;
        let ybounds = button.superview!.bounds.height - button.frame.height - 20;
        return generateRandomCandidate(xbound: xbounds, ybound: ybounds)
//        while(true){
//            var (xcandidate, ycandidate) = generateRandomCandidate(xbound: xbounds, ybound: ybounds);
//
//        }
    }
    
    private func generateRandomCandidate(xbound: CGFloat, ybound: CGFloat) -> (CGFloat, CGFloat){
        return (CGFloat(arc4random_uniform(UInt32(xbound))), CGFloat(arc4random_uniform(UInt32(ybound))))
    }


}

