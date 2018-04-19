package br.com.fabappu9.ecoloc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import br.com.fabappu9.ecoloc.Model.RespostaLogin;
import br.com.fabappu9.ecoloc.Permissoes.Permissoes;
import br.com.fabappu9.ecoloc.data.SharedPreferenceHelper;
import br.com.fabappu9.ecoloc.network.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import retrofit2.RetrofitError;

public class LoginActivity extends AppCompatActivity {
    private TextView user;
    private TextView pass;
    private Button cadastrar;
    private Button login;
    private Callback<RespostaLogin> respostaCallback;
    CheckBox checkBox;
    String Usuario;
    String Senha;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Permissoes permissoes = new Permissoes();
        permissoes.setMyPermissionsRequestAccessFineLocation(LoginActivity.this);
        cadastrar  = (Button) findViewById(R.id.CadastrarButton);
        checkBox = (CheckBox) findViewById(R.id.check_lembre_me);
        user = (TextView) findViewById(R.id.txtLoginUser);
        pass = (TextView) findViewById(R.id.txtLoginPass);
        login  = (Button) findViewById(R.id.LoginButton);

        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(LoginActivity.this);
        if (sharedPreferenceHelper.getCheckLogin()){
            user.setText(sharedPreferenceHelper.getUsuarioLogin());
            pass.setText(sharedPreferenceHelper.getSenhaLogin());
            checkBox.setChecked(true);
            login.callOnClick();
        }
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(LoginActivity.this, CadastradoActivity.class);
                startActivity(intent1);


            }
        });




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Usuario = user.getText().toString();
                Senha = pass.getText().toString();

                Call<RespostaLogin> retorno = null;
                if (TextUtils.isEmpty(Usuario) || TextUtils.isEmpty(Senha)) {
                    Toast.makeText(LoginActivity.this, "Campo usuario ou senha em branco.", Toast.LENGTH_SHORT).show();
                } else {
                    retorno = new APIClient().getRestService().setUsuarioLoginDTO("12345", "GETLOGARUSUARIO", Usuario, Senha);
                    configurarCallback(retorno);

                }
            }
        });
    }
    private void configurarCallback(Call<RespostaLogin> retorno ) {
        retorno.enqueue(new Callback<RespostaLogin>() {
            @Override
            public void onResponse(Call<RespostaLogin> call, Response<RespostaLogin> response) {

                try {
                    new Thread().sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!response.isSuccessful()){
                    Log.e("ERRO:",response.message());
                }else{
                    RespostaLogin login = response.body();
                    if (login.getRETORNO().equals("SUCESSO")){
                        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(LoginActivity.this);
                        if (isCheck()){
                            sharedPreferenceHelper.setLogin(Usuario,Senha);
                        }else{
                            sharedPreferenceHelper.setCheckLogin();
                        }
                        Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent1);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, login.getRETORNO() +" ,Verifique usuário e senha" , Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onFailure(Call<RespostaLogin> call, Throwable error) {
                Toast.makeText(LoginActivity.this, "Deu Ruim: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isCheck(){
        return  checkBox.isChecked();
    }

}