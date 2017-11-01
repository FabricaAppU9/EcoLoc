package br.com.fabappu9.ecoloc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class InfoEnderecoActivity extends AppCompatActivity {


    private static final String TAG = "InfoEnderecoActivity";
    EditText endereco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_endereco);

        Intent intent = getIntent();

        if (intent != null){
            Bundle params = intent.getExtras();
            if(params != null){
               String latlng =  params.getString("Latitude") + params.getString("Longitude");
                endereco  = (EditText) findViewById(R.id.editTxtEndereco);
                endereco.setText(latlng);

               Log.d(TAG, "onCreate: " + latlng);

            }
        }


    }
}
