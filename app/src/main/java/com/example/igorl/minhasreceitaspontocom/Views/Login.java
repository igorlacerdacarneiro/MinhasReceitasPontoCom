package com.example.igorl.minhasreceitaspontocom.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.igorl.minhasreceitaspontocom.R;

public class Login extends AppCompatActivity {

    EditText usuario;
    EditText senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button botaoLogin = (Button) findViewById(R.id.btnLogin);
        Button botaoCadastrar = (Button) findViewById(R.id.btnCadastrar);
        usuario = (EditText) findViewById(R.id.editTextUsuario);
        senha = (EditText) findViewById(R.id.editTextSenha);

        botaoLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void  onClick(View v){

                validarVazio(senha,"Preencha o campo senha");

                boolean email_valido = validarEmail(usuario.getText().toString());
                if(!email_valido){
                    usuario.setError("Email inválido");
                    usuario.setFocusable(true);
                    usuario.requestFocus();
                }

                if(email_valido && validarVazio(senha,"Preencha o campo senha")){
                    if(usuario.getText().toString().equals("igor@gmail.com")  && senha.getText().toString().equals("12345")){
                        Intent intent = new Intent(Login.this, Receitas.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "Usuario ou senha incorreto!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        botaoCadastrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void  onClick(View v){
                Intent intent = new Intent(Login.this, Cadastro.class);
                startActivity(intent);
            }
        });
    }

    public final static boolean validarEmail(String txtEmail) {
        if (TextUtils.isEmpty(txtEmail)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches();
        }
    }

    public static boolean validarVazio(View pView, String pMessage) {
        if (pView instanceof EditText) {
            EditText edText = (EditText) pView;
            Editable text = edText.getText();
            if (text != null) {
                String strText = text.toString();
                if (!TextUtils.isEmpty(strText)) {
                    return true;
                }
            }
            edText.setError(pMessage);
            edText.setFocusable(true);
            edText.requestFocus();
            return false;
        }
        return false;
    }
}

