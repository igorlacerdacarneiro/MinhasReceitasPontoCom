package com.example.igorl.minhasreceitaspontocom.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.igorl.minhasreceitaspontocom.Models.Recipe;

import java.sql.Blob;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by igorl on 13/11/2017.
 */

public class DataBaseHelpers extends SQLiteOpenHelper {

    // Versão da Database
    private static final int DATABASE_VERSION = 1;
    // Nome da Database
    private static final String DATABASE_NAME = "db_receitas";
    // Nome da tabela Contacts
    private static final String TABLE_RECEITAS = "receitas";

    // Tabela Contatos, identificador colunas
    private static final String KEY_ID = "id";
    private static final String KEY_NOME = "nome";
    private static final String KEY_INGREDIENTES = "ingredientes";
    private static final String KEY_MODOPREPARO = "modopreparo";
    private static final String KEY_TEMPO = "tempo";
    private static final String KEY_RENDIMENTO = "rendimento";
    private static final String KEY_FOTO = "foto";

    public DataBaseHelpers(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Criar tabela, na primeira vez, a partir da segunda (se mesma versao) nao faz nada
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_RECEITAS_TABLE = "CREATE TABLE " + TABLE_RECEITAS + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY autoincrement, "
                + KEY_NOME + " TEXT, "
                + KEY_INGREDIENTES + " TEXT, "
                + KEY_MODOPREPARO + " TEXT, "
                + KEY_TEMPO + " TEXT, "
                + KEY_RENDIMENTO + " TEXT, "
                + KEY_FOTO + " TEXT "
                + " ) ";
        db.execSQL(CREATE_RECEITAS_TABLE);

        Log.d("teste", "onCreate: "+ CREATE_RECEITAS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("teste", "onUpadte:  oldVersion = "+oldVersion+" newVersion = "+newVersion);
        db.execSQL("DROP TABLE IF EXISTS TABLE_RECEITAS");
        onCreate(db);
    }

    // Adicionar uma receita
    public void addReceita (Recipe receita) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(db.isOpen())
        {
            Log.d("teste", "addReceitas: BD opened - receita: " + receita.getNome()+"; " + receita.getIngredientes()+"; "
                    + receita.getModoPreparo()+"; " + receita.getTempo()+"; " + receita.getRendimento()+"; " + receita.getFoto());
        }

        ContentValues values = new ContentValues();
        values.put(KEY_NOME, receita.getNome());
        values.put(KEY_INGREDIENTES, receita.getIngredientes());
        values.put(KEY_MODOPREPARO, receita.getModoPreparo());
        values.put(KEY_TEMPO, receita.getTempo());
        values.put(KEY_RENDIMENTO, receita.getRendimento());
        values.put(KEY_FOTO, receita.getFoto());

        long resultado = db.insert(TABLE_RECEITAS, null, values);

        Log.d("teste", "addReceitas: BD opened - resultado: "+resultado );

        if(db.isOpen())
        {
            db.close();
        }
    }

    public Recipe getReceita(int id) throws ParseException {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RECEITAS,
                new String[] { KEY_ID, KEY_NOME, KEY_INGREDIENTES, KEY_MODOPREPARO, KEY_TEMPO, KEY_RENDIMENTO, KEY_FOTO },
                KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Recipe receita = new Recipe();
        receita.setId(cursor.getInt(0));
        receita.setNome(cursor.getString(1));
        receita.setIngredientes(cursor.getString(2));
        receita.setModoPreparo(cursor.getString(3));
        receita.setTempo(cursor.getString(4));
        receita.setRendimento(cursor.getInt(5));
        receita.setFoto(cursor.getBlob(6));

        return receita;
    }

    public List<Recipe> getAllReceitas() {
        List<Recipe> receitasList = new ArrayList<Recipe>();

        String selectQuery = "SELECT  * FROM " + TABLE_RECEITAS;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {

                Recipe receita = new Recipe();

                receita.setId(cursor.getInt(0));
                receita.setNome(cursor.getString(1));
                receita.setIngredientes(cursor.getString(2));
                receita.setModoPreparo(cursor.getString(3));
                receita.setTempo(cursor.getString(4));
                receita.setRendimento(cursor.getInt(5));
                receita.setFoto(cursor.getBlob(6));

                receitasList.add(receita);

            } while (cursor.moveToNext());
        }
        if(db.isOpen())
        {
            db.close();
        }

        return receitasList;
    }

    public int updateReceita(Recipe receita) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOME, receita.getNome());
        values.put(KEY_INGREDIENTES, receita.getIngredientes());
        values.put(KEY_MODOPREPARO, receita.getModoPreparo());
        values.put(KEY_TEMPO, receita.getTempo());
        values.put(KEY_RENDIMENTO, receita.getRendimento());
        values.put(KEY_FOTO, receita.getFoto());

        return db.update(TABLE_RECEITAS, values, KEY_ID + " = ?", new String[] { String.valueOf(receita.getId()) });
    }

    public void deleteReceita(Recipe receita) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECEITAS, KEY_ID + " = ?", new String[] { String.valueOf(receita.getId()) });
        db.close();
    }

    public int getReceitasCount() {
        String countQuery = "SELECT  * FROM " + TABLE_RECEITAS;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }
}
