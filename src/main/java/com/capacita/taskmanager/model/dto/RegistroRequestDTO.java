package com.capacita.taskmanager.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class RegistroRequestDTO {
    private String nome;
    private String email;
    private String senha;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}