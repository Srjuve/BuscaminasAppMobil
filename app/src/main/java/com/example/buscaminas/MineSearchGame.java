package com.example.buscaminas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MineSearchGame implements Serializable {
    private int[][] layout;
    private boolean[][] discoveredLayout;
    private boolean[][] flagged;
    private String userAlias;
    private int minePercentage;
    private int gridSize;
    private int numberOfMines;

    public MineSearchGame(int gridSize,String alias,int minePercentage){
        this.layout = new int[gridSize][gridSize];
        this.discoveredLayout = new boolean[gridSize][gridSize];
        this.flagged = new boolean[gridSize][gridSize];
        this.gridSize=gridSize;
        this.minePercentage=minePercentage;
        this.numberOfMines=(gridSize*gridSize*minePercentage)/100;
        this.userAlias=alias;
        fillLayout();
    }

    public int getUndiscoveredCount(){
        int count=0;
        for(int i=0;i<this.layout.length;i+=1){
            for(int j=0;j<this.layout[0].length;j+=1){
                if(this.layout[i][j]!=-1){
                    if(!this.discoveredLayout[i][j])
                        count+=1;
                }
            }
        }
        return count;
    }

    public int getGridSize(){
        return this.gridSize;
    }
    public String getUserAlias(){return this.userAlias;}
    public int getMinePercentage() {return this.minePercentage;}
    public int getNumberOfMines() {return this.numberOfMines;}
    private void fillLayout(){
        int maxSize=this.layout.length*this.layout.length;
        List<Integer> mines = createRandomPositions(maxSize,this.numberOfMines);
        for(int i=0;i<this.layout.length;i+=1){
            for(int j=0;j<this.layout[0].length;j+=1){
                int position=(i*this.layout.length+j);
                if(mines.contains(new Integer(position))){
                    this.layout[i][j]=-1;
                    updateNeighConter(i,j);
                }
                this.discoveredLayout[i][j]=false;
            }

        }

    }

    private List<Integer> createRandomPositions(int maxSize, int numberOfMines){
        List<Integer> positions = new ArrayList<>();
        Random random = new Random();
        while(positions.size()!=numberOfMines){
            Integer value=new Integer(random.nextInt(maxSize-0)+0);
            if(!positions.contains(value))
                positions.add(value);
        }
        return positions;
    }

    public boolean isFlagged(int x, int y){
        return this.flagged[x][y];
    }
    public boolean isBomb(int x, int y){
        return this.layout[x][y] == -1;
    }

    public boolean isDiscovered(int x, int y){
        return this.discoveredLayout[x][y];
    }

    public int getValue(int x,int y){
        return this.layout[x][y];
    }

    private void updateNeighConter(int x,int y){
        if(x>0){
            if(y>0)
                addCounterPosition(x-1,y-1);
            if(y<this.layout[0].length-1)
                addCounterPosition(x-1,y+1);
            addCounterPosition(x-1,y);
        }
        if(y>0)
            addCounterPosition(x,y-1);
        if(y<this.layout[0].length-1)
            addCounterPosition(x,y+1);
        if(x<this.layout.length-1){
            if(y>0)
                addCounterPosition(x+1,y-1);
            if(y<this.layout[0].length-1)
                addCounterPosition(x+1,y+1);
            addCounterPosition(x+1,y);
        }
    }

    private void addCounterPosition(int x, int y){
        if(this.layout[x][y]!=-1)
            this.layout[x][y]+=1;
    }

    public boolean checkVictory(){
        for(int i=0;i<this.discoveredLayout.length;i+=1){
            for(int j=0;j<this.discoveredLayout[0].length;j+=1){
                if(this.layout[i][j]!=-1 && !this.discoveredLayout[i][j])
                    return false;
            }
        }
        return true;
    }

    public void changeFlag(int x, int y){
        this.flagged[x][y]=!this.flagged[x][y];
    }

    public int discoverPosition(int x,int y){
        if(this.discoveredLayout[x][y]){
            return -2;
        }
        this.discoveredLayout[x][y]=true;
        return this.layout[x][y];
    }

}
