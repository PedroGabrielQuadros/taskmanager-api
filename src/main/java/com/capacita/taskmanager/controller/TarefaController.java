package com.capacita.taskmanager.controller;

import com.capacita.taskmanager.model.dto.TarefaRequestDTO;
import com.capacita.taskmanager.model.dto.TarefaResponseDTO;
import com.capacita.taskmanager.service.TarefaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tarefas")
@Tag(name = "Tarefas", description = "Endpoints para gerenciamento de tarefas do usuário")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @PostMapping
    @Operation(summary = "Criar uma nova tarefa", description = "Cria uma tarefa associada ao usuário autenticado.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<TarefaResponseDTO> criarTarefa(@RequestBody TarefaRequestDTO dto, Principal principal) {
        TarefaResponseDTO criada = tarefaService.criarTarefa(dto, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @GetMapping
    @Operation(summary = "Listar tarefas", description = "Retorna todas as tarefas do usuário autenticado.")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    public ResponseEntity<List<TarefaResponseDTO>> listarTarefas(Principal principal) {
        return ResponseEntity.ok(tarefaService.listarTarefas(principal.getName()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tarefa", description = "Atualiza os dados de uma tarefa existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa atualizada"),
            @ApiResponse(responseCode = "403", description = "Não é o dono da tarefa"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @PreAuthorize("@tarefaService.isOwner(authentication.name, #id)")
    public ResponseEntity<TarefaResponseDTO> atualizarTarefa(@PathVariable Long id, @RequestBody TarefaRequestDTO dto) {
        return ResponseEntity.ok(tarefaService.atualizarTarefa(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir tarefa", description = "Remove uma tarefa pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tarefa excluída"),
            @ApiResponse(responseCode = "403", description = "Não é o dono da tarefa"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })

    @PreAuthorize("@tarefaService.isOwner(authentication.name, #id)")
    public ResponseEntity<Void> deletarTarefa(@PathVariable Long id) {
        tarefaService.deletarTarefa(id);
        return ResponseEntity.noContent().build();
    }
}