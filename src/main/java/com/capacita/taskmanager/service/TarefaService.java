package com.capacita.taskmanager.service;

import com.capacita.taskmanager.model.entity.Tarefa;
import com.capacita.taskmanager.model.entity.Usuario;
import com.capacita.taskmanager.repository.TarefaRepository;
import com.capacita.taskmanager.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.capacita.taskmanager.model.dto.TarefaResponseDTO;
import com.capacita.taskmanager.model.dto.TarefaRequestDTO;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final UsuarioRepository usuarioRepository;

    public TarefaService(TarefaRepository tarefaRepository, UsuarioRepository usuarioRepository){
        this.tarefaRepository= tarefaRepository;
        this.usuarioRepository= usuarioRepository;
    }

    public TarefaResponseDTO criar (String emailUsuario, TarefaRequestDTO dto){
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);

        Tarefa novaTarefa = new Tarefa(dto.getTitulo(),dto.getDescricao(),usuario);
        Tarefa tarefaSalva = tarefaRepository.save(novaTarefa);

        return converterParaResponseDTO(tarefaSalva);
    }

    public List<TarefaResponseDTO> listarTodasTarefasDoUsuario(String emailUsuario){
        Usuario usuario= buscarUsuarioPorEmail(emailUsuario);

        return tarefaRepository.findByUsuarioId(usuario.getId())
                .stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    public TarefaResponseDTO atualizar (Long id, String emailUsuario,TarefaRequestDTO dto){
        Tarefa tarefa= buscarTarefaValidandoDono(id, emailUsuario);
        tarefa.setTitulo(dto.getTitulo());
        tarefa.setDescricao(dto.getDescricao());

        Tarefa tarefaAtualizada =tarefaRepository.save(tarefa);
        return converterParaResponseDTO(tarefaAtualizada);
    }

    public void deletar (Long id, String emailUsuario){
        Tarefa tarefa =buscarTarefaValidandoDono(id, emailUsuario);
        tarefaRepository.delete(tarefa);
    }

    private Usuario buscarUsuarioPorEmail(String emailUsuario){
        return usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
    }

    private Tarefa buscarTarefaValidandoDono(Long tarefaId, String emailUsuario){
        Tarefa tarefa= tarefaRepository.findById(tarefaId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa nao encontrada"));

        if (!tarefa.getUsuario().getEmail().equals(emailUsuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para acessar esta tarefa.");
        }
        return tarefa;
    }

    private TarefaResponseDTO converterParaResponseDTO(Tarefa tarefa){
        return new TarefaResponseDTO(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getIsConcluida()
        );
    }
}
