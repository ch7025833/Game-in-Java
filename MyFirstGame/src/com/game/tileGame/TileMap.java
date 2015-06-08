package com.game.tileGame;

import com.game.reusable.graphics.Sprite;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by chenhao on 5/16/2015.
 */
public class TileMap {

    private Image[][] tiles;
    private LinkedList<Sprite> sprites;
    private Sprite player;

    public TileMap(int width,int height){
        tiles = new Image[width][height];
        sprites = new LinkedList<Sprite>();
    }

    public int getWidth(){
        return tiles.length;
    }

    public int getHeight(){
        return tiles[0].length;
    }

    public Image getTile(int x,int y){
        if ( x < 0 || x >= getWidth() || y < 0 || y >= getHeight()){
            return  null;
        }else {
            return tiles[x][y];
        }
    }

    public void setTiles(int x,int y, Image tile){
        tiles[x][y] = tile;
    }

    public Sprite getPlayer(){
        return player;
    }

    public void setPlayer(Sprite player){
        this.player = player;
    }

    public void addSprite(Sprite sprite){
        sprites.add(sprite);
    }

    public void removeSprite(Sprite sprite){
        sprites.remove(sprite);
    }

    public Iterator getSprites(){
        return sprites.iterator();
    }
}
