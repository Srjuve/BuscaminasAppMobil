package com.example.buscaminas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameActivity extends Activity implements View.OnClickListener{
    MineSearchGame gameInstance;
    int numColums;
    //List<TextView> entries;
    boolean checkTime;
    List<Button> entries;
    GridView gridv;
    CustomButtonAdapter gridAdapter;
    CountDownTimer timer;
    int time_counter;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        if(savedInstanceState==null) {
            Intent data = getIntent();
            this.numColums = data.getIntExtra("gridsize", 5);
            this.gameInstance = createGameInstance(data);
            this.checkTime=data.getBooleanExtra("timeControl",false);
            this.time_counter=30;
            if(this.checkTime){
                createCountDownTimer();
            }else{
                TextView time_view = (TextView)findViewById(R.id.time_text);
                time_view.setText("Sin control de tiempo");
            }
            TextView undiscovered_view = (TextView)findViewById(R.id.undiscovered_text);
            undiscovered_view.setText(String.valueOf(this.gameInstance.getUndiscoveredCount())+" casillas por descubrir");
            startNewEntries();
            startGridView();
        }

    }

    private void createCountDownTimer(){
        timer=new CountDownTimer(this.time_counter*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TextView time_view = (TextView)findViewById(R.id.time_text);
                time_view.setText(String.valueOf(time_counter)+" Secs");
                time_counter-=1;
            }

            @Override
            public void onFinish() {
                finish();
            }
        }.start();
    }

    private void startGridView(){
        this.gridv = findViewById(R.id.game_grid);
        this.gridv.setNumColumns(this.numColums);
        this.gridAdapter = new CustomButtonAdapter(this, this.entries);
        this.gridv.setAdapter(gridAdapter);
    }

    private MineSearchGame createGameInstance(Intent data){
        MineSearchGame searchGame = new MineSearchGame(data.getIntExtra("gridsize",5),data.getStringExtra("alias"),data.getIntExtra("minePercentage",15));
        return searchGame;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("SavedGameState",this.gameInstance);
        savedInstanceState.putInt("time_counter",this.time_counter);
        savedInstanceState.putBoolean("check_time",this.checkTime);
    }

    private void startNewEntries(){
        this.entries= new ArrayList<>();
        for(int i=0;i<this.numColums;i+=1){
            for(int j=0;j<this.numColums;j+=1){
                Button newButton = new Button(this);
                newButton.setTag(new Integer(i*this.numColums+j));
                if(!this.gameInstance.isDiscovered(i,j)){
                    newButton.setBackgroundResource(R.drawable.rectangle);
                }else{
                    newButton.setBackgroundResource(R.drawable.rectangleshowed);
                    newButton.setGravity(Gravity.CENTER);
                    newButton.setText(String.valueOf(this.gameInstance.getValue(i,j)));
                }
                newButton.setOnClickListener(this);
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
        }
        TextView undiscovered_view = (TextView)findViewById(R.id.undiscovered_text);
        undiscovered_view.setText(String.valueOf(this.gameInstance.getUndiscoveredCount())+" casillas por descubrir");
        startNewEntries();
        startGridView();
    }

    @Override
    public void onClick(View v) {
        int x=(Integer)v.getTag()/numColums;
        int y=(Integer)v.getTag()%numColums;
        Toast.makeText(this,String.valueOf(x) + ":" + String.valueOf(y),Toast.LENGTH_LONG).show();
        int result=this.gameInstance.discoverPosition(x,y);
        if(result!=-1 && result!=-2){
            Button clickedButton=(Button)v;
            clickedButton.setGravity(Gravity.CENTER);
            clickedButton.setText(String.valueOf(result));
            clickedButton.setBackgroundResource(R.drawable.rectangleshowed);
            TextView undiscovered_view = (TextView)findViewById(R.id.undiscovered_text);
            undiscovered_view.setText(String.valueOf(this.gameInstance.getUndiscoveredCount())+" casillas por descubrir");
            this.gridAdapter.notifyDataSetChanged();
            if(this.gameInstance.checkVictory()){
                finish();
            }
        }else if(result==-1){
            finish();
        }
    }
}
