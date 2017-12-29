package com.example.igorl.minhasreceitaspontocom.Models;

public class Recipe {
    private int id;
    private String nome;
    private String ingredientes;
    private String modoPreparo;
    private String tempo;
    private int rendimento;
    private byte[] foto;

    public Recipe(){}

    public Recipe(int a, String n, String i, String m, String t, int r, byte[] f){
        id = a;
        nome = n;
        ingredientes = i;
        modoPreparo = m;
        tempo = t;
        rendimento = r;
        foto = f;
    }

    public void setNome(String n) {
        this.nome = n;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public void setModoPreparo(String modoPreparo) {
        this.modoPreparo = modoPreparo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public void setRendimento(int rendimento) {
        this.rendimento = rendimento;
    }

    public String getNome() {
        return nome;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public String getModoPreparo() {
        return modoPreparo;
    }

    public String getTempo() {
        return tempo;
    }

    public int getRendimento() {
        return rendimento;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String toString(){
        return "Receita{" +
                "nome='" + nome + '\'' +
                ", ingredintes='" + ingredientes + '\'' +
                ", modopreparo='" + modoPreparo + '\'' +
                ", tempo='" + tempo + '\'' +
                ", rendimento='" + rendimento + '\'' +
                ", foto='" + foto + '\'' +
                '}';
    }
}