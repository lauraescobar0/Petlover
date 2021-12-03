package com.example.petlover;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {

    ImageView logo,title,splash_img;
    LottieAnimationView lottieAnimationView;
    FirebaseAuth firebaseAuth;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        firebaseAuth= FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(firebaseAuth.getCurrentUser()!=null){ //Está logueado
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{ // No existe sesión en el dispositivo
                    Intent intent = new Intent(Splash.this, LoginInicial.class);
                    startActivity(intent);
                    finish();
                }
            }
        },4500);

        logo = findViewById(R.id.logo);
        title = findViewById(R.id.titulo);
        splash_img = findViewById(R.id.img);
        lottieAnimationView = findViewById(R.id.lottie);

        splash_img.animate().translationY(-2600).setDuration(1000).setStartDelay(4000);
        logo.animate().translationY(1790).setDuration(1000).setStartDelay(4000);
        title.animate().translationY(1400).setDuration(1000).setStartDelay(4000);
        lottieAnimationView.animate().translationY(1400).setDuration(1000).setStartDelay(4000);
    }


}