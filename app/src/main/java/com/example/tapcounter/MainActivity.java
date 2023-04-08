package com.example.tapcounter;

import static android.widget.Toast.LENGTH_SHORT;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tapcounter.R;

public class MainActivity extends AppCompatActivity {
    private int count = 0;
    private int flag = 0;
    private TextView txt;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button decreaseButton = findViewById(R.id.decreaseCountBtn);
        Button resetButton = findViewById(R.id.resetBtn);
        Button lockButton = findViewById(R.id.lockBtn);
        txt = findViewById(R.id.countTxt);
        img = findViewById(R.id.imageView3);

        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (flag == 0) {
                    count++;
                    txt.setText(String.valueOf(count));
                    textAnimation();
                }
            }
        });

        decreaseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                count--;
                updateCountText();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                count = 0;
                updateCountText();
            }
        });

        lockButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch (flag) {
                    case 0:
                        flag = 1;
                        Toast.makeText(MainActivity.this, "Counter Locked", LENGTH_SHORT).show();
                        break;
                    case 1:
                        flag = 0;
                        Toast.makeText(MainActivity.this, "Counter Unlocked", LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void updateCountText() {
        StringBuilder builder = new StringBuilder();
        builder.append(count);
        txt.setText(builder.toString());
        textAnimation();
    }

    private void textAnimation(){
        // Create an ObjectAnimator to animate the scale of the TextView
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(txt, "scaleX", 0.7f, 1.0f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(txt, "scaleY", 0.7f, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator);
        animatorSet.start();

    }
}
