package com.example.buscaminas;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class GameConfigActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_configuration);

        Button startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(this);
    }

    @Override
    public void onClick (View v) {
        EditText alias=findViewById(R.id.alias_edit_text);
        RadioGroup gridsizerg = findViewById(R.id.grip_size_radiogroup);
        CheckBox timeControl = findViewById(R.id.time_control_checkbox);
        RadioGroup minepercentage = findViewById(R.id.mine_percentage_radiogroup);

        int gridsizeChecked = gridsizerg.getCheckedRadioButtonId();
        int minePercentageChecked = minepercentage.getCheckedRadioButtonId();
        String newAlias = alias.getText().toString();
        if(gridsizeChecked==-1 || minePercentageChecked==-1 || newAlias.isEmpty()){
            Toast.makeText(this,R.string.invalid_parameters_added, Toast.LENGTH_LONG).show();
        }else {
            Intent in = new Intent(this, GameActivity.class);
            in.putExtra("alias",newAlias);
            in.putExtra("gridsize",getGridSize(gridsizeChecked));
            in.putExtra("minePercentage",getPercentage(minePercentageChecked));
            if(timeControl.isChecked())
                in.putExtra("timeControl",true);
            startActivity(in);
            finish();
        }
    }

    private int getPercentage(int checkedMinePercentatge){
        switch (checkedMinePercentatge){
            case R.id.mine_grid_option1:
                return 15;
            case R.id.grid_size_option2:
                return 25;
            case R.id.grid_size_option3:
                return 35;
            default:
                return 15;
        }


    }
    private int getGridSize(int checkedGrid){
        switch (checkedGrid){
            case R.id.grid_size_option1:
                return 7;
            case R.id.grid_size_option2:
                return 6;
            case R.id.grid_size_option3:
                return 5;
            default:
                return 5;
        }
    }
}
