package com.example.imag;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    WhiteBoard canvas;

    // Add the wanted image to the folder drawable of this app ( imag\app\src\main\res\drawable )

    //The list of the images ID's
    ArrayList <Integer> imagess = new ArrayList<>();
    //The list of the images names
    ArrayList <String> imagess_name =new ArrayList<>();

    //Maximum and minimum values of the number of pictures in the array
    int min = 0;
    int max;

    int counter;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        imageView=findViewById(R.id.imageView);
        loadDrawables(R.drawable.class);
        max = imagess.size();
        counter= new Random().nextInt((max - min) ) + min;
        imageView.setImageResource(imagess.get(counter));

        canvas=(WhiteBoard) findViewById(R.id.signature_canvas);
        change_mode(0);

    }

    private void loadDrawables(Class<?> clz){//Method to load all the pictures (.jpg or .png) in drawable folder
        imagess.clear();
        final Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            final int drawableId;
            try {
                drawableId = field.getInt(clz);

                //To get the type of the file
                TypedValue value = new TypedValue();
                getResources().getValue(drawableId, value, true);
                String filename =value.string.toString();
                String extension=filename.substring(filename.lastIndexOf("."));
                //if .png or .jpg, we add the file to imagess
                if(extension.equals(".png")||extension.equals(".jpg")){
                    imagess.add(drawableId);

                    String file_name=filename.substring(filename.lastIndexOf("/")+1);
                    imagess_name.add(file_name.substring(0,file_name.length()-4));
                }
            } catch (Exception e) {
                continue;
            }

        }
    }

    public void Save (View v){
        //Method to save the draw
        //♦♦♦ Confirm that the device have allowed the app to have access to the storage
        canvas.setDrawingCacheEnabled(true);
        String imgSave = MediaStore.Images.Media.insertImage(
                getContentResolver(),canvas.getDrawingCache(), imagess_name.get(counter)+"_v"+ UUID.randomUUID().toString()+".jpg","drawing"  );
        if(imgSave != null){
            Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Error, image not saved",Toast.LENGTH_SHORT).show();
        }

        canvas.destroyDrawingCache();
    }



    public void Next(View v){//Method to go to the next picture

        // Method to make a random number
        counter= new Random().nextInt((max - min) ) + min;

        //To print the random number of the counter
        //Toast.makeText(getApplicationContext(),"El numero es: " + imagess_name.get(counter) ,Toast.LENGTH_SHORT).show();

        change_mode(0);
        //To change the image to the image with the number of counter
        //imageView.setImageResource(imagess.get(counter));

        //To clear the whiteboard
        canvas.clearCanvas();


    }

    public void Reset(View v){//Method to reset the canvas
        canvas.clearCanvas();
    }


    public void change_mode(int mode_change){
        if(mode_change==1){ //Draw Mode
            imageView.setImageResource(R.drawable.border);
            canvas.draw_available(1);

        }
        else if(mode_change==0){ //Show Image Mode
            imageView.setImageResource(imagess.get(counter));
            canvas.draw_available(0);

            //To make the delay between 'modes'
            Runnable runnable= new Runnable() {
                @Override
                public void run() {
                    change_mode(1);
                }
            };
            Handler handler =new Handler(Looper.getMainLooper());
            handler.postDelayed(runnable, 5000);
        }
        else{
            imageView.setImageResource(imagess.get(counter));
            canvas.draw_available(0);
        }
    }

    public void Draw_in_canvas(View v){
            change_mode(1);
    }

    public void Show_image(View v){
            change_mode(2);
    }


    //The buttons fuctions to set the 'pencil' color
    public void Black_set(View v){
        canvas.setColor(WhiteBoard.DrawingColors.BLACK.getColorValue());
    }
    public void Blue_set(View v){
        canvas.setColor(WhiteBoard.DrawingColors.BLUE.getColorValue());
    }
    public void Red_set(View v){
        canvas.setColor(WhiteBoard.DrawingColors.RED.getColorValue());
    }
    public void Yellow_set(View v){
        canvas.setColor(WhiteBoard.DrawingColors.YELLOW.getColorValue());
    }
    public void Purple_set(View v){
        canvas.setColor(WhiteBoard.DrawingColors.PURPLE.getColorValue());
    }
    public void Green_set(View v){
        canvas.setColor(WhiteBoard.DrawingColors.GREEN.getColorValue());
    }
    public void Orange_set(View v){
        canvas.setColor(WhiteBoard.DrawingColors.ORANGE.getColorValue());
    }
    public void Cofee_set(View v){
        canvas.setColor(WhiteBoard.DrawingColors.COFEE.getColorValue());
    }
    public void Light_Blue_set(View v){
        canvas.setColor(WhiteBoard.DrawingColors.LIGHT_BLUE.getColorValue());
    }
    public void Magenta_set(View v){
        canvas.setColor(WhiteBoard.DrawingColors.MAGENTA.getColorValue());
    }
    public void Light_Brown_set(View v){
        canvas.setColor(WhiteBoard.DrawingColors.LIGHT_BROWN.getColorValue());
    }
    public void White_set(View v){
        canvas.setColor(WhiteBoard.DrawingColors.WHITE.getColorValue());
    }
}


    
