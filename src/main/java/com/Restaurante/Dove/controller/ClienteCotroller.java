package com.Restaurante.Dove.controller;

import com.Restaurante.Dove.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.Restaurante.Dove.service.ClienteService;

import java.util.List;

@RestController
@RequestMapping("api/cliente")
@RequiredArgsConstructor
public class ClienteCotroller {

    private final ClienteService clienteService;

    @GetMapping("/findAll")
    public ResponseEntity<List<Cliente>> findAll(){

        try{
             var result = clienteService.findAll();
             return new ResponseEntity<>(result , HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(null , HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/findById/{id}")
    public ResponseEntity<Cliente> findById(@PathVariable long id){
        try {
            var result = clienteService.findById(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/save")
    public ResponseEntity<Cliente> save(@RequestBody Cliente cliente){

        try {
            var result = clienteService.save(cliente);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Cliente> update(@PathVariable Long id , @RequestBody Cliente cliente){

        try {
            var result = clienteService.update(id, cliente);
            return new ResponseEntity<>(null, HttpStatus.OK);
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
