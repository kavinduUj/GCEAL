package com.appflavorsz.gceal.service.other;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AppCompatActivity;

public class AlerdDialog extends AppCompatActivity {


   public void openD(Context cc ,String title,String msg,int imag){

       new AlertDialog.Builder(cc)
               .setTitle(title)
               .setMessage(msg)
               .setIcon(imag)
               .setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {

                   }
               })
               .show();
   }



    }


