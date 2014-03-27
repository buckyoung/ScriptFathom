package edu.pitt.bcy3.scriptfathom;

import android.graphics.Canvas;

import java.util.ArrayList;

/**
 * Created by BuckYoung on 1/31/14.
 */
public class PointsCache {

    private static float MAXSCALE = (float) 254.0;

    private static int canvasWidth = 0;
    private static int canvasHeight = 0;
    private static ArrayList<Integer> pCache = new ArrayList<Integer>();

    //Add point to cache -- scale down first!
    public static void add(float x, float y){
        int xadd =(int)((x/canvasWidth)*MAXSCALE);
        int yadd =(int)((y/canvasHeight)*MAXSCALE);
        pCache.add(xadd);
        pCache.add(yadd);
    }
    public static void endStroke(){
        pCache.add(255);
        pCache.add(0);
    }

    //Empty cache
    public static void clear(){
        pCache = new ArrayList<Integer>();
    }

    //Add end encoding
    public static void prepareSend(){
        pCache.add(255);
        pCache.add(255);
    }

    //Set Canvas Width and Height
    public static void setCanvasDimensions(int w, int h){
        canvasWidth = w;
        canvasHeight = h;
    }

    //For sending HTTP Post
    public static String stringOut(){
        return pCache.toString();
    }

}
