package com.example.bank.controller;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final IContaService contaService = ContaServiceFactory.getContaService();

    @GetMapping
    public ResponseEntity<List<Conta>> listarContas() {
        log.info("Listando todas as contas cadastradas.");
        List<Conta> contas = contaService.buscarTodasContas();
        return ResponseEntity.ok(contas);
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Conta conta) {
        log.info("Tentando criar uma nova conta para {}", conta.getNomeTitular());
        if (conta.getNomeTitular() == null || conta.getNomeTitular().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Erro: Nome do titular é obrigatório.");
        }
        if (conta.getCpfTitular() == null || conta.getCpfTitular().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Erro: CPF do titular é obrigatório.");
        }
        if (conta.getDataAbertura().isAfter(LocalDate.now())) {
            return ResponseEntity.badRequest().body("Erro: Data de abertura não pode ser no futuro.");
        }
        
        Conta novaConta = contaService.criarConta(conta);
        log.info("Conta criada com sucesso: {}", novaConta.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conta> get(@PathVariable Long id) {
        log.info("Buscando conta com ID {}", id);
        Conta conta = contaService.buscarConta(id);
        return conta != null ? ResponseEntity.ok(conta) : ResponseEntity.notFound().build();
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Conta> getByCpf(@PathVariable String cpf) {
        log.info("Buscando conta pelo CPF {}", cpf);
        Conta conta = contaService.buscarContaPorCpf(cpf);
        return conta != null ? ResponseEntity.ok(conta) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/encerrar")
    public ResponseEntity<Void> encerrarConta(@PathVariable Long id) {
        log.info("Encerrando conta com ID {}", id);
        contaService.encerrarConta(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/deposito")
    public ResponseEntity<?> depositar(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        double valor = request.get("valor");
        log.info("Realizando depósito de R$ {} na conta ID {}", valor, id);
        
        if (valor <= 0) {
            return ResponseEntity.badRequest().body("Erro: Valor do depósito deve ser maior que zero.");
        }
        
        boolean sucesso = contaService.depositar(id, valor);
        Conta conta = contaService.buscarConta(id);
        return sucesso ? ResponseEntity.ok(conta) : ResponseEntity.badRequest().body("Erro ao realizar depósito.");
    }

    @PostMapping("/{id}/saque")
    public ResponseEntity<?> sacar(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        double valor = request.get("valor");
        log.info("Realizando saque de R$ {} na conta ID {}", valor, id);

        if (valor <= 0 || !contaService.sacar(id, valor)) {
            return ResponseEntity.badRequest().body("Erro: Saque não pode ser realizado.");
        }
        
        Conta conta = contaService.buscarConta(id);
        return ResponseEntity.ok(conta);
    }

    @PostMapping("/pix")
    public ResponseEntity<?> realizarPix(@RequestBody Map<String, Object> request) {
        Long origemId = Long.valueOf(request.get("origemId").toString());
        Long destinoId = Long.valueOf(request.get("destinoId").toString());
        double valor = Double.parseDouble(request.get("valor").toString());

        log.info("Realizando PIX de R$ {} da conta {} para conta {}", valor, origemId, destinoId);
        
        if (valor <= 0) {
            return ResponseEntity.badRequest().body("Erro: Valor do PIX deve ser maior que zero.");
        }
        
        boolean sucesso = contaService.realizarPix(origemId, destinoId, valor);
        if (!sucesso) {
            return ResponseEntity.badRequest().body("Erro: PIX não pôde ser realizado. Verifique saldo e contas.");
        }
        
        Conta origemAtualizada = contaService.buscarConta(origemId);
        log.info("PIX realizado com sucesso. Saldo atualizado na conta de origem ID {}", origemId);
        return ResponseEntity.ok(origemAtualizada);
    }
}
