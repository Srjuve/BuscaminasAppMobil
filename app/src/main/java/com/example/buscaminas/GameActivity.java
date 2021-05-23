package com.example.buscaminas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends Activity {
    MineSearchGame gameInstance;
    int numColums;
    boolean checkTime;
    String alias;
    int minePercentage;
    List<Button> entries;
    GridView gridv;
    CustomButtonAdapter gridAdapter;
    CountDownTimer timer;
    Timer timerFalse;
    int time_counter;
    int maxTime;
    int num_mines;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        //Set the game activity needed data
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        if(savedInstanceState==null) {
            //If we do not restore a game activity instance we create/get the needed data
            Intent data = getIntent();
            this.numColums = data.getIntExtra("gridsize", 5);
            this.minePercentage = data.getIntExtra("minePercentage",15);
            this.checkTime = data.getBooleanExtra("timeControl",false);
            this.alias = data.getStringExtra("alias");
            this.num_mines = this.numColums*this.numColums*this.minePercentage/100;
            this.gameInstance = createGameInstance(data);
            this.time_counter=30;
            this.maxTime=this.time_counter;
            TextView time_view = findViewById(R.id.time_text);
            TextView time_following_text = findViewById(R.id.seconds_text);
            if(this.checkTime){
                time_view.setTextColor(ContextCompat.getColor(this,R.color.red));
                time_following_text.setTextColor(ContextCompat.getColor(this,R.color.red));
                createCountDownTimer();
            }else{
                time_view.setTextColor(ContextCompat.getColor(this,R.color.blue));
                time_following_text.setTextColor(ContextCompat.getColor(this,R.color.blue));
                createSimpleTimer();
            }
            TextView undiscovered_view = (TextView)findViewById(R.id.undiscovered_text);
            undiscovered_view.setText(String.valueOf(this.gameInstance.getUndiscoveredCount())+" casillas por descubrir");
            startGridView();
        }

    }

    private void createSimpleTimer(){
        //Create timer used when we do not need to control time
        this.timerFalse =new Timer();
        TextView time_value = findViewById(R.id.time_text);
        time_value.setText(String.valueOf(this.time_counter));
        this.timerFalse.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                TextView time_value = findViewById(R.id.time_text);
                int value= Integer.parseInt(time_value.getText().toString());
                value-=1;
                time_value.setText(String.valueOf(value));
            }
        },0,1000);
    }

    private void createCountDownTimer(){
        //Create timer when we do need to control the time
        MineSearchGame game = this.gameInstance;
        String minePercentage = String.valueOf(this.minePercentage)+"%";
        int max_time = this.time_counter;
        String num_mines = String.valueOf(this.num_mines);
        Context actualContext = getApplicationContext();
        Activity context = this;
        this.timer=new CountDownTimer(this.time_counter*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TextView time_view = (TextView)findViewById(R.id.time_text);
                time_view.setText(String.valueOf(time_counter));
                time_counter-=1;
            }

            private void createToast(String message){
                LayoutInflater inflater = context.getLayoutInflater();
                View layout = inflater.inflate(R.layout.endtoast,(ViewGroup)context.findViewById(R.id.toast_layout));
                TextView text = layout.findViewById(R.id.toast_text_id);
                text.setText(message);
                Toast toast = new Toast(context);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            }

            @Override
            public void onFinish() {
                //Toast.makeText(getApplicationContext(),"Tiempo agotado!.Repite suerte...",Toast.LENGTH_LONG).show();
                createToast("Tiempo agotado!.Repite suerte...");
                String alias = gameInstance.getUserAlias();
                String timeTaken = String.valueOf(max_time-time_counter);
                Intent data = new Intent(getBaseContext(),EndInfoActivity.class);
                data.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                String undiscovered = String.valueOf(gameInstance.getUndiscoveredCount());
                setDayHourData(data);
                data.putExtra("LogData","Alias: "+alias+" Casillas: "+ minePercentage +" Minas: "+ num_mines +" Tiempo Total: "+ timeTaken+ " Has agotado el tiempo!! Te han quedado "+ undiscovered +" casillas por descubrir" );
                startActivity(data);
                finish();
            }
        }.start();
    }

    private void startGridView(){
        //Creating the used grid view
        this.gridv = findViewById(R.id.game_grid);
        this.gridv.setNumColumns(this.numColums);
        if(this.checkTime) {
            this.gridAdapter = new CustomButtonAdapter(this,  this.gameInstance, this.timer, this.maxTime);
        }else{
            this.gridAdapter = new CustomButtonAdapter(this, this.gameInstance, this.timerFalse, this.maxTime);
        }
        this.gridv.setAdapter(gridAdapter);
    }

    private MineSearchGame createGameInstance(Intent data){
        MineSearchGame searchGame = new MineSearchGame(this.numColums,this.alias,this.minePercentage);
        return searchGame;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        //We save some needed data like game state, time, etc
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("SavedGameState",this.gameInstance);
        TextView time = findViewById(R.id.time_text);
        savedInstanceState.putInt("time_counter",Integer.parseInt(time.getText().toString()));
        savedInstanceState.putBoolean("check_time",this.checkTime);
        savedInstanceState.putInt("max_time",this.maxTime);
        if(this.checkTime){
            this.timer.cancel();
        }else{
            this.timerFalse.cancel();
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        //We restore the data of an already created instance
        super.onRestoreInstanceState(savedInstanceState);
        this.gameInstance=(MineSearchGame)savedInstanceState.getSerializable("SavedGameState");
        this.maxTime=savedInstanceState.getInt("max_time");
        this.numColums=this.gameInstance.getGridSize();
        this.checkTime=savedInstanceState.getBoolean("check_time");
        this.time_counter=savedInstanceState.getInt("time_counter");
        TextView time_view = findViewById(R.id.time_text);
        TextView time_following_text = findViewById(R.id.seconds_text);
        if(this.checkTime){
            time_view.setTextColor(ContextCompat.getColor(this,R.color.red));
            time_following_text.setTextColor(ContextCompat.getColor(this,R.color.red));
            createCountDownTimer();
        }else{
            time_view.setTextColor(ContextCompat.getColor(this,R.color.blue));
            time_following_text.setTextColor(ContextCompat.getColor(this,R.color.blue));
            createSimpleTimer();
        }
        TextView undiscovered_view = findViewById(R.id.undiscovered_text);
        undiscovered_view.setText(String.valueOf(this.gameInstance.getUndiscoveredCount())+" casillas por descubrir");
        startGridView();
    }

    private void setDayHourData(Intent data){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        data.putExtra("DayHourData",dateFormat.format(calendar.getTime()));
    }
}
