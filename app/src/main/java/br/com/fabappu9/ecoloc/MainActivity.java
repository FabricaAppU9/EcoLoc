package br.com.fabappu9.ecoloc;

import android.app.Activity;
import android.app.Fragment;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                Fragment fragment = null;
                if(tabId == R.id.home){
                    fragment = new HomeFragment();
                }else if(tabId == R.id.mapa){
                    fragment = new MapaFragment();
                }else if(tabId == R.id.rank){
                    fragment = new RankFragment();
                }else if(tabId == R.id.perfil){
                    fragment = new PerfilFragment();
                }

                getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

            }
        });
    }
}
