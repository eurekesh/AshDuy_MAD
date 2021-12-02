package com.ash.voxelpickerproultra

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Spinner
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.ash.voxelpickerproultra.databinding.ActivityOutboundBinding

class OutboundActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOutboundBinding
    private lateinit var playerTitle: TextView
    private lateinit var ratingSpinner: Spinner
    private var url: String? = null;
    private var title: String? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOutboundBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playerTitle = findViewById(R.id.titleView)
        ratingSpinner = findViewById(R.id.ratingSpinner)

        setSupportActionBar(binding.toolbar)
        url = intent.getStringExtra("destUrl");
        title = intent.getStringExtra("currTitle");

        playerTitle.text = "Greetings, $title";
        binding.fab.setOnClickListener { view ->
            goToWebsite()
        }
    }

    override fun onBackPressed() {
        val int = Intent()
        int.putExtra("rating", ratingSpinner.selectedItemPosition);
        setResult(Activity.RESULT_OK, int);
        super.onBackPressed()
        finish()
    }

    private fun goToWebsite(){
        val int = Intent(Intent.ACTION_VIEW, url?.let{ Uri.parse(url) });
        Log.i("startUrl", "Starting navigation to url $url");
        startActivity(int);
    }
}