package com.example.buscaminas;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class EndInfoActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Intent data = getIntent();
        String DayHourData = data.getStringExtra("DayHourData");
        String LogData = data.getStringExtra("LogData");

        TextView DayHourTextView = findViewById(R.id.time_values);
        TextView LogTextView = findViewById(R.id.log_values);

        DayHourTextView.setText(DayHourData);
        LogTextView.setText(LogData);

        Button sendMailButton = findViewById(R.id.send_mail_button);
        Button startGameButton = findViewById(R.id.start_game_result_button);
        Button exitButton = findViewById(R.id.exit_result_button);

        sendMailButton.setOnClickListener(this);
        startGameButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.send_mail_button:
                Intent in = new Intent(Intent.ACTION_SENDTO);
                in.setType("text/plain");
                in.setData(Uri.parse("mailto:"));
                TextView mailreView = findViewById(R.id.mail_re);
                TextView LogTextView = findViewById(R.id.log_values);
                TextView DayHourView = findViewById(R.id.time_values);
                in.putExtra(Intent.EXTRA_EMAIL,new String[]{mailreView.getText().toString()}); //Modificar destinatari
                in.putExtra(Intent.EXTRA_SUBJECT,"Game Result");
                in.putExtra(Intent.EXTRA_TEXT,DayHourView.getText().toString()+" "+LogTextView.getText().toString());
                startActivity(in);
                break;
            case R.id.start_game_result_button:
                in = new Intent(this,GameConfigActivity.class);
                startActivity(in);
                finish();
                break;
            case R.id.exit_result_button:
                finish();
                break;
        }
    }
}
