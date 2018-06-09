package br.com.fabappu9.ecoloc;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.fabappu9.ecoloc.data.SharedPreferenceHelper;

/**
 * Created by Geraldo on 06/06/2017.
 * Fixed by Guilherme on 12/05/2018
 */

public class PerfilFragment extends Fragment {

    private View view;
    private TextView apelido;
    private TextView nome;
    private TextView pontuacao;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        if(view == null)
            view= inflater.inflate(R.layout.fragment_perfil, container, false);
        apelido =(TextView) view.findViewById(R.id.txtApelidoUsuario);
        nome =(TextView) view.findViewById(R.id.txtNomeCompleto);
        pontuacao =(TextView) view.findViewById(R.id.txtQuantidadePontos);
        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(getActivity());

        apelido.setText(sharedPreferenceHelper.getUsuarioLogin());
        nome.setText(sharedPreferenceHelper.getNomeLogin());
        pontuacao.setText(sharedPreferenceHelper.getPontoLogin() + " Ponto(s) obtidos");

        return view;
    }

}
