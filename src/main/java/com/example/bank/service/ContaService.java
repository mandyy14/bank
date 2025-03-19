package com.example.bank.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.bank.model.Conta;

 class ContaService implements IContaService {

    private final Map<Long, Conta> contas = new HashMap<>();

    @Override
    public Conta criarConta(Conta conta) {
        contas.put(conta.getId(), conta);
        return conta;
    }

    @Override
    public Conta buscarConta(Long id) {
        return contas.get(id);
    }

    @Override
    public Conta buscarContaPorCpf(String cpf) {
        for (Conta conta : contas.values()) {
            if (conta.getCpfTitular().equals(cpf)) {
                return conta;
            }
        }
        return null;
    }

    @Override
    public List<Conta> buscarTodasContas() {
        return new ArrayList<>(contas.values());
    }

    @Override
    public boolean depositar(Long id, double valor) {
        Conta conta = buscarConta(id);
        if (conta != null && valor > 0) {
            conta.setSaldo(conta.getSaldo() + valor);
            return true;
        }
        return false;
    }

    @Override
    public boolean sacar(Long id, double valor) {
        Conta conta = buscarConta(id);
        if (conta != null && valor > 0 && conta.getSaldo() >= valor) {
            conta.setSaldo(conta.getSaldo() - valor);
            return true;
        }
        return false;
    }

    @Override
    public boolean realizarPix(Long origemId, Long destinoId, double valor) {
        Conta origem = buscarConta(origemId);
        Conta destino = buscarConta(destinoId);

        if (origem != null && destino != null && origem.getSaldo() >= valor && valor > 0) {
            origem.setSaldo(origem.getSaldo() - valor);
            destino.setSaldo(destino.getSaldo() + valor);
            return true;
        }
        return false;
    }

    @Override
    public boolean encerrarConta(Long id) {
        Conta conta = buscarConta(id);
        if (conta != null) {
            conta.setAtiva(false);
            return true;
        }
        return false;
    }
}
