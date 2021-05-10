package com.example.buscaminas;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
        int x=i/this.gameInstance.getGridSize();
        int y=i%this.gameInstance.getGridSize();
        View newView = this.inflater.inflate(R.layout.button_layout,null);
        Button actualButton = newView.findViewById(R.id.used_button);
        if(!this.gameInstance.isDiscovered(x,y)){
            actualButton.setBackgroundResource(R.drawable.rectangle);
        }else{
            actualButton.setBackgroundResource(R.drawable.rectangleshowed);
            actualButton.setGravity(Gravity.CENTER);
            actualButton.setText(String.valueOf(this.gameInstance.getValue(x,y)));
        }
        actualButton.setOnClickListener(new GridCellOnClickListener((Activity)this.context,this.gameInstance,this,this.timer,i,this.maxTime));
        return actualButton;
    }
}
