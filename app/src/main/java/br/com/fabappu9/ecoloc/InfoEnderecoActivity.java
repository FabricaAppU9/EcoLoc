package br.com.fabappu9.ecoloc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import br.com.fabappu9.ecoloc.Model.Resposta;
import br.com.fabappu9.ecoloc.network.APIClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class InfoEnderecoActivity extends AppCompatActivity {

    private static final String TAG = "InfoEnderecoActivity";
    EditText endereco;
    EditText tipomaterial;
    EditText nome;
    Double latitude,longitude;
    private Button btnCadastrarPonto;
    private Callback<Resposta> respostaCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_endereco);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        if (intent != null){
            Bundle params = intent.getExtras();
            if(params != null){
                String enderecoSaida =  params.getString("Endereco");
                latitude = params.getDouble("Latitude");
                longitude = params.getDouble("Longitude");

                endereco  = (EditText) findViewById(R.id.editTxtEndereco);
                endereco.setText(enderecoSaida);

                //Toast.makeText(InfoEnderecoActivity.this,latitude.toString(),Toast.LENGTH_LONG).show();
                //Toast.makeText(InfoEnderecoActivity.this,longitude.toString(),Toast.LENGTH_LONG).show();
                Log.d(TAG, "onCreate: " + enderecoSaida);

            }
        }
        btnCadastrarPonto = (Button) findViewById(R.id.btnCadastrarPonto);
        tipomaterial = (EditText)findViewById(R.id.edtTipoDescarte);
        nome = (EditText) findViewById(R.id.edtNomePonto);

        btnCadastrarPonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tipomaterial.getText().toString().equals("") || nome.getText().toString().equals("")){
                    Toast.makeText(InfoEnderecoActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }else {
                    configurarCallback();
                    //new APIClient().getRestService().setPontoDTO("12345", "CRIARPONTO,","teste01","pilha","11111","111111", respostaCallback);
                    new APIClient().getRestService().setPontoDTO("12345", "CRIARPONTO", nome.getText().toString(),tipomaterial.getText().toString(), latitude.toString(), longitude.toString(), respostaCallback);
                     //new APIClient().getRestService().setUsuarioLoginDTO("12345", "USUARIOLOGINDTO", Usuario, Senha, respostaCallback);
                    Intent intent = new Intent(InfoEnderecoActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    private void configurarCallback() {
        respostaCallback = new Callback<Resposta>() {
            @Override
            public void success(Resposta resposta, Response response) {
                Toast.makeText(InfoEnderecoActivity.this, "Cadastrado com Sucesso!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(InfoEnderecoActivity.this, "Algum erro aconteceu: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }

}
