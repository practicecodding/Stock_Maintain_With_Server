package com.hamidul.stockmaintain;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class SplashScreen extends AppCompatActivity {
    CardView logo;
    TextView name;
    Animation Splash_top,Splash_bottom;
    public static boolean onSplash = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_splash_screen);

        logo = findViewById(R.id.logo);
        name = findViewById(R.id.name);

        Splash_top = AnimationUtils.loadAnimation(this, R.anim.splash_top);
        Splash_bottom = AnimationUtils.loadAnimation(this, R.anim.splash_bottom);

        logo.setAnimation(Splash_top);
        name.setAnimation(Splash_bottom);

        if (onSplash){
            startActivity(new Intent(SplashScreen.this,MainActivity.class));
            finish();
        }else {
            Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreen.this,MainActivity.class));
                    finish();
                }
            },1500);
        }



    }



}