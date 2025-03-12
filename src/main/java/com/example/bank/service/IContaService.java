package com.example.bank.service;

import java.util.List;

import com.example.bank.model.Conta;

public interface IContaService {

    Conta criarConta(Conta conta);

    Conta buscarConta(Long id);

    Conta buscarContaPorCpf(String cpf);

    List<Conta> buscarTodasContas();

    boolean depositar(Long id, double valor);

    boolean sacar(Long id, double valor);

    boolean realizarPix(Long origemId, Long destinoId, double valor);

    boolean encerrarConta(Long id);
}
