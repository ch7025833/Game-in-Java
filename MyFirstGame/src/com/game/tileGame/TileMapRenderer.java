package com.game.tileGame;

import com.game.reusable.graphics.Sprite;
import com.game.tileGame.Sprites.Creature;

import java.awt.*;
import java.util.Iterator;

/**
 * Created by chenhao on 5/17/2015.
 */
public class TileMapRenderer {

    private static final int TILE_SIZE = 64;
    //Math.pow(2,TILE_SIZE_BIT) == 64
    private static final int TILE_SIZE_BIT = 6;

    private Image backgroundImage;


    public static int pixelsToTiles(float pixels){
        return pixelsToTiles(Math.round(pixels));
    }

    public static int pixelsToTiles(int pixels){
        //shift also applies to the negative pixels
        return pixels >> TILE_SIZE_BIT;

        //for the tile size are not power of 2,can use the floor function
        //return (int)Math.floor((float)pixels / TILE_SIZE);
    }

    public static int tilesToPixels(int numTiles){
        return numTiles << TILE_SIZE_BIT;

        //if tile size is not power of 2
        //return numTiles * TILE_SIZE;
    }

    public void setBackgroundImage(Image image){
        this.backgroundImage = image;
    }

    public void draw(Graphics2D graphics2D,TileMap map,int screenWidth,int screenHeight){

        Sprite player = map.getPlayer();
        int mapWidth = tilesToPixels(map.getWidth());

        //get the scrolling position of the map based on the player's position,player is in the center of the screen
        //so the offsetX is negative and should have limits when player approaches the far left or right.
        int offsetX = screenWidth / 2 - TILE_SIZE - Math.round(player.getX());
        offsetX = Math.min(offsetX,0);
        offsetX = Math.max(offsetX,screenWidth - mapWidth);

        //get the y offset to draw all sprites and titles
        int offsetY = screenHeight - tilesToPixels(map.getHeight());

        //draw background if needed
        if ((backgroundImage == null || (screenHeight >= backgroundImage.getHeight(null)))){
            graphics2D.setColor(Color.black);
            graphics2D.fillRect(0,0,screenWidth,screenHeight);
        }

        //draw parallax background image
        if (backgroundImage != null){
            //x is compared with the ratio of the total map length with the total background image length.
            //map is longer than the background image
            int x = offsetX * ((screenWidth - backgroundImage.getWidth(null) / (screenWidth - mapWidth)));
            int y = screenHeight - backgroundImage.getHeight(null);

            graphics2D.drawImage(backgroundImage,x,y,null);
        }

        //draw the visible tiles
        int firstTileX = pixelsToTiles(-offsetX);
        int lastTileX = firstTileX + pixelsToTiles(screenWidth) + 1;
        for (int y = 0; y < map.getHeight(); y++){
            for (int x = firstTileX; x <= lastTileX; x++){
                Image image = map.getTile(x,y);
                if (image != null){
                    graphics2D.drawImage(image, tilesToPixels(x)+offsetX, tilesToPixels(y)+offsetY,null);
                }
            }
        }

        //draw the player
        graphics2D.drawImage(player.getImage(), Math.round(player.getX()) + offsetX,Math.round(player.getY()) + offsetY,null);

        //draw the sprites
        Iterator iterator = map.getSprites();
        while (iterator.hasNext()){
            Sprite sprite = (Sprite) iterator.next();
            int x = Math.round(sprite.getX()) + offsetX;
            int y = Math.round(sprite.getY()) + offsetY;
            graphics2D.drawImage(sprite.getImage(),x,y,null);

            //wake up the creature when it's on the screen;
            if (sprite instanceof Creature && x >= 0 && x < screenWidth){
                ((Creature)sprite).wakeUp();
            }
        }

    }
}
