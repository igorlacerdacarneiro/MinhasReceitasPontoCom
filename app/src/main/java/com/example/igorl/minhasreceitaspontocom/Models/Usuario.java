package com.example.igorl.minhasreceitaspontocom.Models;

/**
 * Created by igorl on 27/09/2017.
 */

public class Usuario {
    private String nome;
    private String senha;
    private String email;

    public Usuario(){}

    public Usuario(String n, String s, String z){
        nome=n;
        senha = s;
        email = z;
    }

    public void setNome(String n)
    {
        this.nome = n;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setEmail(String email) {
        this.senha = email;
    }

    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }

    public String getEmail() {
        return email;
    }

    public String objString(){
        return String.format("%s;%s", nome, senha);
    }

}
