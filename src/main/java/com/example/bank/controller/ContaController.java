package com.example.bank.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException; //retirar 
//implentar a service, usar o contrutor da factory para implementar a service 

import com.example.bank.model.Conta;

@RestController
@RequestMapping("/contas")
public class ContaController {

    private Logger log = LoggerFactory.getLogger(getClass());
    private List<Conta> repository = new ArrayList<>();

    @GetMapping
    public List<Conta> listarContas() {
        return repository;
    }

    @PostMapping
    public ResponseEntity<Conta> criar(@RequestBody Conta conta) {
        log.info("Cadastrando conta para " + conta.getNomeTitular());
        repository.add(conta);
        return ResponseEntity.status(HttpStatus.CREATED).body(conta);
    }

    @GetMapping("/{id}")
    public Conta get(@PathVariable Long id) {
        log.info("Buscando conta com id " + id);
        return getConta(id);
    }

    @GetMapping("/cpf/{cpf}")
    public Conta getByCpf(@PathVariable String cpf) {
        log.info("Buscando conta com cpf " + cpf);
        return repository.stream()
                .filter(c -> c.getCpfTitular().equals(cpf))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{id}/encerrar")
    public ResponseEntity<Void> encerrarConta(@PathVariable Long id) {
        Conta conta = getConta(id);
        conta.setAtiva(false);
        log.info("Encerrando conta com id " + id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/deposito")
    public Conta depositar(@PathVariable Long id, @RequestBody double valor) {
        Conta conta = getConta(id);
        if (valor <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor de depósito inválido");
        }
        conta.setSaldo(conta.getSaldo() + valor);
        log.info("Depósito de R${} realizado na conta id {}", valor, id);
        return conta;
    }

    @PostMapping("/{id}/saque")
    public Conta sacar(@PathVariable Long id, @RequestBody double valor) {
        Conta conta = getConta(id);
        if (valor <= 0 || conta.getSaldo() < valor) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente ou valor inválido para saque");
        }
        conta.setSaldo(conta.getSaldo() - valor);
        log.info("Saque de R${} realizado na conta id {}", valor, id);
        return conta;
    }

    private Conta getConta(Long id) {
        return repository.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
