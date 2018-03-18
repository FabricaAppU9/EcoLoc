package br.com.fabappu9.ecoloc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import br.com.fabappu9.ecoloc.Model.Resposta;
import br.com.fabappu9.ecoloc.Model.RespostaLogin;
import br.com.fabappu9.ecoloc.network.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
//import retrofit2.RetrofitError;
import retrofit2.Response;


public class CadastradoActivity extends AppCompatActivity {

    private TextView txtNome;
    private TextView txtUsuario;
    private TextView txtSenha;
    private TextView txtConfirmarSenha;
    private Button btnCadastrar;
    private Callback<Resposta> respostaCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrado);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtNome = (TextView) findViewById(R.id.txtNome);
        txtUsuario = (TextView) findViewById(R.id.txtUsuario);
        txtSenha = (TextView) findViewById(R.id.txtSenha);
        txtConfirmarSenha = (TextView) findViewById(R.id.txtConfirmarSenha);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome =txtNome.getText().toString();
                String usuario =txtUsuario.getText().toString();
                String senha =txtSenha.getText().toString();
                String confirmar =txtConfirmarSenha.getText().toString();
                Call<Resposta> retorno = null;
                if (nome.equals("") || usuario.equals("") ||
                        senha.equals("") || confirmar.equals("")){

                    Toast.makeText(CadastradoActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();

                }else {

                    if (senha.equals(confirmar)) {
                        retorno = new APIClient().getRestService().setUsuarioDTO("12345", "CRIARUSUARIODTO", nome, usuario, senha);
                        configurarCallback(retorno);
                        Intent intent = new Intent(CadastradoActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();

                    } else {

                        Toast.makeText(CadastradoActivity.this, "Essas senhas não coicidem", Toast.LENGTH_SHORT).show();
                        txtSenha.setText("");
                        txtConfirmarSenha.setText("");

                    }
                }
            }
        });

    }

    private void configurarCallback(Call<Resposta> resposta) {
        resposta.enqueue( new Callback<Resposta>() {
            @Override
            public void onResponse(Call<Resposta> call, Response<Resposta> response) {
                if (!response.isSuccessful()){
                    Log.e("ERRO:",response.message());
                }else{
                    Resposta res = response.body();
                    if (res.getRETORNO().equals("SUCESSO")){
                        Toast.makeText(CadastradoActivity.this, "Cadastrado com Sucesso!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(CadastradoActivity.this, res.getRETORNO() +" ,não cadastrado" , Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Resposta> call, Throwable error) {
                Toast.makeText(CadastradoActivity.this, "Algum erro aconteceu: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
