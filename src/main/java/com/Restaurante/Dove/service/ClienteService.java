package com.Restaurante.Dove.service;

import com.Restaurante.Dove.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import com.Restaurante.Dove.model.ClienteEntity;


import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteEntity save(ClienteEntity cliente) {
        if (cliente.getSenha() != null) {
            return clienteRepository.save(cliente);
        }
            throw new RuntimeException("Senha deve ser preenchida");
    }

    public List<ClienteEntity> findAll() {return clienteRepository.findAll();
    }

    public ClienteEntity findById(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
    }

    public ClienteEntity update(Long id , ClienteEntity cliente) {
        ClienteEntity update = findById(id);

        if (cliente.getNome() != null && !cliente.getNome().isBlank()) {
            update.setNome(cliente.getNome());
        }

        if (cliente.getSenha() != null ){
            update.setSenha(cliente.getSenha());
        }

        return clienteRepository.save(update);
    }

    public void delete (Long id){
        ClienteEntity delete = findById(id);
        clienteRepository.delete(delete);
    }


}

