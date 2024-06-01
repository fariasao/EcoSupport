package com.TPC.ocean.controller;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.TPC.ocean.model.Transacao;
import com.TPC.ocean.repository.TransacaoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/transacoes")
@CacheConfig(cacheNames = "transacoes")
@Tag(name = "transacao", description = "Gerenciamento de transações")
public class TransacaoController {
    @Autowired
    TransacaoRepository repository;

    @Autowired
    PagedResourcesAssembler<Transacao> assembler;

    @GetMapping
    @Cacheable
    @Operation(summary = "Listar Transações")
    public PagedModel<EntityModel<Transacao>> index(@PageableDefault(size = 5) Pageable pageable) {
        Page<Transacao> page = repository.findAll(pageable);
        return assembler.toModel(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Listar Transação por ID")
    public EntityModel<Transacao> show(@PathVariable Long id) {
        Transacao transacao = repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transação não encontrada")
        );
        return transacao.toEntityModel();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(allEntries = true)
    @Operation(summary = "Cadastrar Transação")
    @ApiResponses({
        @ApiResponse(responseCode = "201"),
        @ApiResponse(responseCode = "400")
    })
    public ResponseEntity<Transacao> create(@RequestBody @Valid Transacao transacao) {
        repository.save(transacao);
        return ResponseEntity
                .created(transacao.toEntityModel().getRequiredLink("self").toUri())
                .body(transacao);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @CacheEvict(allEntries = true)
    @Operation(summary = "Deletar Transação")
    @ApiResponses({
        @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "404"),
        @ApiResponse(responseCode = "401")
    })
    public ResponseEntity<Object> destroy(@PathVariable Long id) {
        repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transação não encontrada")
        );
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @CacheEvict(allEntries = true)
    @Operation(summary = "Atualizar Transação")
    @ApiResponses({
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400"),
        @ApiResponse(responseCode = "401"),
        @ApiResponse(responseCode = "404")
    })
    public ResponseEntity<Transacao> update(@PathVariable Long id, @RequestBody @Valid Transacao transacaoAtualizada) {
        Transacao transacao = repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transação não encontrada")
        );
        
        transacao.setData(transacaoAtualizada.getData());
        transacao.setValor(transacaoAtualizada.getValor());
        transacao.setDescricao(transacaoAtualizada.getDescricao());
        transacao.setContrato(transacaoAtualizada.getContrato());
        
        repository.save(transacao);
        
        return ResponseEntity.ok(transacao);
    }
}
