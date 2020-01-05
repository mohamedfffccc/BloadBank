package com.example.bloadbank.data.local;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;

import com.example.bloadbank.R;
import com.example.bloadbank.Views.Activity.UserCycleActivity;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static com.example.bloadbank.helper.BloodBankConstants.REMEMBER_USER;

/**
 * Created by medo on 07/04/2017.
 */

public class Saveddata {
    public String govid;
    ProgressDialog progressDialog;
    SharedPreferences pref;
    static Context con;
   public String phone , password , api_token,remember_user;
    public Saveddata(Context con) {
        this.con = con;
        pref = con.getSharedPreferences("mypref", Context.MODE_PRIVATE);
    }

    //TODO save data  in shared preference
    public void Save_data(String phone , String password , String api_token , String remember_user  , String govid)
    {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("phone_number" , phone);
        editor.putString("password" , password);
        editor.putString("api_token" , api_token);
        editor.putString("governenent_id" , govid);

        editor.putString(REMEMBER_USER , remember_user);

        Log.d("data" , "saved");
        editor.commit();
    }
    //TODO read data from shared preference
    public void Read_data()
    {
        phone = pref.getString("phone_number" , "0");
        password=pref.getString("password" , "0");
        api_token=pref.getString("api_token" , "0");
        govid  =pref.getString("governenent_id" , "0");

        remember_user=pref.getString(REMEMBER_USER , "0");


        Log.d("data" , phone+"\n"+api_token);
        if (phone.equals("0"))
        {
            Intent i = new Intent(con, UserCycleActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            con.startActivity(i);
        }
    }
    //TODO upload profile picture to firebase

    public void show_Dialog()
    {
        progressDialog = new ProgressDialog(con);
        progressDialog.setTitle("please wait");
        progressDialog.show();
    }
    public void hide_Dialoge()
    {
        progressDialog.dismiss();
    }
//    public  void Save_Followings(ArrayList<String> f)
//    {
//         set = new HashSet<String>();
//        set.addAll(f);
//
//        SharedPreferences.Editor editor2 = pref.edit();
//        editor2.putStringSet("followings" , set);
//        Log.d("set" , "saved");
//        editor2.commit();
//    }
//    public  void  Read_Followings()
//    {
//        set = (HashSet<String>) pref.getStringSet("followings" , null);
//    }
  public static void showNegativeToast(Context context , String text)

  {
      Toast toast = new Toast(context);
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      View view = inflater.inflate(R.layout.toastviewnegative, null);
      TextView tv = view.findViewById(R.id.toasttextnega);
      tv.setText(text);
      toast.setView(view);
      toast.show();
  }
    public static void showPositiveToast(Context context , String text)

    {
        Typeface typeface = Typeface.createFromAsset(con.getAssets() , "fonts/fontskir.ttf");

        Toast toast = new Toast(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.toaseviewpositive, null);
        TextView tv = view.findViewById(R.id.toasttextposi);
        tv.setTypeface(typeface);
        tv.setText(text);
        toast.setView(view);
        toast.show();
    }
    public void clearData()
    {
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();

    }
    public static  Typeface setFont()
    {
        Typeface typeface = Typeface.createFromAsset(con.getAssets() , "fonts/fontskir.ttf");
        return typeface;

    }


}