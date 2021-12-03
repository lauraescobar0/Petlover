package com.example.petlover.comun;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.petlover.R;


public class sharedPreferencesUtils {

    public static String getSharedPreferencesString(Context context, String clave){ // Devieñve vacio sino encuentra un elemento.
        String valor;
        SharedPreferences sharedPreferences= context.getSharedPreferences(context.getString(R.string.SharedPreferencesAppID), Context.MODE_PRIVATE);
        valor= sharedPreferences.getString(clave,"");
        return valor;
    }

    public static String getSharedPreferencesString(Context context, String clave, String valorPorDefecto){
        String valor;
        SharedPreferences sharedPreferences= context.getSharedPreferences(context.getString(R.string.SharedPreferencesAppID), Context.MODE_PRIVATE);
        valor= sharedPreferences.getString(clave,valorPorDefecto);
        return valor;
    }

    public static int getSharedPreferencesInt(Context context, String clave){ //Devuelve -1 sino lo encuentra
        int valor;
        SharedPreferences sharedPreferences= context.getSharedPreferences(context.getString(R.string.SharedPreferencesAppID), Context.MODE_PRIVATE);
        valor= sharedPreferences.getInt(clave,-1);
        return valor;
    }

    public static int getSharedPreferencesInt(Context context, String clave, int valorPorDefecto){
        int valor;
        SharedPreferences sharedPreferences= context.getSharedPreferences(context.getString(R.string.SharedPreferencesAppID), Context.MODE_PRIVATE);
        valor= sharedPreferences.getInt(clave,valorPorDefecto);
        return valor;
    }

    public static boolean getSharedPreferencesBoolean(Context context, String clave){//Sino se indica el valorPorDefecto será falso.
        boolean valor;
        SharedPreferences sharedPreferences= context.getSharedPreferences(context.getString(R.string.SharedPreferencesAppID), Context.MODE_PRIVATE);
        valor= sharedPreferences.getBoolean(clave,false);
        return valor;
    }

    public static boolean getSharedPreferencesBoolean(Context context, String clave, boolean valorPorDefecto){
        boolean valor;
        SharedPreferences sharedPreferences= context.getSharedPreferences(context.getString(R.string.SharedPreferencesAppID), Context.MODE_PRIVATE);
        valor= sharedPreferences.getBoolean(clave,valorPorDefecto);
        return valor;
    }

    /* SETTERS, GUARDAR PREFERENCIAS, CLAVE-VALOR */
  /**************************************************************/

    public static void setSharedPreferenceInt(Context context,String clave, int valor){
        SharedPreferences sharedPreferences= context.getSharedPreferences(context.getString(R.string.SharedPreferencesAppID), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putInt(clave,valor);
        editor.apply();
    }

    public static void setSharedPreferenceString(Context context,String clave, String valor){
        SharedPreferences sharedPreferences= context.getSharedPreferences(context.getString(R.string.SharedPreferencesAppID), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(clave,valor);
        editor.apply();
    }

    public static void setSharedPreferenceBoolean(Context context,String clave, Boolean valor){
        SharedPreferences sharedPreferences= context.getSharedPreferences(context.getString(R.string.SharedPreferencesAppID), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putBoolean(clave,valor);
        editor.apply();
    }
}
