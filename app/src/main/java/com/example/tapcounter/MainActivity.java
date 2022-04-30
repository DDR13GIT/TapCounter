package com.example.tapcounter;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.media.Image;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
int count =0;
int flag =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button decreaseButton = (Button)findViewById(R.id.decreaseCountBtn);
        Button resetButton = (Button)findViewById(R.id.resetBtn);
        Button lockButton = (Button)findViewById(R.id.lockBtn);
        TextView txt = (TextView) findViewById(R.id.countTxt);

        ImageView img = (ImageView) findViewById(R.id.imageView3);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(flag == 0) {
                count++;
                txt.setText(Integer.toString(count));}
            }
        });

        decreaseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                count--;
                txt.setText(Integer.toString(count));
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                count=0;
                txt.setText(Integer.toString(count));
            }
        });

        lockButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               if (flag == 0) {
                   flag=1;
                   Toast.makeText(MainActivity.this, "Counter Locked", LENGTH_SHORT).show();
               }
               else if (flag == 1) {
                   flag=0;
                   Toast.makeText(MainActivity.this, "Counter Unlocked", LENGTH_SHORT).show();
               }
            }
        });
    }
}


