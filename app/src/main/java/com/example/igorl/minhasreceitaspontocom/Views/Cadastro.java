package com.example.igorl.minhasreceitaspontocom.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.igorl.minhasreceitaspontocom.R;

public class Cadastro extends AppCompatActivity {

    EditText editTextNome;
    EditText editTextEmail;
    EditText editTextSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        editTextNome = (EditText)findViewById(R.id.editTextNome);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextSenha = (EditText)findViewById(R.id.editTextSenha);
        Button btnCadastrar = (Button)findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarVazio(editTextNome,"Preencha o campo nome");
                validarVazio(editTextEmail,"Preencha o campo e-mail");
                validarVazio(editTextSenha,"Preencha o campo senha");
                boolean email_valido = validarEmail(editTextEmail.getText().toString());
                if(!email_valido){
                    editTextEmail.setError("Email inválido");
                }
                if(email_valido
                        && validarVazio(editTextNome,"Preencha o campo nome")
                        && validarVazio(editTextEmail,"Preencha o campo e-mail")
                        && validarVazio(editTextSenha,"Preencha o campo senha")){
                    Intent intent = new Intent(Cadastro.this, Receitas.class);
                    startActivity(intent);
                }

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
