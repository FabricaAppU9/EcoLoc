package br.com.fabappu9.ecoloc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import br.com.fabappu9.ecoloc.Model.RespostaLogin;
import br.com.fabappu9.ecoloc.data.SharedPreferenceHelper;
import br.com.fabappu9.ecoloc.network.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();
        final SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(SplashActivity.this);

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (sharedPreferenceHelper.getCheckLogin()){
                        logar(sharedPreferenceHelper.getUsuarioLogin(),sharedPreferenceHelper.getSenhaLogin());
                    }else{
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }
            }
        };
        timerThread.start();



    }

    void logar(String user, String senha){
        Call<RespostaLogin> retorno = null;
        retorno = new APIClient().getRestService().setUsuarioLoginDTO("12345", "GETLOGARUSUARIO", user, senha);
        configurarCallback(retorno);

    }

    private void configurarCallback(Call<RespostaLogin> retorno ) {
        retorno.enqueue(new Callback<RespostaLogin>() {
            @Override
            public void onResponse(Call<RespostaLogin> call, Response<RespostaLogin> response) {
                if (!response.isSuccessful()){
                    Log.e("ERRO:",response.message());
                }else{
                    RespostaLogin login = response.body();
                    if (login.getRETORNO().equals("SUCESSO")){
                        Intent intent1 = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent1);
                        finish();
                    }else{
                        Toast.makeText(SplashActivity.this, login.getRETORNO() +" ,Verifique usuário e senha" , Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RespostaLogin> call, Throwable error) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

}
