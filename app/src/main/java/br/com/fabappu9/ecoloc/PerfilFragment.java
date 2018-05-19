package br.com.fabappu9.ecoloc;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Geraldo on 06/06/2017.
 * Fixed by Guilherme on 12/05/2018
 */

public class PerfilFragment extends Fragment {

    private View view;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        if(view == null)
            view= inflater.inflate(R.layout.fragment_perfil, container, false);
        return view;
    }

}
