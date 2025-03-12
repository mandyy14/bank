package com.example.bank.controller;

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

import com.example.bank.model.Conta;
import com.example.bank.service.ContaServiceFactory;
import com.example.bank.service.IContaService;

@RestController
@RequestMapping("/contas")
public class ContaController {

    private Logger log = LoggerFactory.getLogger(getClass());
    private IContaService contaService = ContaServiceFactory.getContaService();

    @GetMapping
    public List<Conta> listarContas() {
        return contaService.buscarTodasContas();
    }

    @PostMapping
    public ResponseEntity<Conta> criar(@RequestBody Conta conta) {
        log.info("Cadastrando conta para " + conta.getNomeTitular());
        Conta novaConta = contaService.criarConta(conta);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);
    }

    @GetMapping("/{id}")
    public Conta get(@PathVariable Long id) {
        log.info("Buscando conta com id " + id);
        return contaService.buscarConta(id);
    }

    @GetMapping("/cpf/{cpf}")
    public Conta getByCpf(@PathVariable String cpf) {
        log.info("Buscando conta com cpf " + cpf);
        return contaService.buscarContaPorCpf(cpf);
    }

    @PatchMapping("/{id}/encerrar")
    public ResponseEntity<Void> encerrarConta(@PathVariable Long id) {
        contaService.encerrarConta(id);
        log.info("Encerrando conta com id " + id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/deposito")
    public ResponseEntity<Conta> depositar(@PathVariable Long id, @RequestBody double valor) {
        ResponseEntity<Conta> response = (valor <= 0) ? 
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null) : null;
        if (response != null) {
            return response;
        }
        boolean sucesso = contaService.depositar(id, valor);
        Conta conta = contaService.buscarConta(id);
        log.info("Depósito de R${} realizado na conta id {}", valor, id);
        return sucesso ? ResponseEntity.ok(conta) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @PostMapping("/{id}/saque")
    public ResponseEntity<Conta> sacar(@PathVariable Long id, @RequestBody double valor) {
        ResponseEntity<Conta> response = (valor <= 0 || !contaService.sacar(id, valor)) ? 
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null) : null;
        if (response != null) {
            return response;
        }
        Conta conta = contaService.buscarConta(id);
        log.info("Saque de R${} realizado na conta id {}", valor, id);
        return ResponseEntity.ok(conta);
    }

}
