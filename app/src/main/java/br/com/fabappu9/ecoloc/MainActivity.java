package br.com.fabappu9.ecoloc;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;

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
               if(tabId == R.id.mapa ){
                   if(getFragmentManager().findFragmentByTag("mapa") == null)
                       getFragmentManager().beginTransaction().replace(R.id.container, new MapaFragment(),"mapa").commit();
               }else if(tabId == R.id.perfil ){
                   if(getFragmentManager().findFragmentByTag("perfil") == null)
                       getFragmentManager().beginTransaction().replace(R.id.container, new PerfilFragment(),"perfil").commit();
               }

           }
       });

    }

    /*void setarMapa(){
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTabPosition(R.id.mapa);
    }*/

}


