package com.example.buscaminas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button helpButton = findViewById(R.id.helpButtonID);
        Button configGameButton = findViewById(R.id.startGameButtonID);
        Button exitButton = findViewById(R.id.exitButtonID);

        helpButton.setOnClickListener(this);
        configGameButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);
    }

    @Override
    public void onClick (View v) {
        Intent in;
        switch (v.getId()){
            case R.id.helpButtonID:
                in = new Intent(this,HelpActivity.class);
                startActivity(in);
                break;
            case R.id.startGameButtonID:
                in = new Intent(this,GameConfigActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                finish();
                break;
            case R.id.exitButtonID:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Toast.makeText(this,"Sortida",Toast.LENGTH_LONG).show();
    }
}