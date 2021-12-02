package com.ash.reactionreimaginedpro

import android.content.Context
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager

import androidx.annotation.NonNull




class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun getDisplaySize(context: Context): Point? {
        val point = Point()
        val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        context.getDisplay().getSize(point)
        return point
    }
}