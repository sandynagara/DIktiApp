package com.example.dikti;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preference {
    private static final String dataLogin = "status login",dataAs = "as",dataUsername="username",filterDepartemen="Dikti",filterJenisLomba="Karya Tulis";
    private static final String filterAngkatan="angkatan";

    private static SharedPreferences getSharedPreferences (Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setDataAs(Context context, String data){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(dataAs,data);
        editor.apply();
    }

    public static String getDataAs(Context context){
        return getSharedPreferences(context).getString(dataAs,"");
    }

    public static void setDataUsername(Context context, String data){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(dataUsername,data);
        editor.apply();
    }

    public static String getDataUsername(Context context){
        return getSharedPreferences(context).getString(dataUsername,"");
    }

    public static void setDataJenisLomba(Context context, String data){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(filterJenisLomba,data);
        editor.apply();
    }

    public static String getDataJenisLomba(Context context){
        return getSharedPreferences(context).getString(filterJenisLomba,"");
    }

    public static void setDataLogin(Context context, boolean status){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(dataLogin, status);
        editor.apply();
    }

    public static boolean getDataLogin(Context context){
        return getSharedPreferences(context).getBoolean(dataLogin,false);
    }

    public static void setFilterDepartemen(Context context, String data){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(filterDepartemen,data);
        editor.apply();
    }

    public static String getFilterDepartemen(Context context){
        return getSharedPreferences(context).getString(filterDepartemen,"");
    }

    public static void setFilterAngkatan(Context context, String data){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(filterAngkatan,data);
        editor.apply();
    }

    public static String getFilterAngkatan(Context context){
        return getSharedPreferences(context).getString(filterAngkatan,"");
    }

    public static void clearData(Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(dataAs);
        editor.remove(dataLogin);
        editor.remove(dataUsername);
        editor.apply();
    }

    public static void clearDepartemen(Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(filterDepartemen);
        editor.apply();
    }

    public static void clearAngkatan(Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(String.valueOf(filterAngkatan));
        editor.apply();
    }

    public static void clearJenisLomba(Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(filterJenisLomba);
        editor.apply();
    }
}
