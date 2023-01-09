package com.example.imag;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WhiteBoard extends View {

    private Bitmap bitmap;
    private Canvas canvas;

    public WhiteBoard(Context context,AttributeSet attrs) {//Create the whiteboard and set the basic color
        super(context, attrs);
        init();
    }

    private ConcurrentLinkedQueue<Map.Entry<Path,  Paint>> mPaths = new ConcurrentLinkedQueue<Map.Entry<Path, Paint>>();
    // The Linked Queue of mPaths is used so it is posible to make diferent 'lines' of diferents colors in the same canvas

    private Path mCurrentPath = null;
    private Paint currentPaint = null;
    private Path lastPath = null;
    private Paint lastPaint = null;
    boolean callOnDraw=false;

    private void init(){
        mCurrentPath = new Path();
        currentPaint=new Paint();
        currentPaint.setAntiAlias(true);
        currentPaint.setColor(DrawingColors.BLACK.getColorValue());
        currentPaint.setStrokeJoin(Paint.Join.ROUND);
        currentPaint.setStyle(Paint.Style.STROKE);
        currentPaint.setStrokeWidth(5f);
        mPaths.add(new AbstractMap.SimpleImmutableEntry<Path, Paint>(mCurrentPath, currentPaint));
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // your Canvas will draw onto the defined Bitmap
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) { //function of the characteristics of the line
        super.onDraw(canvas);
        if(callOnDraw){ //To check if able/ disable to draw
            for (Map.Entry<Path,Paint> entry : mPaths) {
                canvas.drawPath(entry.getKey(), entry.getValue());
            }
            //It show all the 'lines' made until de moment
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {    //function of the path's made by the user

        if(callOnDraw){ //To check if able / disable to draw

            float xPos= event.getX();
            float yPos=event.getY();

            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    mCurrentPath.moveTo(xPos,yPos);
                    return true;

                case MotionEvent.ACTION_MOVE:
                    mCurrentPath.lineTo(xPos,yPos);
                    break;

                case MotionEvent.ACTION_UP:
                    break;

                default:
                    return false;
            }
        }
        invalidate();
        return true;
    }

    public void clearCanvas() {//To clear the canvas
        int j=mPaths.size()-1;
        int l=0;
        for (Map.Entry<Path,Paint> entry : mPaths) {
            if (l==j){
                lastPaint=entry.getValue();
                lastPath=entry.getKey();
            }
            entry.getKey().reset();
            mPaths.remove(entry);
            l++;


        }
        mPaths.add(new AbstractMap.SimpleImmutableEntry<Path, Paint>(lastPath, lastPaint));
        lastPath=null;
        lastPaint=null;
        invalidate();
    }

    public void draw_available(int mode){ //Method to make the canvas able or disable to draw in it

        //1= it's able to draw ** 0 = it's not able to draw
        if (mode==1){
            callOnDraw=true;
        }
        else{
            callOnDraw=false;
        }

    }




    public enum DrawingColors{//The 'List' of colors
        BLACK(Color.parseColor("#000000")),  BLUE(Color.parseColor("#0000FF")),  RED(Color.parseColor("#FF0000")),
        YELLOW(Color.parseColor("#FFD801")), PURPLE(Color.parseColor("#800080")), GREEN(Color.parseColor("#01C501")),
        ORANGE(Color.parseColor("#FFA500")), COFEE(Color.parseColor("#D2691E")), MAGENTA(Color.parseColor("#FF00FF")),
        LIGHT_BLUE(Color.parseColor("#14E1E1")), LIGHT_BROWN(Color.parseColor("#D6AB8B")) ,WHITE(Color.parseColor("#FFFFFF"));

        private int colorValue;
        private DrawingColors(final int color) {
            colorValue = color;
        }
        int getColorValue() {//To get the int value of especific color
            return colorValue;
        }
    }

    public void setColor(final int color ) {
        //To set a new color you have to create a new set of path and paint so it doesn't overwrite the others
        // Then you have to add that new set to the mPaths
        mCurrentPath = new Path();
        currentPaint =new Paint();
        currentPaint.setAntiAlias(true);
        currentPaint.setColor(color);
        if (color ==-1){
            currentPaint.setStrokeJoin(Paint.Join.ROUND);
            currentPaint.setStyle(Paint.Style.STROKE);
            currentPaint.setStrokeWidth(9f);
        }
        else{
            currentPaint.setStrokeJoin(Paint.Join.ROUND);
            currentPaint.setStyle(Paint.Style.STROKE);
            currentPaint.setStrokeWidth(5f);
        }

        mPaths.add(new AbstractMap.SimpleImmutableEntry<Path, Paint>(mCurrentPath, currentPaint));


    }
}
