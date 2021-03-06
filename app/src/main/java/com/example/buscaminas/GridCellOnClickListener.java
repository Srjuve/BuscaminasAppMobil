package com.example.buscaminas;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;

public class GridCellOnClickListener implements View.OnClickListener{
    Activity actualContext;
    MineSearchGame gameInstance;
    int numColums;
    int minePercentage;
    String alias;
    CustomButtonAdapter gridAdapter;
    CountDownTimer timer;
    Timer timerFalse;
    int maxTime;
    int num_mines;
    int position;

    public GridCellOnClickListener(Activity actualContext, MineSearchGame gameInstance,CustomButtonAdapter gridAdapter, Object timer, int position, int maxTime){
        this.actualContext=actualContext;
        this.gameInstance=gameInstance;
        this.alias=gameInstance.getUserAlias();
        this.numColums=gameInstance.getGridSize();
        this.minePercentage=gameInstance.getMinePercentage();
        this.num_mines=gameInstance.getNumberOfMines();
        this.maxTime=maxTime;
        this.position=position;
        this.gridAdapter=gridAdapter;
        if(timer instanceof CountDownTimer){
            this.timer=(CountDownTimer)timer;
        }else if(timer instanceof Timer){
            this.timerFalse = (Timer)timer;
        }
    }

    @Override
    public void onClick(View v) {
        //Check if the clicked position is a bomb or not and make the necessary changes
        int x=this.position/numColums;
        int y=this.position%numColums;
        int result=this.gameInstance.discoverPosition(x,y);
        if(result!=-1 && result!=-2){
            changeCellsCountState(v);
            this.gridAdapter.notifyDataSetChanged();
            if(this.gameInstance.checkVictory()){
                cancelTimer();
                Intent data = initEndIntent();
                wonGameExit(v,data);
                startEndActivity(data);
            }
        }else if(result==-1){
            cancelTimer();
            this.gridAdapter.notifyDataSetChanged();
            MediaPlayer player = MediaPlayer.create(actualContext,R.raw.explosion);
            player.start();
            Intent data = initEndIntent();
            lostGameExit(v,data,x,y);
            startEndActivity(data);
        }
    }

    private Intent initEndIntent(){
        Intent data = new Intent(this.actualContext,EndInfoActivity.class);
        data.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        setDayHourData(data);
        return data;
    }

    private void wonGameExit(View v, Intent data){
        //Prepare the data used if the user wins the game
        createToast("GAME OVER... BIEN HECHO, HAS GANADO !!");
        TextView time_value_view = (TextView)v.getRootView().findViewById(R.id.time_text);
        int time_counter = Integer.parseInt(time_value_view.getText().toString());
        if(timer!=null) {
            data.putExtra("LogData", "Alias: " + this.alias + " Casillas: " + String.valueOf(this.minePercentage) + "% Minas: " + String.valueOf(this.num_mines) + " Tiempo Total: " + String.valueOf(this.maxTime - time_counter) + " Has ganado!! Te han sobrado " + String.valueOf(time_counter) + " Secs");
        }else{
            data.putExtra("LogData", "Alias: " + this.alias + " Casillas: " + String.valueOf(this.minePercentage) + "% Minas: " + String.valueOf(this.num_mines) + " Tiempo Total: " + String.valueOf(this.maxTime - time_counter) + " Has ganado!!");
        }
    }

    private void lostGameExit(View v,Intent data,int x,int y){
        //Prepare the data used if the user loses the game
        createToast("GAME OVER... MALA SUERTE, HAS PERDIDO !!");
        TextView time_value_view = (TextView)v.getRootView().findViewById(R.id.time_text);
        int time_counter = Integer.parseInt(time_value_view.getText().toString());
        data.putExtra("LogData","Alias: "+this.alias+" Casillas: "+ String.valueOf(this.minePercentage)+"% Minas: "+ String.valueOf(this.num_mines) +" Tiempo Total: "+String.valueOf(this.maxTime - time_counter)+ " Has perdido!! Bomba en casilla "+ String.valueOf(x)+","+String.valueOf(y)+" Te han quedado "+ this.gameInstance.getUndiscoveredCount() +" casillas por descubrir");
    }

    private void createToast(String message){
        LayoutInflater inflater = this.actualContext.getLayoutInflater();
        View layout = inflater.inflate(R.layout.endtoast,(ViewGroup)this.actualContext.findViewById(R.id.toast_layout));
        TextView text = layout.findViewById(R.id.toast_text_id);
        text.setText(message);
        Toast toast = new Toast(this.actualContext);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    private void cancelTimer(){
        if(this.timer!=null) {
            this.timer.cancel();
        }else{
            this.timerFalse.cancel();
        }
    }

    private void startEndActivity(Intent data){
        this.actualContext.startActivity(data);
        this.actualContext.finish();
    }

    private void changeCellsCountState(View v){
        TextView undiscovered_view = (TextView)v.getRootView().findViewById(R.id.undiscovered_text);
        undiscovered_view.setText(String.valueOf(this.gameInstance.getUndiscoveredCount())+" casillas por descubrir");
    }

    private void setDayHourData(Intent data){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        data.putExtra("DayHourData",dateFormat.format(calendar.getTime()));
    }

}
