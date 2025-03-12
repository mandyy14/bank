package com.example.bank.service;

import java.util.Optional;

import com.example.bank.model.Conta;

public interface IContaService {

    Conta criarConta(Conta conta);

    Optional<Conta> buscarConta(Long id);

    boolean depositar(Long id, double valor);

    boolean sacar(Long id, double valor);

    boolean encerrarConta(Long id);
    
}
