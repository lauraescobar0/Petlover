package com.example.petlover;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Menu extends AppCompatActivity {

    OfertasFragment ofertasFragment = new OfertasFragment();
    ClinicasFragment clinicasFragment = new ClinicasFragment();
    FavoritosFragment favoritosFragment = new FavoritosFragment();
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        navigation = findViewById(R.id.navegacion_btn);
        loadFragment(ofertasFragment);
        navigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.nav_ofert:
                    loadFragment(ofertasFragment);
                    return true;
                case R.id.nav_vetLocal:
                    loadFragment(clinicasFragment);
                    return true;
                case R.id.nav_favet:
                    loadFragment(favoritosFragment);
                    return true;

            }
            return false;
        });
       
    }
    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.menu_cuerpo, fragment);
        transaction.commit();
    }


}