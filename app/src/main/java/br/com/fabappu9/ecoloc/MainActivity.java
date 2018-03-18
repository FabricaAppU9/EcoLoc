package br.com.fabappu9.ecoloc;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.List;

import br.com.fabappu9.ecoloc.DTO.PontoDto;
import br.com.fabappu9.ecoloc.Model.RespostaLogin;
import br.com.fabappu9.ecoloc.Permissoes.Permissoes;
import br.com.fabappu9.ecoloc.network.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public List<PontoDto> pontos;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       Call<List<PontoDto>> retorno = null;
       retorno = new APIClient().getRestService().getPontoDTO("12345", "GETPONTOS", "");
       configurarCallback(retorno);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                Fragment fragment = null;
                if(tabId == R.id.mapa){
                    fragment = new MapaFragment();
                }else if(tabId == R.id.perfil){
                    fragment = new PerfilFragment();
                }

                getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

            }
        });

    }

    void setarMapa(){
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTabPosition(R.id.mapa);
    }
    private void configurarCallback(Call<List<PontoDto>> retorno ) {
        retorno.enqueue(new Callback<List<PontoDto>>() {
            @Override
            public void onResponse(Call<List<PontoDto>> call, Response<List<PontoDto>> response) {
                if (!response.isSuccessful()) {
                    Log.e("ERRO:", response.message());
                } else {
                    pontos = (List<PontoDto>) response.body();

                    Toast.makeText(MainActivity.this, pontos.size(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<List<PontoDto>> call, Throwable error) {
                Toast.makeText(MainActivity.this, "Deu Ruim: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}


