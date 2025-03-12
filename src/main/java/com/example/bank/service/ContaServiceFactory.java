package com.example.bank.service;

public class ContaServiceFactory {

    private static IContaService instance;

    private ContaServiceFactory() {
    }

    public static IContaService getContaService() {
        if (instance == null) {
            instance = new ContaService();
        }
        return instance;
    }
    
}
