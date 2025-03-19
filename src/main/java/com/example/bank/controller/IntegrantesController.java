package com.example.bank.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class IntegrantesController {
    @GetMapping
    public String infoProjeto() {
        return "Projeto Bank - Integrantes: Amanda Mesquita e Journey Tiago";
    }
}
