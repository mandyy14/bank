package com.example.bank.model;

import java.time.LocalDate;

public class Conta {   

    private final Long id;
    private final String numero;
    private final String agencia;
    private final String nomeTitular;
    private final String cpfTitular;
    private final LocalDate dataAbertura;
    private final String tipo;

    private static long contadorId = 1;
    private double saldo;
    private boolean ativa;    


    // Construtores:

    public Conta(String numero, String agencia, String nomeTitular, String cpfTitular, LocalDate dataAbertura, double saldo, String tipo) {
        this.id = contadorId++;
        this.numero = numero;
        this.agencia = agencia;
        this.nomeTitular = nomeTitular;
        this.cpfTitular = cpfTitular;
        this.dataAbertura = dataAbertura;
        this.saldo = saldo >= 0 ? saldo : 0;
        this.ativa = true;
        this.tipo = (tipo.equals("Corrente") || tipo.equals("Poupança") || tipo.equals("Salário")) ? tipo : "Corrente";
    }


    // Getters e Setters:

    public Long getId() { return id; }

    public String getNumero() { return numero; }

    public String getAgencia() { return agencia; }

    public String getNomeTitular() { return nomeTitular; }

    public String getCpfTitular() { return cpfTitular; }

    public LocalDate getDataAbertura() { return dataAbertura; }

    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public boolean isAtiva() { return ativa; }
    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public String getTipo() { return tipo; }

}
