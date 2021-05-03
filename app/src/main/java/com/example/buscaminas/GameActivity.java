package com.example.buscaminas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class GameActivity extends Activity implements AdapterView.OnItemClickListener {
    MineSearchGame gameInstance;
    int numColums;
    List<TextView> entries;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        if(savedInstanceState==null) {
            Intent data = getIntent();
            this.numColums = data.getIntExtra("gridsize", 5);
            this.gameInstance = createGameInstance(data);
            startNewEntries();
            startGridView();
        }

    }

    private void startGridView(){
        GridView gridv = findViewById(R.id.game_grid);
        gridv.setNumColumns(this.numColums);
        CustomButtonAdapter gridAdapter = new CustomButtonAdapter(this, this.entries);
        gridv.setAdapter(gridAdapter);
        gridv.setOnItemClickListener(this);
    }

    private MineSearchGame createGameInstance(Intent data){
        MineSearchGame searchGame = new MineSearchGame(data.getIntExtra("gridsize",5),data.getStringExtra("alias"),data.getIntExtra("minePercentage",15));
        return searchGame;
    }

    private void fillButtons(List<TextView> entries, int gridsize){
        for(int i=0;i<gridsize*gridsize;i+=1){
            TextView example = new TextView(this);
            example.setBackgroundResource(R.drawable.rectangle);
            entries.add(example);
        }
    }
    @Override
    public void onItemClick (AdapterView<?> listv, View selectedView, int position, long id) {
        int x=position/numColums;
        int y=position%numColums;
        Toast.makeText(this,String.valueOf(x) + ":" + String.valueOf(y),Toast.LENGTH_LONG).show();
        int result=this.gameInstance.discoverPosition(x,y);
        if(result!=-1 && result!=-2){
            TextView clickedButton=(TextView) listv.getItemAtPosition(position);
            clickedButton.setGravity(Gravity.CENTER);
            clickedButton.setText(String.valueOf(result));
            clickedButton.setBackgroundResource(R.drawable.rectangleshowed);
            if(this.gameInstance.checkVictory()){
                finish();
            }
        }else if(result==-1){
            finish();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("SavedGameState",this.gameInstance);
    }

    private void startNewEntries(){
        this.entries= new ArrayList<>();
        for(int i=0;i<this.numColums;i+=1){
            for(int j=0;j<this.numColums;j+=1){
                TextView newText = new TextView(this);
                if(!this.gameInstance.isDiscovered(i,j)){
                    newText.setBackgroundResource(R.drawable.rectangle);
                }else{
                    newText.setBackgroundResource(R.drawable.rectangleshowed);
                    newText.setGravity(Gravity.CENTER);
                    newText.setText(String.valueOf(this.gameInstance.getValue(i,j)));
                }
                entries.add(newText);
            }
        }
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        this.gameInstance=(MineSearchGame)savedInstanceState.getSerializable("SavedGameState");
        this.numColums=this.gameInstance.getGridSize();
        startNewEntries();
        startGridView();
    }

}
