package com.Restaurante.Dove.controller;

import com.Restaurante.Dove.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.Restaurante.Dove.service.ClienteService;

import java.util.List;

@RestController
@RequestMapping("/api/cliente")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping("/findAll")
    public ResponseEntity<List<ClienteEntity>> findAll(){

        try{
             var result = clienteService.findAll();
             return new ResponseEntity<>(result , HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(null , HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<ClienteEntity> findById(@PathVariable Long id){
        try {
            var result = clienteService.findById(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/findPedido/{id}")
    public ResponseEntity<Long> findPedidoById(@PathVariable Long id){

        try {
            var result = clienteService.getPedidosById(id);
            return new ResponseEntity<>(result , HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>(null , HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<ClienteEntity> save(@RequestBody ClienteEntity cliente){

        try {
            var result = clienteService.save(cliente);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ClienteEntity> update(@PathVariable Long id , @RequestBody ClienteEntity cliente){

        try {
            var result = clienteService.update(id, cliente);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        try {
            clienteService.delete(id);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            return new ResponseEntity<>(null , HttpStatus.BAD_REQUEST);
        }
    }
}
