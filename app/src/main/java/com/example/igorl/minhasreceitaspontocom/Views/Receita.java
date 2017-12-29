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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Receita extends AppCompatActivity {

    private Button botaoEditar;
    private Button botaoSalvar;
    private Button botaoCamera;
    private Button botaoGaleria;

    EditText nome;
    EditText modoPreparo;
    EditText ingredientes;
    EditText tempo;
    EditText rendimento;
    ImageView foto;

    Integer receitaId;

    Gson gson = new Gson();

    ImageView imageView;

    int PERMISSAO_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receita);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        botaoEditar = (Button) findViewById(R.id.btnEditar);
        botaoSalvar = (Button) findViewById(R.id.btnSalvar);
        botaoCamera = (Button) findViewById(R.id.botaoAdicionarFotoCamera);
        botaoGaleria = (Button) findViewById(R.id.botaoAdicionarFotoGaleria);

        imageView = (ImageView) findViewById(R.id.imgViewFoto);

        nome = (EditText) findViewById(R.id.eTextNomeReceita);
        ingredientes = (EditText) findViewById(R.id.eTextIngredientes);
        modoPreparo = (EditText) findViewById(R.id.eTextModoPreparo);
        tempo = (EditText) findViewById(R.id.eTextTempoPreparo);
        rendimento = (EditText) findViewById(R.id.eTextRendimento);
        foto = (ImageView) findViewById(R.id.imgViewFoto);

        disableEditText(nome);
        disableEditText(ingredientes);
        disableEditText(modoPreparo);
        disableEditText(tempo);
        disableEditText(rendimento);
        disableButton(botaoSalvar);
        disableButton(botaoCamera);
        disableButton(botaoGaleria);

        Bundle bundle = getIntent().getExtras();
        String result = bundle.getString("receita");

        Recipe receita = gson.fromJson(result, Recipe.class);

        receitaId = receita.getId();

        nome.setText(receita.getNome());
        ingredientes.setText(receita.getIngredientes());
        modoPreparo.setText(receita.getModoPreparo());
        tempo.setText(receita.getTempo());
        rendimento.setText(String.valueOf(receita.getRendimento()));

        byte[] outImage = receita.getFoto();
        ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
        Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);
        foto.setImageBitmap(imageBitmap);

        botaoEditar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                enableEditText(nome);
                enableEditText(ingredientes);
                enableEditText(modoPreparo);
                enableEditText(tempo);
                enableEditText(rendimento);
                disableButton(botaoEditar);
                enableButton(botaoSalvar);
                enableButton(botaoCamera);
                enableButton(botaoGaleria);
            }
        });

        botaoSalvar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(validarCampos()){
                    Recipe receita = new Recipe();
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

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("receita",gson.toJson(receita));
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
            }
        });

        botaoCamera.setOnClickListener(new View.OnClickListener() {
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

        botaoGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

    }

    private void disableEditText(EditText editText) {
        editText.setEnabled(false);
    }

    private void enableEditText(EditText editText) {
        editText.setEnabled(true);
    }

    private void disableButton(Button button) {
        button.setVisibility(View.INVISIBLE);
    }

    private void enableButton(Button button) {
        button.setVisibility(View.VISIBLE);
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
