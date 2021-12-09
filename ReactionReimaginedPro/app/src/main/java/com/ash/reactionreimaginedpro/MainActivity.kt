package com.ash.reactionreimaginedpro

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Point
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import android.content.DialogInterface
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.provider.MediaStore
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    lateinit var logoView: ImageView
    lateinit var playButton: Button
    lateinit var timerView: TextView
    lateinit var countDownTimer: CountDownTimer
    lateinit var gameButton: Button
    lateinit var instructionButton: Button
    lateinit var currSongMediaPlayer: MediaPlayer

    var gameTimeLeft: Long = 5000;
    var timerRunning = false;
    var currScore = 0;
    var currHighScore: Int? = null;
    lateinit var sharedPref: SharedPreferences
    val preferencesKey = "com.ash.reactionreimaginedpro";
    val highScoreKey = "highScore";
    val timerSize = 35F

    var currTune = 0;
    private var taps = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logoView = findViewById(R.id.logoView);
        playButton = findViewById(R.id.startGame);
        timerView = findViewById(R.id.timerView);
        gameButton = findViewById(R.id.gameButton);
        gameButton.isSoundEffectsEnabled = false; // WOW took way too long to figure this out
        instructionButton = findViewById(R.id.instructionButton)

        sharedPref = getSharedPreferences(preferencesKey, Context.MODE_PRIVATE);
        currHighScore = sharedPref.getInt(highScoreKey, 0)

    }

    fun playGame(view: View) {
        logoView.visibility = INVISIBLE;
        playButton.visibility = INVISIBLE;
        instructionButton.visibility = INVISIBLE
        instructionButton.isEnabled = false

        playButton.text = getString(R.string.play_again)
        timerView.visibility = VISIBLE;

        // timer help from https://stackoverflow.com/questions/54095875/how-to-create-a-simple-countdown-timer-in-kotlin
        gameTimeLeft = 5000;
        beginTimer(gameTimeLeft)
        gameButton.visibility = VISIBLE
        gameButton.isEnabled = true
        currScore = 0;
        timerView.textSize = timerSize
    }

    fun doGameButtonTouch(view: View) {
        // getting window size help from https://alvinalexander.com/android/how-to-determine-android-screen-size-dimensions-orientation/
        val s = Point();
        windowManager.defaultDisplay.getSize(s); // may be deprecated, but it works
        Log.d("window", "window looks like it is size $s")

        val r = Random();

        gameTimeLeft -= 200;
        currScore++;

        beginTimer(gameTimeLeft)

        // calculation help from https://stackoverflow.com/questions/19135827/setting-a-buttons-position-to-random-points-on-screen-android
        gameButton.animate()
            .x(r.nextInt(s.x - gameButton.width).toFloat())
            .y(r.nextInt(s.y - 3*gameButton.height).toFloat())
            .setDuration(0)
            .start()
        playTune()
    }

    fun openInstructionDialog(view: View) {
        // dialog help from https://stackoverflow.com/questions/16603817/how-can-open-alert-dialog-box-in-android
        AlertDialog.Builder(this)
            .setTitle(R.string.instructions_title)
            .setMessage(R.string.instructions)
            .setPositiveButton("Okay") { _, _ ->
            }
            .setNegativeButton(R.string.resetHighScore) { _, _ ->
                resetHighScore()
            }
            .show()
    }

    fun tapResetHighScore(view: View) {
        if (currHighScore != 0) {
            taps++;
        }

        if (taps == 4) {
            resetHighScore();
            taps = 0;
            val snackbarReset = Snackbar.make(findViewById<ConstraintLayout>(R.id.root_layout), "Easter egg! High score reset.", Snackbar.LENGTH_SHORT);
            snackbarReset.show();
        }
    }

    private fun resetHighScore() {
        currHighScore = 0
        with(sharedPref.edit()) {
            putInt(highScoreKey, 0)
            apply();
        }
    }

    private fun endOfGame() {
        gameButton.isEnabled = false
        gameButton.visibility = INVISIBLE
        timerRunning = false;
        playButton.visibility = VISIBLE;
        timerView.textSize = 14F
        checkHighScore()
    }

    private fun beginTimer(length: Long) {
        if (timerRunning) countDownTimer.cancel();

        // android docs was big help here!
        countDownTimer = object: CountDownTimer(length, 10) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished/1000;
                val remainingMillis = millisUntilFinished%1000;

                timerRunning = true;

                timerView.textSize = timerSize * (millisUntilFinished.toFloat() / 5000F) // percentage based size
                timerView.text = "$seconds.$remainingMillis"
            }

            override fun onFinish() {
                timerView.text = "Score was $currScore"
                endOfGame();
            }
        }

        countDownTimer.start();
    }

    private fun checkHighScore() {
        if (currScore > currHighScore!!) {
            currHighScore = currScore;
            with(sharedPref.edit()) {
                putInt(highScoreKey, currScore)
                apply();
            }

            timerView.text = "Nice job! You got a new high score of $currScore."
            return;
        }

        timerView.text = "Better luck next time! Your score of $currScore wasn't good enough to beat $currHighScore."
    }

    private fun playTune() {
        // all sound help was from android docs
        val audioFiles = listOf<Int>(R.raw.a1, R.raw.a2, R.raw.b1, R.raw.c1, R.raw.c2,
        R.raw.d1, R.raw.d2, R.raw.e1, R.raw.f1, R.raw.f2, R.raw.g1, R.raw.g2);

        currSongMediaPlayer = MediaPlayer.create(this, audioFiles[currTune++ % 12])
        currSongMediaPlayer.setOnCompletionListener {
            it.release()
        }
        currSongMediaPlayer.start();

    }

}