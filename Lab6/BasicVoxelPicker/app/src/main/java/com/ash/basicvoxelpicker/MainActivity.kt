package com.ash.basicvoxelpicker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // landscape help provided by https://www.youtube.com/watch?v=KJGKj078Qag
    fun processInput(view: View) {
        val image = findViewById<ImageView>(R.id.gameImageView)
        val inputText = findViewById<EditText>(R.id.editInputText)
        val textLabel = findViewById<TextView>(R.id.gameLabel)

        if (inputText.text.toString().lowercase() == "minecraft") {
            image.setImageResource(R.drawable.minecraft)
            textLabel.text = "Minecraft"
        } else if (inputText.text.toString().lowercase() == "roblox") {
            image.setImageResource(R.drawable.roblox)
            textLabel.text = "Roblox"
        } else {
            image.setImageResource(R.drawable.cube)
            textLabel.text = "Type either Minecraft or Roblox"
        }
    }
}