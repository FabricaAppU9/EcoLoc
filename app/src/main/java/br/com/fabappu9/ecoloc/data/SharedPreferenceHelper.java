package br.com.fabappu9.ecoloc.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import okhttp3.internal.cache.DiskLruCache;

/**
 * Created by andre on 15/04/2018.
 */


public class SharedPreferenceHelper {

    Context context;
    SharedPreferences sharedPreferences;
    static final String CHAVE_USUARIO = "usuario";
    static final String CHAVE_SENHA = "senha";
    static final String CHAVE_CHECK = "check";
    static final String CHAVE_FILE_LOGIN = "check";
    static final String CHAVE_NOME = "nome";
    static final String CHAVE_FOTO =  "foto";
    static final String CHAVE_ID = "-1";
    static final String CHAVE_PONTO = "50";

    public SharedPreferenceHelper(Context context) {
        this.context = context;
    }

    public void setLogin(String usuario, String senha, String nome, String foto,String id, String ponto) {
        sharedPreferences = context.getSharedPreferences(CHAVE_FILE_LOGIN, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CHAVE_CHECK, true);
        editor.putString(CHAVE_USUARIO, usuario);
        editor.putString(CHAVE_SENHA, senha);
        editor.putString(CHAVE_NOME, nome);
        editor.putString(CHAVE_FOTO, foto);
        editor.putString(CHAVE_ID,id);
        editor.putString(CHAVE_PONTO,ponto);
        editor.commit();
        Log.e("shared", sharedPreferences.getString("usuario", "erro"));
    }

    public String getUsuarioLogin(){
        sharedPreferences = context.getSharedPreferences(CHAVE_FILE_LOGIN, 0);
        return sharedPreferences.getString(CHAVE_USUARIO, "");
    }

    public String getSenhaLogin(){
        sharedPreferences = context.getSharedPreferences(CHAVE_FILE_LOGIN, 0);
        return sharedPreferences.getString(CHAVE_SENHA, "");
    }
    public String getNomeLogin(){
        sharedPreferences = context.getSharedPreferences(CHAVE_FILE_LOGIN, 0);
        return sharedPreferences.getString(CHAVE_NOME, "");
    }
    public String getFotoLogin(){
        sharedPreferences = context.getSharedPreferences(CHAVE_FILE_LOGIN, 0);
        return sharedPreferences.getString(CHAVE_FOTO, "");
    }
    public String getIDLogin(){
        sharedPreferences = context.getSharedPreferences(CHAVE_FILE_LOGIN, 0);
        return sharedPreferences.getString(CHAVE_ID, "");
    }

    public boolean getCheckLogin(){
        sharedPreferences = context.getSharedPreferences(CHAVE_FILE_LOGIN, 0);
        return sharedPreferences.getBoolean(CHAVE_CHECK, false);
    }

    public void setCheckLogin(){
        sharedPreferences = context.getSharedPreferences(CHAVE_FILE_LOGIN, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CHAVE_CHECK,false);
        editor.commit();
    }
    public void setPontoLogin(String ponto){
        sharedPreferences = context.getSharedPreferences(CHAVE_FILE_LOGIN, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CHAVE_PONTO,ponto);
        editor.commit();
    }
    public String getPontoLogin(){
        sharedPreferences = context.getSharedPreferences(CHAVE_FILE_LOGIN, 0);
        return sharedPreferences.getString(CHAVE_PONTO,"");
    }


}
