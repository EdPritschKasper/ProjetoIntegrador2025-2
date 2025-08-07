package com.Restaurante.Dove.service;

import com.Restaurante.Dove.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.Restaurante.Dove.model.Cliente;


import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente save(Cliente cliente) {
        if (cliente.getSenha() != null) {
            return clienteRepository.save(cliente);
        }
            throw new RuntimeException("Senha deve ser preenchida");
    }

    public List<Cliente> findAll() {return clienteRepository.findAll();
    }

    public Cliente findById(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
    }

    public Cliente update(Long id , Cliente cliente) {
        Cliente update = findById(id);

        if (cliente.getNome() != null && !cliente.getNome().isBlank()) {
            update.setNome(cliente.getNome());
        }

        if (cliente.getSenha() != null ){
            update.setSenha(cliente.getSenha());
        }

        return clienteRepository.save(update);
    }

    public void delete (Long id){
        Cliente delete = findById(id);
        clienteRepository.delete(delete);
    }


}

