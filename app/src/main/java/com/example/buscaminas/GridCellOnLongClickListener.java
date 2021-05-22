package com.example.buscaminas;

import android.app.Activity;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;

public class GridCellOnLongClickListener implements View.OnLongClickListener {
    Activity actualContext;
    MineSearchGame gameInstance;
    CustomButtonAdapter gridAdapter;
    int position;
    int numColums;

    public GridCellOnLongClickListener(Activity actualContext, MineSearchGame gameInstance, CustomButtonAdapter gridAdapter,int position){
        this.actualContext=actualContext;
        this.gameInstance=gameInstance;
        this.numColums=gameInstance.getGridSize();
        this.position=position;
        this.gridAdapter=gridAdapter;
    }

    @Override
    public boolean onLongClick (View v){
        //On Long click the position is flagged
        int x=this.position/numColums;
        int y=this.position%numColums;
        this.gameInstance.changeFlag(x,y);
        this.gridAdapter.notifyDataSetChanged();
        return true;
    }
}
