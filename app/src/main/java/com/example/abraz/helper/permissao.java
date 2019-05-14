package com.example.abraz.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class permissao {

    public static boolean validarPermissoes(String[]permissoes, Activity activity, int requestCode){

        if(Build.VERSION.SDK_INT>=23){
            List<String> listaOermissoes = new ArrayList<>();

            for(String permissao: permissoes){
               Boolean temPermissao= ContextCompat.checkSelfPermission(activity, permissao)==PackageManager.PERMISSION_GRANTED;
               if (!temPermissao) listaOermissoes.add(permissao);

            }

            if(listaOermissoes.isEmpty()) return true;

            String[]novasPermissoes = new String[listaOermissoes.size()];
            listaOermissoes.toArray(novasPermissoes);

            //Solicitar permissoes

            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode);


        }
        return true;
    }
}
