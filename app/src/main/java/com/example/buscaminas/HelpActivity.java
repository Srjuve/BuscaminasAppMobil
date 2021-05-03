package com.example.buscaminas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HelpActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Button return_button = findViewById(R.id.return_button);
        return_button.setOnClickListener(this);
    }

    @Override
    public void onClick (View v) {
        finish();
    }
}
