package com.game.tileGame;

import com.game.reusable.graphics.Sprite;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by chenhao on 5/16/2015.
 */
public class ResourceManager {

    private ArrayList<Image> tiles;


    public ResourceManager(){
        tiles = new ArrayList<Image>();
    }


    private TileMap loadMap(String fileName) throws IOException {

        ArrayList<String> lines = new ArrayList<String>();
        int width = 0;
        int height = 0;

        //read every line in the text file into the list.
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = null;
            while ((line = reader.readLine()) != null){
                if (!line.startsWith("#")){
                    lines.add(line);
                    width = Math.max(width,line.length());
                }
            }
        }finally {
            reader.close();
        }

        //parse the lines to create a TileEngine
        height = lines.size();
        TileMap newMap = new TileMap(width,height);
        for (int y = 0; y < height; y++){
            String line = (String)lines.get(y);
            for (int x = 0; x < line.length(); x++){
                char ch = line.charAt(x);

                //check if the char represents tile A, B, C, etc.
                int tileIndex = ch - 'A';
                if (tileIndex >= 0 && tileIndex < tiles.size()){
                    newMap.setTiles(x,y,tiles.get(tileIndex));
                }

                //check if the char represents a sprite
                else if(ch == 'o'){
                    addSprite(newMap, coinSprite, x, y);
                }else if (ch == '!'){
                    addSprite(newMap, musicSprite, x, y);
                }else if (ch == '*'){
                    addSprite(newMap, goalSprite, x, y);
                }else if (ch == '1'){
                    addSprite(newMap, grubSprite, x, y);
                }else if (ch == '2'){
                    addSprite(newMap, flySprite, x, y);
                }
            }
        }

        //add the player to the map
        Sprite player = (Sprite)playerSprite.clone();
        player.setX(TileMapRenderer.tilesToPixels(3));
        player.setY(0);
        newMap.setPlayer(player);

        return newMap;
    }

    private void addSprite(TileMap tileMap,Sprite hostSprite,int tileX,int tileY){

        if (hostSprite != null){
            //clone the sprite from the host
            try {
                Sprite sprite = hostSprite.clone();
            } catch (CloneNotSupportedException e) {
            }
            //center the sprite
            hostSprite.setX(TileMapRenderer.tilesToPixels(tileX) + (TileMapRenderer.tilesToPixels(1) - hostSprite.getWidth()) / 2 );

            //bottom-justify the sprite
            hostSprite.setY(TileMapRenderer.tilesToPixels(tileY + 1) - hostSprite.getHeight());

            //add it to the map
            tileMap.addSprite(hostSprite);
        }
    }
}
