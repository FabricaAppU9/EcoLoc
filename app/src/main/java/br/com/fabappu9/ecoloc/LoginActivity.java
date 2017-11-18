package br.com.fabappu9.ecoloc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import br.com.fabappu9.ecoloc.Model.Resposta;
import br.com.fabappu9.ecoloc.Model.RespostaLogin;
import br.com.fabappu9.ecoloc.Permissoes.Permissoes;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import br.com.fabappu9.ecoloc.network.APIClient;

public class LoginActivity extends AppCompatActivity {
    TextView user;
    TextView pass;
    Button cadastrar;
    Button login;
    private Callback<RespostaLogin> respostaCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Permissoes permissoes = new Permissoes();
        permissoes.setMyPermissionsRequestAccessFineLocation(LoginActivity.this);

        cadastrar  = (Button) findViewById(R.id.CadastrarButton);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(LoginActivity.this, CadastradoActivity.class);
                startActivity(intent1);

            }
        });


        login  = (Button) findViewById(R.id.LoginButton);
        user = (TextView) findViewById(R.id.txtLoginUser);
        pass = (TextView) findViewById(R.id.txtLoginPass);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent1);
                /*
                String Usuario = user.getText().toString();
                String Senha = pass.getText().toString();

                if (TextUtils.isEmpty(Usuario) || TextUtils.isEmpty(Senha)){
                    Toast.makeText(LoginActivity.this, "Campo usuario ou senha em branco.", Toast.LENGTH_SHORT).show();
                }else{
                    configurarCallback();
                    new APIClient().getRestService().setUsuarioLoginDTO("12345", "GETLOGARUSUARIO", Usuario, Senha, respostaCallback);
                }
                */
            }
        });
    }
    private void configurarCallback() {
        respostaCallback = new Callback<RespostaLogin>() {
            @Override
            public void success(RespostaLogin resposta, Response response) {
                if (resposta.getRETORNO().equals("SUCESSO")){
                    Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent1);
                }else{
                    Toast.makeText(LoginActivity.this, resposta.getRETORNO() +" ,Verifique usuário e senha" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(LoginActivity.this, "Deu Ruim: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }



}