package com.example.igorl.minhasreceitaspontocom.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.igorl.minhasreceitaspontocom.R;
import com.example.igorl.minhasreceitaspontocom.Models.Recipe;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * Created by igorl on 13/11/2017.
 */

public class ReceitaAdapter extends RecyclerView.Adapter<ReceitaAdapter.MyViewHolder> {

    private List<Recipe> receitaList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nome, ingredientes, modoPreparo, tempo, rendimento;
        public ImageView foto;

        public MyViewHolder(View view) {
            super(view);
            nome = (TextView) view.findViewById(R.id.textViewNomeReceita);
            foto = (ImageView) view.findViewById(R.id.imagemViewFoto);
        }
    }

    public ReceitaAdapter(List<Recipe> receitas) {
        this.receitaList = receitas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_linha_receita, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Recipe receitas = receitaList.get(position);
        holder.nome.setText(receitas.getNome());

        byte[] outImage = receitas.getFoto();
        ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
        Bitmap imageBitmap= BitmapFactory.decodeStream(imageStream);
        holder.foto.setImageBitmap(imageBitmap);
    }

    @Override
    public int getItemCount() {
        return receitaList.size();
    }
}