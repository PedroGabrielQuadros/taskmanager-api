package com.capacita.taskmanager.service;

import com.capacita.taskmanager.exception.TarefaNaoEncontradaException;
import com.capacita.taskmanager.model.dto.TarefaRequestDTO;
import com.capacita.taskmanager.model.dto.TarefaResponseDTO;
import com.capacita.taskmanager.model.entity.Tarefa;
import com.capacita.taskmanager.model.entity.Usuario;
import com.capacita.taskmanager.repository.TarefaRepository;
import com.capacita.taskmanager.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("tarefaService")
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    public TarefaService(TarefaRepository tarefaRepository, UsuarioRepository usuarioRepository, ModelMapper modelMapper) {
        this.tarefaRepository = tarefaRepository;
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
    }

    public com.capacita.taskmanager.model.dto.TarefaResponseDTO criarTarefa(com.capacita.taskmanager.model.dto.TarefaRequestDTO dto, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario).orElseThrow();
        Tarefa tarefa = modelMapper.map(dto, Tarefa.class);
        tarefa.setUsuario(usuario);

        tarefa = tarefaRepository.save(tarefa);
        return modelMapper.map(tarefa, com.capacita.taskmanager.model.dto.TarefaResponseDTO.class);
    }

    public List<com.capacita.taskmanager.model.dto.TarefaResponseDTO> listarTarefas(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario).orElseThrow();
        List<Tarefa> tarefas = tarefaRepository.findByUsuarioId(usuario.getId());

        return tarefas.stream()
                .map(tarefa -> modelMapper.map(tarefa, com.capacita.taskmanager.model.dto.TarefaResponseDTO.class))
                .collect(Collectors.toList());
    }

    public TarefaResponseDTO atualizarTarefa(Long id, TarefaRequestDTO dto) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new TarefaNaoEncontradaException("Tarefa não encontrada."));

        tarefa.setTitulo(dto.getTitulo());
        tarefa.setDescricao(dto.getDescricao());

        tarefa = tarefaRepository.save(tarefa);
        return modelMapper.map(tarefa, TarefaResponseDTO.class);
    }

    public void deletarTarefa(Long id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new TarefaNaoEncontradaException("Tarefa não encontrada."));
        tarefaRepository.delete(tarefa);
    }

    public boolean isOwner(String emailUsuario, Long tarefaId) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new TarefaNaoEncontradaException("Tarefa não encontrada."));
        return tarefa.getUsuario().getEmail().equals(emailUsuario);
    }
}