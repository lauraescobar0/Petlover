package com.example.petlover;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.petlover.comun.sharedPreferencesUtils;

public class AppDemostration extends AppCompatActivity {

    ViewPager mcambio_pagina;
    LinearLayout mDotLayout;
    Button omitir_btn, continuar_btn, regresar_btn;

    TextView[] dots;
    AdaptadorPagina adaptadorPagina;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_descripcion);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        regresar_btn = findViewById(R.id.regresar_btn);
        continuar_btn = findViewById(R.id.continuar_btn);
        omitir_btn = findViewById(R.id.omitir_btn);

        regresar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getitem(0) > 0){

                    mcambio_pagina.setCurrentItem(getitem(-1),true);

                }

            }
        });

        continuar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getitem(0) < 3)
                    mcambio_pagina.setCurrentItem(getitem(1),true);
                else {
                    sharedPreferencesUtils.setSharedPreferenceBoolean(getApplicationContext(),getString(R.string.claveDemostracion),true);
                    Intent i = new Intent(AppDemostration.this,MainActivity.class);
                    startActivity(i);
                    finish();

                }

            }
        });

        omitir_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferencesUtils.setSharedPreferenceBoolean(getApplicationContext(),getString(R.string.claveDemostracion),true);
                Intent i = new Intent(AppDemostration.this,MainActivity.class);
                startActivity(i);
                finish();

            }
        });

        mcambio_pagina = (ViewPager) findViewById(R.id.cambio_pagina);
        mDotLayout = (LinearLayout) findViewById(R.id.indicador_layout);

        adaptadorPagina = new AdaptadorPagina(this);

        mcambio_pagina.setAdapter(adaptadorPagina);

        setUpindicator(0);
        mcambio_pagina.addOnPageChangeListener(viewListener);

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setUpindicator(int position){

        dots = new TextView[4];
        mDotLayout.removeAllViews();

        for (int i = 0 ; i < dots.length ; i++){

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.inactive,getApplicationContext().getTheme()));
            mDotLayout.addView(dots[i]);

        }

        dots[position].setTextColor(getResources().getColor(R.color.active,getApplicationContext().getTheme()));

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onPageSelected(int position) {

            setUpindicator(position);

            if (position > 0){

                regresar_btn.setVisibility(View.VISIBLE);

            }else {

                regresar_btn.setVisibility(View.INVISIBLE);

            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private int getitem(int i){

        return mcambio_pagina.getCurrentItem() + i;
    }


}