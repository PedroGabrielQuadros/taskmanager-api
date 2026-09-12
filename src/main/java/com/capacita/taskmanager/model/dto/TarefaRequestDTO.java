package com.capacita.taskmanager.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public class TarefaRequestDTO {

    @NotBlank(message = "O título é obrigatório")
    @Size(max = 100, message = "O título não pode ultrapassar 100 caracteres")
    private String titulo;

    private String descricao;

    public TarefaRequestDTO() {}

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}