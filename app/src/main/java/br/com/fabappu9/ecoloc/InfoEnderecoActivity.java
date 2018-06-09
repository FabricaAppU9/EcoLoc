package br.com.fabappu9.ecoloc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import br.com.fabappu9.ecoloc.DTO.MaterialDto;
import br.com.fabappu9.ecoloc.Model.RespostaPonto;
import br.com.fabappu9.ecoloc.network.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoEnderecoActivity extends AppCompatActivity {

    private static final String TAG = "InfoEnderecoActivity";
    EditText endereco;
    ListView listTipoMaterial;
    EditText nome;
    Double latitude,longitude;
    private SharedPreferences sharedPreferences;
    private Button btnCadastrarPonto;
    private List<MaterialDto> materiais =null;
    private Call<List<MaterialDto>> retorno = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_endereco);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        if (intent != null){
            Bundle params = intent.getExtras();
            if(params != null){
                String enderecoSaida =  params.getString("Endereco");
                latitude = params.getDouble("Latitude");
                longitude = params.getDouble("Longitude");

                endereco  = (EditText) findViewById(R.id.editTxtEndereco);
                endereco.setText(enderecoSaida);

                Log.d(TAG, "onCreate: " + enderecoSaida);

            }
        }
        btnCadastrarPonto = (Button) findViewById(R.id.btnCadastrarPonto);
        iniCallback();

        nome = (EditText) findViewById(R.id.edtNomePonto);

        btnCadastrarPonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<RespostaPonto> resposta;

                if (getCheckedItemCount() == 0 || nome.getText().toString().equals("")){
                    Toast.makeText(InfoEnderecoActivity.this, "Preencha todos os campos ", Toast.LENGTH_SHORT).show();
                }else {
                    sharedPreferences = getSharedPreferences("id", Context.MODE_PRIVATE);
                    String id = sharedPreferences.getString("id", "8");
                    resposta = new APIClient().getRestService().setPontoDTO("12345",
                            "CRIARPONTO",
                            nome.getText().toString(),
                            getIdsTems(),
                            latitude.toString(),
                            longitude.toString(),
                            id
                    );
                    configurarCallback(resposta);
                }
            }
        });
    }

    private int getCheckedItemCount(){
        int count=0;
        for (MaterialDto m: materiais)
            count += m.isMarcado()? 1:0;
        return count;
    }

    private String getIdsTems(){
        char separador =',';
        StringBuilder ids= new StringBuilder();
        for (MaterialDto m: materiais) {
            if(m.isMarcado())
                ids.append(m.getId()).append(separador);
        }
        return ids.toString();
    }

    private void initTipoMaterial(List<MaterialDto> materiais){
        listTipoMaterial = (ListView) findViewById(R.id.list_checkBox);
        MaterialAdapter adapter = new MaterialAdapter(this,
                R.layout.list_item_checkbox, materiais);
        listTipoMaterial.setAdapter(adapter);
    }


    // -----recebe lista dos materiais-----
    private void iniCallback(){
        retorno = new APIClient().getRestService().getMaterialDTO("12345", "GETTIPOMATERIAL", "");
        retorno.enqueue(new Callback<List<MaterialDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<MaterialDto>> call, @NonNull Response<List<MaterialDto>> response) {
                if (!response.isSuccessful()) {
                    Log.e("ERRO:", response.message());
                } else {
                    materiais = response.body();
                    initTipoMaterial(materiais);
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<MaterialDto>> call, @NonNull Throwable error) {
                Toast.makeText(InfoEnderecoActivity.this, "Erro: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    // ------ envia ponto cadastrado ------
    private void configurarCallback(Call<RespostaPonto> resposta) {
        resposta.enqueue(new Callback<RespostaPonto>() {
            @Override
            public void onResponse(@NonNull Call<RespostaPonto> call, @NonNull Response<RespostaPonto> response) {
                setResult(RESULT_OK);
                agradecimento();
            }

            @Override
            public void onFailure(@NonNull Call<RespostaPonto> call, @NonNull Throwable error) {
                Toast.makeText(InfoEnderecoActivity.this, "Algum erro aconteceu: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setResult(RESULT_CANCELED);
        finish();
        return super.onOptionsItemSelected(item);
    }



    private void agradecimento(){
        LayoutInflater li = getLayoutInflater();
        @SuppressLint("InflateParams") View view = li.inflate(R.layout.alert_agradecimento, null);
        // btn ok
        view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                finish();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(InfoEnderecoActivity.this);
        builder.setView(view);
        builder.create().show();
    }




//================================ cria a lista dos materiais ========================================
    class MaterialAdapter extends ArrayAdapter<MaterialDto>{
        private int resource;

        public MaterialAdapter(Context context , int resource , List<MaterialDto> objects) {
            super(context ,resource,objects);
            this.resource = resource;
        }

        @NonNull
        @Override
        public View getView(int position , View convertView , @NonNull ViewGroup parent) {
            View row = convertView;

            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert inflater != null;
                row = inflater.inflate(this.resource , parent , false);
            }

            String text = Objects.requireNonNull(getItem(position)).getDescricao();

            CheckBox checkBox = (CheckBox) row.findViewById(R.id.checkBox);
            checkBox.setText(text);
            checkBox.setTag(getItem(position));

            //-------
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MaterialDto p = (MaterialDto) v.getTag();
                    p.setMarcado(((CheckBox) v).isChecked());
                }
            });
            return row;
        }

    }
}