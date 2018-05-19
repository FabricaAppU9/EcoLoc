package br.com.fabappu9.ecoloc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import br.com.fabappu9.ecoloc.Model.Resposta;
import br.com.fabappu9.ecoloc.network.APIClient;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
//import retrofit2.RetrofitError;
import retrofit2.Response;

import static br.com.fabappu9.ecoloc.FotoFragment.TAG_FOTO_FRAGMENT;


public class CadastradoActivity extends AppCompatActivity {
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int RESULT_LOAD_IMAGE = 2;
    static final int RESULT_HABILIT_CAM = 3;

    private FotoFragment fotoLogin;
    private AlertDialog mensagem;
    private TextView txtNome;
    private TextView txtUsuario;
    private TextView txtSenha, txtConfirmarSenha;
    private Button btnCadastrar , btnAddFoto;
    private CircleImageView imageFoto;
    private Callback<Resposta> respostaCallback;
    String photoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrado);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getFragmentManager();
        fotoLogin = (FotoFragment) fm.findFragmentByTag(TAG_FOTO_FRAGMENT);
        if (fotoLogin == null) {
            fotoLogin = new FotoFragment(640, 320);
            fm.beginTransaction().add(fotoLogin, TAG_FOTO_FRAGMENT).commit();
        }


        txtNome = (TextView) findViewById(R.id.txtNome);
        txtUsuario = (TextView) findViewById(R.id.txtUsuario);
        txtSenha = (TextView) findViewById(R.id.txtSenha);
        txtConfirmarSenha = (TextView) findViewById(R.id.txtConfirmarSenha);
        imageFoto = (CircleImageView) findViewById(R.id.imgUsuario);
        btnAddFoto = (Button) findViewById(R.id.btnAddFoto);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        imageFoto.setImageBitmap(fotoLogin.getFoto(getResources()));

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
                        retorno = new APIClient().getRestService().setUsuarioDTO("12345", "CRIARUSUARIODTO",
                                nome,
                                usuario,
                                senha,
                                fotoLogin.getFotoBase64());
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

        buttonAddFoto();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            fotoLogin.setFoto(photoPath);

        }else if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            assert selectedImage != null;
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            fotoLogin.setFoto(picturePath);
        }
        imageFoto.setImageBitmap(fotoLogin.getFoto(getResources()));
    }

    // abrir app da camera do android
    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "br.com.fabappu9.ecoloc.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        photoPath = image.getAbsolutePath();
        return image;
    }


    // tela mensagem para editar a foto "btn"
    public void buttonAddFoto() {
        btnAddFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = getLayoutInflater();
                View view = li.inflate(R.layout.alert_add_foto, null);
                // btn galeria
                view.findViewById(R.id.btn_galeria).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        selectImageGaleria();
                        mensagem.dismiss();
                    }
                });
                // btn camera
                view.findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        dispatchTakePictureIntent();
                        mensagem.dismiss();
                    }
                });
                // btn remover foto
                Button btnRemover = (Button) view.findViewById(R.id.btn_remover_foto);
                if(!fotoLogin.isFotoAdd())
                    btnRemover.setVisibility(View.GONE);
                btnRemover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fotoLogin.cleanFoto(getResources());
                        imageFoto.setImageBitmap(fotoLogin.getFoto(getResources()));
                        mensagem.dismiss();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(CadastradoActivity.this);
                builder.setView(view);
                mensagem = builder.create();
                mensagem.show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && requestCode == RESULT_HABILIT_CAM && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImageGaleria();
        }
    }

    // abrir app do android para carregar a foto (galeria)
    private void selectImageGaleria() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), RESULT_LOAD_IMAGE);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE },
                    RESULT_HABILIT_CAM);
        }
    }


    private void configurarCallback(Call<Resposta> resposta) {
        resposta.enqueue( new Callback<Resposta>() {
            @Override
            public void onResponse(@NonNull Call<Resposta> call, @NonNull Response<Resposta> response) {
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
            public void onFailure(@NonNull Call<Resposta> call, @NonNull Throwable error) {
                Toast.makeText(CadastradoActivity.this, "Algum erro aconteceu: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
