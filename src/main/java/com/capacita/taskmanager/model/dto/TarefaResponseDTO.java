package com.capacita.taskmanager.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class TarefaResponseDTO {

    private Long id;
    private String titulo;
    private String descricao;
    private boolean concluida;

    public TarefaResponseDTO() {}

    public TarefaResponseDTO(Long id, String titulo, String descricao, boolean concluida) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.concluida = concluida;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public boolean isConcluida() { return concluida; }
    public void setConcluida(boolean concluida) { this.concluida = concluida; }
}