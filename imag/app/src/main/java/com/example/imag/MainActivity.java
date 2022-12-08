package com.example.imag;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;

    boolean save_check =false; //To check if the draw has been save


    //int [] draws= {R.drawable.pato,R.drawable.gato,R.drawable.oso};
    // Add the wanted image to the folder drawable of this app ( imag\app\src\main\res\drawable )

    //The list of the images
    ArrayList <Integer> imagess = new ArrayList<>();


    private void loadDrawables(Class<?> clz){
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
                    }
            } catch (Exception e) {
                continue;
            }

        }
    }


    //Maximum and minimum values of the number of pictures in the array
    int min = 0;
    int max;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        imageView=findViewById(R.id.imageView);
        loadDrawables(R.drawable.class);
        max = imagess.size();
        final int counter= new Random().nextInt((max - min) ) + min;
        imageView.setImageResource(imagess.get(counter));

    }

    public void Save (View v){
        //Method to save the draw

        //♦♦How to save the canvas
        save_check=true;
    }



    public void Next(View v){
        //Method to go to the next picture

        // Method to make a random number
                //final int counter= new Random().nextInt((max - min) + 1) + min;
        final int counter= new Random().nextInt((max - min) ) + min;

        //To print the random number of the counter
        Toast.makeText(getApplicationContext(),"El numero es: " + counter ,Toast.LENGTH_SHORT).show();
        imageView.setImageResource(imagess.get(counter));
        //save_check=false;
        //♦♦ How to 'reset' the canvas
    }
}


    
