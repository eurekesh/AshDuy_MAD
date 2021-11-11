package com.ash.voxelpickerpro

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        groupRadioButton = findViewById<RadioGroup>(R.id.characterGroup);
        enderDragonCheckbox = findViewById<CheckBox>(R.id.enderDragonCheckbox);
        witherCheckBox = findViewById<CheckBox>(R.id.witherCheckBox);
        spinner = findViewById<Spinner>(R.id.spinnerSelect);
        hardcoreSwitch = findViewById<Switch>(R.id.hardcoreSwitch);
        viewablePlayerTitle = findViewById<TextView>(R.id.playerTitleView);
    }

    fun determineTitle(view: View) {
        val enderDragonText = if (enderDragonCheckbox.isChecked) " Slayer of Dragons &"; else "";
        val witherText = if (witherCheckBox.isChecked) " Destroyer of Demons,"; else "";
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
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState);
        outState.putString("playerTitle", playerTitle);
        super.onSaveInstanceState(outState);
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState);
        playerTitle = savedInstanceState.getString("playerTitle", "");
        viewablePlayerTitle.text = if (playerTitle == "") getString(R.string.playerTitle); else playerTitle;

    }
}