package com.Restaurante.Dove.service;

import com.Restaurante.Dove.model.PedidoEntity;
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
        if (cliente.getNome() != null && cliente.getNome().trim().isEmpty()){
            throw new IllegalArgumentException("Você deve preencher o nome");
        }

        if (cliente.getEmail() != null && cliente.getEmail().trim().isEmpty() && (!cliente.getEmail().endsWith("@gmail.com") &&
                !cliente.getEmail().endsWith("@hotmail.com"))) {
            throw new IllegalArgumentException("O email deve ser @gmail ou @hotmail");
        }

        if (cliente.getSenha() != null && cliente.getSenha().trim().isEmpty() &&
                cliente.getSenha().length() < 3) {

            throw new IllegalArgumentException("Senha deve ter mais de 3 caracters");
        }

        return clienteRepository.save(cliente);

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
    public long getPedidosById(long id){
        return clienteRepository.getPedidosById(id);
    }


    public void delete (Long id){
        ClienteEntity delete = findById(id);
        clienteRepository.delete(delete);
    }


}

