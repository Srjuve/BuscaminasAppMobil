package com.example.buscaminas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends Activity {
    MineSearchGame gameInstance;
    int numColums;
    //List<TextView> entries;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        if(savedInstanceState==null) {
            Intent data = getIntent();
            this.numColums = data.getIntExtra("gridsize", 5);
            this.minePercentage = data.getIntExtra("minePercentage",15);
            this.checkTime = data.getBooleanExtra("timeControl",false);
            this.alias = data.getStringExtra("alias");
            this.num_mines = this.numColums*this.numColums*this.minePercentage/100;
            this.gameInstance = createGameInstance(data);
            this.time_counter=60;
            this.maxTime=this.time_counter;
            if(this.checkTime){
                createCountDownTimer();
            }else{
                createSimpleTimer();
            }
            TextView undiscovered_view = (TextView)findViewById(R.id.undiscovered_text);
            undiscovered_view.setText(String.valueOf(this.gameInstance.getUndiscoveredCount())+" casillas por descubrir");
            startNewEntries();
            startGridView();
        }

    }

    private void createSimpleTimer(){
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
        MineSearchGame game = this.gameInstance;
        String minePercentage = String.valueOf(this.minePercentage)+"%";
        int max_time = this.time_counter;
        String num_mines = String.valueOf(this.num_mines);
        this.timer=new CountDownTimer(this.time_counter*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TextView time_view = (TextView)findViewById(R.id.time_text);
                time_view.setText(String.valueOf(time_counter));
                time_counter-=1;
            }

            @Override
            public void onFinish() {
                String alias = gameInstance.getUserAlias();
                String timeTaken = String.valueOf(max_time-time_counter);
                Intent data = new Intent(getBaseContext(),EndInfoActivity.class);
                String undiscovered = String.valueOf(gameInstance.getUndiscoveredCount());
                setDayHourData(data);
                data.putExtra("LogData","Alias: "+alias+" Casillas: "+ minePercentage +" Minas: "+ num_mines +" Tiempo Total: "+ timeTaken+ " Has agotado el tiempo!! Te han quedado "+ undiscovered +" casillas por descubrir" );
                startActivity(data);
                finish();
            }
        }.start();
    }

    private void startGridView(){
        this.gridv = findViewById(R.id.game_grid);
        this.gridv.setNumColumns(this.numColums);
        if(this.checkTime) {
            this.gridAdapter = new CustomButtonAdapter(this, this.entries, this.gameInstance, this.timer, this.maxTime);
        }else{
            this.gridAdapter = new CustomButtonAdapter(this, this.entries, this.gameInstance, this.timerFalse, this.maxTime);
        }
        this.gridv.setAdapter(gridAdapter);
    }

    private MineSearchGame createGameInstance(Intent data){
        MineSearchGame searchGame = new MineSearchGame(this.numColums,this.alias,this.minePercentage);
        return searchGame;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("SavedGameState",this.gameInstance);
        TextView time = findViewById(R.id.time_text);
        savedInstanceState.putInt("time_counter",Integer.parseInt(time.getText().toString()));
        savedInstanceState.putBoolean("check_time",this.checkTime);
    }

    private void startNewEntries(){
        this.entries= new ArrayList<>();
        for(int i=0;i<this.numColums;i+=1){
            for(int j=0;j<this.numColums;j+=1){
                Button newButton = new Button(this);
                if(!this.gameInstance.isDiscovered(i,j)){
                    newButton.setBackgroundResource(R.drawable.rectangle);
                }else{
                    newButton.setBackgroundResource(R.drawable.rectangleshowed);
                    newButton.setGravity(Gravity.CENTER);
                    newButton.setText(String.valueOf(this.gameInstance.getValue(i,j)));
                }
                entries.add(newButton);
            }
        }
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        this.gameInstance=(MineSearchGame)savedInstanceState.getSerializable("SavedGameState");
        this.numColums=this.gameInstance.getGridSize();
        this.checkTime=savedInstanceState.getBoolean("check_time");
        this.time_counter=savedInstanceState.getInt("time_counter");
        if(this.checkTime){
            createCountDownTimer();
        }else{
            createSimpleTimer();
        }
        TextView undiscovered_view = (TextView)findViewById(R.id.undiscovered_text);
        undiscovered_view.setText(String.valueOf(this.gameInstance.getUndiscoveredCount())+" casillas por descubrir");
        startNewEntries();
        startGridView();
    }

    private void setDayHourData(Intent data){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        data.putExtra("DayHourData",dateFormat.format(calendar.getTime()));
    }
}
