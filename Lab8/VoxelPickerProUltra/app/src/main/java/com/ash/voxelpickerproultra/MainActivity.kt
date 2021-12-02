package com.ash.voxelpickerproultra

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    var playerTitle: String = "";
    lateinit var groupRadioButton: RadioGroup;
    lateinit var enderDragonCheckbox: CheckBox;
    lateinit var witherCheckBox: CheckBox;
    lateinit var spinner: Spinner;
    lateinit var constraintLayout: ConstraintLayout;
    lateinit var hardcoreSwitch: Switch;
    lateinit var viewablePlayerTitle: TextView;
    lateinit var logoButton: Button;
    lateinit var ratingTextView: TextView
    private var titleData = TitleData();
    private val REQ_CODE = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        groupRadioButton = findViewById<RadioGroup>(R.id.characterGroup);
        enderDragonCheckbox = findViewById<CheckBox>(R.id.enderDragonCheckbox);
        witherCheckBox = findViewById<CheckBox>(R.id.witherCheckBox);
        spinner = findViewById<Spinner>(R.id.ratingSpinner);
        hardcoreSwitch = findViewById<Switch>(R.id.hardcoreSwitch);
        viewablePlayerTitle = findViewById<TextView>(R.id.playerTitleView);
        logoButton = findViewById(R.id.logoButton);
        ratingTextView = findViewById(R.id.ratingText)

        logoButton.setOnClickListener {
            val selectedSpinnerItem = spinner.selectedItemPosition
            val currTitle = viewablePlayerTitle.text.toString()
            titleData.generateUrl(selectedSpinnerItem, currTitle);

            val int = Intent(this, OutboundActivity::class.java);
            int.putExtra("destUrl", titleData.url);
            int.putExtra("currTitle", currTitle);

            startActivityForResult(int, REQ_CODE);
        }
    }

    fun determineTitle(view: View) {
        val enderDragonText = if (enderDragonCheckbox.isChecked) " Slayer of Dragons"; else "";
        val witherText = if (witherCheckBox.isChecked) "& Destroyer of Demons,"; else "";
        val spinnerText = spinner.selectedItem.toString();
        val selectedRadio = groupRadioButton.checkedRadioButtonId;
        if (selectedRadio == -1) {
            val snackbarError = Snackbar.make(findViewById<ConstraintLayout>(R.id.root_layout), "You must select a Minecraft character!", Snackbar.LENGTH_SHORT);
            snackbarError.show();
            return;
        }
        val radioButtonText = findViewById<RadioButton>(selectedRadio).text;
        val hardcoreText = if(hardcoreSwitch.isChecked) "Hardcore Champion"; else "";
        playerTitle = "$radioButtonText, the $spinnerText: $enderDragonText $witherText $hardcoreText";
        viewablePlayerTitle.text = playerTitle;
        logoButton.isEnabled = true;
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState);
        outState.putString("playerTitle", playerTitle);
        outState.putBoolean("logoButtonEnabled", logoButton.isEnabled)
        super.onSaveInstanceState(outState);
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState);
        playerTitle = savedInstanceState.getString("playerTitle", "");
        if (savedInstanceState.getBoolean("logoButtonEnabled")) {
            logoButton.isEnabled = true;
        }
        viewablePlayerTitle.text = if (playerTitle == "") getString(R.string.playerTitle); else playerTitle;
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if((requestCode == REQ_CODE) && (resultCode == Activity.RESULT_OK)) {
            val datResult = data?.let{data.getIntExtra("rating", -1)};

            if (datResult == -1) {
                return;
            }

            ratingTextView.text = when (datResult) {
                in 0..1 -> "Hmmmm, try some different options!"
                else -> "Wow! We're glad you like it!"
            }
            ratingTextView.visibility = View.VISIBLE;
        }
    }
}