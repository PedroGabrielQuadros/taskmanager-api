package com.capacita.taskmanager.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class LoginRequestDTO {
    private String email;
    private String senha;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}