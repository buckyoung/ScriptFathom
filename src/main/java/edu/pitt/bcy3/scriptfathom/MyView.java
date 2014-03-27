package edu.pitt.bcy3.scriptfathom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BuckYoung on 1/28/14.
 */
public class MyView extends View {

    private Path drawPath;
    private static Paint drawPaint;
    private static int paintColor;
    public static MainActivity mainAct;


    public MyView(Context context) {
        this(context, null, 0);
    }

    public MyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDrawing();
    }

    public static void setMA(MainActivity ma){
        mainAct = ma;
    }

    // Canvas and Paint initialization
    private void initDrawing(){
        drawPath = new Path();
        drawPaint = new Paint();
        paintColor = Color.BLACK;
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.SQUARE);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        PointsCache.clear();//Clear cache (everythings changed!)
        PointsCache.setCanvasDimensions(w, h); //SET canvas size

    }

    @Override
    protected void onDraw(Canvas canvas) {

        //Draw the path
        canvas.drawPath(drawPath, drawPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                PointsCache.add(x,y);
                drawPath.moveTo(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                PointsCache.add(x,y);
                drawPath.lineTo(x,y);
                break;
            case MotionEvent.ACTION_UP:
                PointsCache.add(x,y);
                PointsCache.endStroke();//end stroke
                drawPath.lineTo(x,y);
                break;
            default:
                return false;
        }
        //redraw
        invalidate();
        return true;

    }

    public static void setPaintColor(int color){
      drawPaint.setColor(color);

     if(mainAct != null){
            mainAct.setButtonColor(color);
     }

    }

    public void clear(){
        PointsCache.clear();
        drawPath.reset();
        if(mainAct != null){
            mainAct.setResponse("null");
            mainAct.setTrashNormal();
        }


        invalidate();
    }

    public void send(){

        mainAct.setSending();
        new Send().execute();
    }

    private class Send extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            PointsCache.prepareSend();


            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://cwritepad.appspot.com/reco/usen");

            List<NameValuePair> nvp = new ArrayList<NameValuePair>(2);
            nvp.add(new BasicNameValuePair("key", "11773edfd643f813c18d82f56a8104ed"));
            nvp.add(new BasicNameValuePair("q", PointsCache.stringOut()));

            try {
                post.setEntity(new UrlEncodedFormEntity(nvp));
                HttpResponse httpResponse = client.execute(post);
                InputStreamReader stream = new InputStreamReader(httpResponse.getEntity().getContent());
                BufferedReader reader = new BufferedReader(stream);
                final String response = reader.readLine();


                mainAct.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainAct.setResponse(response);
                    }
                });


            } catch (UnsupportedEncodingException e) {
                Log.d("HTTP Error", e.toString());
            } catch (IOException e) {
                Log.d("HTTP Error", e.toString());
            }

            return null;
        }

    }
}



