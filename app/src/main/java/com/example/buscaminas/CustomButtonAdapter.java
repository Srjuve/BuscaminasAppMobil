package com.example.buscaminas;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class CustomButtonAdapter extends BaseAdapter {
    Context context;
    List<Button> buttons;
    LayoutInflater inflater;
    MineSearchGame gameInstance;
    Object timer;
    int maxTime;
    public CustomButtonAdapter(Context applicationContext, List<Button> buttons,MineSearchGame gameInstance,Object timer, int maxTime){
        this.context=applicationContext;
        this.buttons=buttons;
        this.gameInstance=gameInstance;
        this.timer=timer;
        this.maxTime=maxTime;
        inflater=(LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount(){
        return buttons.size();
    }

    @Override
    public Object getItem(int i){
        return buttons.get(i);
    }

    @Override
    public long getItemId(int i){
        return buttons.get(i).getId();
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        Button actualButton = buttons.get(i);
        actualButton.setOnClickListener(new GridCellOnClickListener((Activity)this.context,this.gameInstance,this,this.timer,i,this.maxTime));
        return buttons.get(i);
    }
}
