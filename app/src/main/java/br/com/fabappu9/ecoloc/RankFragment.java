package br.com.fabappu9.ecoloc;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by Geraldo on 06/06/2017.
 */

public class RankFragment extends Fragment {

    private int idUsuario;
    private int pontuacao;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){



        View v = inflater.inflate(R.layout.rank, container, false);


        ArrayList<String> menuItems = new ArrayList<String>();

        //TODO: Fazer com que a Lista seja atualizada de acordo com a pontuação adquirida por cada usuário.
        menuItems.add(1+"º Mateus Guedes da Conceição");
        menuItems.add(2+"º Guilherme Golfeto");
        menuItems.add(3+"º Andre Gonçalves");
        menuItems.add(4+"º Gerson");
        menuItems.add(5+"º Janderson");
        menuItems.add(6+"º Victor");
        menuItems.add(7+"º Fulano.");
        menuItems.add(8+"º Fulano..");
        menuItems.add(9+"º Fulano...");
        menuItems.add(10+"º Fulano....");
        menuItems.add(11+"º Fulano.....");
        menuItems.add(12+"º Fulano......");



        ListView listView = (ListView) v.findViewById(R.id.RankList);

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, menuItems);

        listView.setAdapter(itemsAdapter);



        return v;
    }

    public int getidUsuario() {
        return idUsuario;
    }

    public void setidUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public void exibir() {

    }

}
