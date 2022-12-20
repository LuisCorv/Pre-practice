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
import android.widget.Switch;

import androidx.annotation.Nullable;

import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WhiteBoard extends View {

    Paint paint;
    Path path;
    private Bitmap bitmap;
    private Canvas canvas;

    public WhiteBoard(Context context,AttributeSet attrs) {//Create the whiteboard and set the basic color
        super(context, attrs);
        init();
        /*
        paint=new Paint();
        path=new Path();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);*/
    }

    private ConcurrentLinkedQueue<Map.Entry<Path,  Paint>> mPaths = new ConcurrentLinkedQueue<Map.Entry<Path, Paint>>();
    // The Linked Queue of mPaths is used so it is posible to make diferent 'lines' of diferents colors in the same canvas

    private Path mCurrentPath = null;
    private Paint currentPaint = null;

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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawPath(path,paint);
        for (Map.Entry<Path,Paint> entry : mPaths) {
            canvas.drawPath(entry.getKey(), entry.getValue());
        }
        //It show all the 'lines' made until de moment
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
        invalidate();
        return true;

    }

    public void clearCanvas() {//To clear the canvas
        for (Map.Entry<Path,Paint> entry : mPaths) {
            entry.getKey().reset();
        }
        invalidate();
    }


    public enum DrawingColors{//The 'List' of colors
        BLACK(Color.parseColor("#000000")),  BLUE(Color.parseColor("#0000FF")),  RED(Color.parseColor("#FF0000")),
        YELLOW(Color.parseColor("#FFD801")), PURPLE(Color.parseColor("#800080")), GREEN(Color.parseColor("#01C501")),
        ORANGE(Color.parseColor("#FFA500")), COFEE(Color.parseColor("#D2691E")), MAGENTA(Color.parseColor("#FF00FF")),
        LIGHT_BLUE(Color.parseColor("#14E1E1")), LIGHT_BROWN(Color.parseColor("#D6AB8B"))
        ;

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
        currentPaint.setStrokeJoin(Paint.Join.ROUND);
        currentPaint.setStyle(Paint.Style.STROKE);
        currentPaint.setStrokeWidth(5f);
        mPaths.add(new AbstractMap.SimpleImmutableEntry<Path, Paint>(mCurrentPath, currentPaint));


    }
}
