package com.example.buscaminas;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomButtonAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    MineSearchGame gameInstance;
    Object timer;
    int maxTime;
    public CustomButtonAdapter(Context applicationContext,MineSearchGame gameInstance,Object timer, int maxTime){
        this.context=applicationContext;
        this.gameInstance=gameInstance;
        this.timer=timer;
        this.maxTime=maxTime;
        inflater=(LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount(){
        return this.gameInstance.getGridSize()*this.gameInstance.getGridSize();
    }

    @Override
    public Object getItem(int i){
        return null;
    }

    @Override
    public long getItemId(int i){
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        //We create the needed buttons
        int x=i/this.gameInstance.getGridSize();
        int y=i%this.gameInstance.getGridSize();
        Button actualButton;
        if(view==null) {
            actualButton = new Button(context);
            actualButton.setLayoutParams(new ViewGroup.LayoutParams(GridView.LayoutParams.WRAP_CONTENT, 50));
            actualButton.setPadding(8, 8, 8, 8);
        }else{
            actualButton = (Button)view;
        }
        if(!this.gameInstance.isDiscovered(x,y)){
            if(this.gameInstance.isFlagged(x,y)){
                actualButton.setBackgroundResource(R.drawable.rectangle_flag);
            }else {
                actualButton.setBackgroundResource(R.drawable.rectangle);
            }
        }else{
            if(this.gameInstance.isBomb(x,y)){
                actualButton.setBackgroundResource(R.drawable.bomb);
                actualButton.setGravity(Gravity.CENTER);
            }else {
                actualButton.setBackgroundResource(R.drawable.rectangleshowed);
                actualButton.setGravity(Gravity.CENTER);
                actualButton.setText(String.valueOf(this.gameInstance.getValue(x, y)));
            }
        }
        actualButton.setOnClickListener(new GridCellOnClickListener((Activity)this.context,this.gameInstance,this,this.timer,i,this.maxTime));
        actualButton.setOnLongClickListener(new GridCellOnLongClickListener((Activity)this.context,this.gameInstance,this,i));
        actualButton.setId(i);
        return actualButton;
    }
}
