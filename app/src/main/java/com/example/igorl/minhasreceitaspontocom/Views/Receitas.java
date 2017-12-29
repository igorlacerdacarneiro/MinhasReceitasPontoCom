package com.example.igorl.minhasreceitaspontocom.Views;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.igorl.minhasreceitaspontocom.Models.Recipe;
import com.example.igorl.minhasreceitaspontocom.R;
import com.example.igorl.minhasreceitaspontocom.Adapters.ReceitaAdapter;
import com.example.igorl.minhasreceitaspontocom.Utils.DataBaseHelpers;
import com.example.igorl.minhasreceitaspontocom.Utils.DividerItemDecoration;
import com.example.igorl.minhasreceitaspontocom.Utils.RecyclerTouchListener;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Receitas extends AppCompatActivity {

    private Button botao;
    private List<Recipe> receitas = new ArrayList<>();

    RecyclerView mRecyclerView;
    ReceitaAdapter mReceitaAdapter;
    DataBaseHelpers db = new DataBaseHelpers(this);

    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_receitas);

        receitas = db.getAllReceitas();

        mRecyclerView = (RecyclerView) findViewById(R.id.listViewReceitas);
        botao = (Button) findViewById(R.id.btnAddReceita);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Receitas.this, Adicionar.class);
                startActivityForResult(intent,1);
            }
        });

        mReceitaAdapter = new ReceitaAdapter(receitas);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mReceitaAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Recipe receita = receitas.get(position);
                Intent intent = new Intent(Receitas.this, Receita.class);

                intent.putExtra("receita",gson.toJson(receita));
                startActivityForResult(intent, 2);

            }

            @Override
            public void onLongClick(View view, final int position) {
                //Cria o gerador do AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(Receitas.this);
                //define o titulo
                builder.setTitle("Minhas Receitas");
                //define a mensagem
                builder.setMessage("Você tem certeza que deseja deletar esta receita? ");
                //define um botão como positivo
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Recipe contato = receitas.get(position);

                        receitas.remove(contato);
                        db.deleteReceita(contato);

                        mReceitaAdapter.notifyDataSetChanged();
                    }
                });
                //define um botão como negativo.
                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                AlertDialog alerta = builder.create();
                alerta.show();
            }
        }));
    }

    @Override
    public void onActivityResult(int codigo, int resultado, Intent data) {
        super.onActivityResult(codigo, resultado, data);
        if(resultado == Activity.RESULT_OK && data != null){
            String result = data.getStringExtra("receita");
            Recipe receita = gson.fromJson(result, Recipe.class);
            if(codigo == 1){
                db.addReceita(receita);
            }
            else if (codigo == 2){
                db.updateReceita(receita);
            }
            receitas.clear();
            receitas.addAll(db.getAllReceitas());
            mReceitaAdapter.notifyDataSetChanged();
        }
    }
}