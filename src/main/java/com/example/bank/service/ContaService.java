package com.example.bank.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.example.bank.model.Conta;

class ContaService implements IContaService {

    private final Map<Long, Conta> contas = new HashMap<>();
    

    @Override
    public Conta criarConta(Conta conta) {
        contas.put(conta.getId(), conta);
        return conta;
    }


    @Override
    public Optional<Conta> buscarConta(Long id) {
        return Optional.ofNullable(contas.get(id));
    }


    @Override
    public boolean depositar(Long id, double valor) {
        Conta conta = contas.get(id);
        if (conta != null && valor > 0) {
            conta.setSaldo(conta.getSaldo() + valor);
            return true;
        }
        return false;
    }
    

    @Override
    public boolean sacar(Long id, double valor) {
        Conta conta = contas.get(id);
        if (conta != null && valor > 0 && conta.getSaldo() >= valor) {
            conta.setSaldo(conta.getSaldo() - valor);
            return true;
        }
        return false;
    }


    @Override
    public boolean encerrarConta(Long id) {
        Conta conta = contas.get(id);
        if (conta != null) {
            conta.setAtiva(false);
            return true;
        }
        return false;
    }

}
