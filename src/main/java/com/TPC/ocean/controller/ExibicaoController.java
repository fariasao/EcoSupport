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

import com.TPC.ocean.model.Exibicao;
import com.TPC.ocean.repository.ExibicaoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/exibicoes")
@CacheConfig(cacheNames = "exibicoes")
@Tag(name = "exibicao", description = "Gerenciamento de exibições")
public class ExibicaoController {
    @Autowired
    ExibicaoRepository repository;

    @Autowired
    PagedResourcesAssembler<Exibicao> assembler;

    @GetMapping
    @Cacheable
    @Operation(summary = "Listar Exibições")
    public PagedModel<EntityModel<Exibicao>> index(@PageableDefault(size = 5) Pageable pageable) {
        Page<Exibicao> page = repository.findAll(pageable);
        return assembler.toModel(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Listar Exibição por ID")
    public EntityModel<Exibicao> show(@PathVariable Long id) {
        Exibicao exibicao = repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exibição não encontrada")
        );
        return exibicao.toEntityModel();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(allEntries = true)
    @Operation(summary = "Cadastrar Exibição")
    @ApiResponses({
        @ApiResponse(responseCode = "201"),
        @ApiResponse(responseCode = "400")
    })
    public ResponseEntity<Exibicao> create(@RequestBody @Valid Exibicao exibicao) {
        repository.save(exibicao);
        return ResponseEntity
                .created(exibicao.toEntityModel().getRequiredLink("self").toUri())
                .body(exibicao);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @CacheEvict(allEntries = true)
    @Operation(summary = "Deletar Exibição")
    @ApiResponses({
        @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "404"),
        @ApiResponse(responseCode = "401")
    })
    public ResponseEntity<Object> destroy(@PathVariable Long id) {
        repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exibição não encontrada")
        );
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @CacheEvict(allEntries = true)
    @Operation(summary = "Atualizar Exibição")
    @ApiResponses({
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400"),
        @ApiResponse(responseCode = "401"),
        @ApiResponse(responseCode = "404")
    })
    public ResponseEntity<Exibicao> update(@PathVariable Long id, @RequestBody @Valid Exibicao exibicaoAtualizada) {
        Exibicao exibicao = repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exibição não encontrada")
        );
        
        exibicao.setDataExibicao(exibicaoAtualizada.getDataExibicao());
        exibicao.setDescricao(exibicaoAtualizada.getDescricao());
        exibicao.setValor(exibicaoAtualizada.getValor());
        exibicao.setTransacao(exibicaoAtualizada.getTransacao());
        
        repository.save(exibicao);
        
        return ResponseEntity.ok(exibicao);
    }
}