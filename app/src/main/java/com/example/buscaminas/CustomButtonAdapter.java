package com.example.buscaminas;

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
    //List<TextView> buttons;
    List<Button> buttons;
    LayoutInflater inflater;
    public CustomButtonAdapter(Context applicationContext, List<Button> buttons){
        this.context=applicationContext;
        this.buttons=buttons;
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
        return buttons.get(i);
    }
}
