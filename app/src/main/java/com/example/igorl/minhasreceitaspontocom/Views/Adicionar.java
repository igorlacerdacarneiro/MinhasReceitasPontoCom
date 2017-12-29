package com.example.igorl.minhasreceitaspontocom.Views;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.igorl.minhasreceitaspontocom.Models.Recipe;
import com.example.igorl.minhasreceitaspontocom.R;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;

public class Adicionar extends AppCompatActivity {

    EditText nome;
    EditText modoPreparo;
    EditText ingredientes;
    EditText tempo;
    EditText rendimento;
    ImageView foto;

    Gson gson = new Gson();
    Integer receitaId;

    ImageView imageView;

    int PERMISSAO_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Button botaoAdicionar = (Button) findViewById(R.id.btnAdicionarReceita);

        Button btnCamera = (Button) findViewById(R.id.btnAdicionarFoto);
        Button btnGaleria = (Button) findViewById(R.id.btnAdicionarFotoGaleria);

        imageView = (ImageView) findViewById(R.id.imageViewFoto);

        botaoAdicionar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(validarCampos()){
                    Recipe receita = new Recipe();

                    nome = ((EditText) findViewById(R.id.editTextNomeReceita));
                    ingredientes = ((EditText) findViewById(R.id.editTextIngredientes));
                    modoPreparo = ((EditText) findViewById(R.id.editTextModoPreparo));
                    tempo = ((EditText) findViewById(R.id.editTextTempoPreparo));
                    rendimento = ((EditText) findViewById(R.id.editTextRendimento));
                    foto = ((ImageView) findViewById(R.id.imageViewFoto));

                    if(receitaId != null)
                    {
                        receita.setId(receitaId);
                    }
                    receita.setNome(nome.getText().toString());
                    receita.setIngredientes(ingredientes.getText().toString());
                    receita.setModoPreparo(modoPreparo.getText().toString());
                    receita.setTempo(tempo.getText().toString());
                    receita.setRendimento(Integer.parseInt(rendimento.getText().toString()));

                    Bitmap bitmap = ((BitmapDrawable)foto.getDrawable()).getBitmap();
                    ByteArrayOutputStream saida = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,saida);
                    byte[] img = saida.toByteArray();

                    receita.setFoto(img);

                    Intent intent = new Intent();
                    intent.putExtra("receita",gson.toJson(receita));
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSAO_REQUEST);
            }
        }

        btnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

    }

    private boolean validarCampos(){
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null){
            if(requestCode == 0){
                Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                imageView.setImageBitmap(bitmap);
            }
            if(requestCode == 1){
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap imagemGaleria = (BitmapFactory.decodeFile(picturePath));
                imageView.setImageBitmap(imagemGaleria);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        if(requestCode== PERMISSAO_REQUEST) {
            if(grantResults.length> 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // A permissão foi concedida. Pode continuar
            } else{
                // A permissão foi negada.
                return;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home)
            finish();
        return  super.onOptionsItemSelected(item);
    }

}