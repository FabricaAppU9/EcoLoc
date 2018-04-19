package br.com.fabappu9.ecoloc;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.MapFragment;

/**
 * Created by Geraldo on 06/06/2017.
 */

public class PerfilFragment extends Fragment {
    private View view =null;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        if(view == null)
            view= inflater.inflate(R.layout.perfil, container, false);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MapaFragment.RetainedFragment fragment = (MapaFragment.RetainedFragment) getFragmentManager().findFragmentByTag("work");

        if (fragment != null) {
            fragment.setTargetFragment(this, 0);
        }
    }}
