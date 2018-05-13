package br.com.fabappu9.ecoloc;

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

               switch (tabId){
                   case R.id.mapa:
                       getFragmentManager().beginTransaction().replace(R.id.container, new MapaFragment(),"mapa").commit();
                   break;
                   case R.id.perfil:
                       getFragmentManager().beginTransaction().replace(R.id.container, new PerfilFragment(),"perfil").commit();
                   break;
               }

           }
       });

    }


}


