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

import com.TPC.ocean.model.Contrato;
import com.TPC.ocean.repository.ContratoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/contratos")
@CacheConfig(cacheNames = "contratos")
@Tag(name = "contrato", description = "Gerenciamento de contratos")
public class ContratoController {
    @Autowired
    ContratoRepository repository;

    @Autowired
    PagedResourcesAssembler<Contrato> assembler;

    @GetMapping
    @Cacheable
    @Operation(summary = "Listar Contratos")
    public PagedModel<EntityModel<Contrato>> index(@PageableDefault(size = 5) Pageable pageable) {
        Page<Contrato> page = repository.findAll(pageable);
        return assembler.toModel(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Listar Contrato por ID")
    public EntityModel<Contrato> show(@PathVariable Long id) {
        Contrato contrato = repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contrato não encontrado")
        );
        return contrato.toEntityModel();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(allEntries = true)
    @Operation(summary = "Cadastrar Contrato")
    @ApiResponses({
        @ApiResponse(responseCode = "201"),
        @ApiResponse(responseCode = "400")
    })
    public ResponseEntity<Contrato> create(@RequestBody @Valid Contrato contrato) {
        repository.save(contrato);
        return ResponseEntity
                .created(contrato.toEntityModel().getRequiredLink("self").toUri())
                .body(contrato);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @CacheEvict(allEntries = true)
    @Operation(summary = "Deletar Contrato")
    @ApiResponses({
        @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "404"),
        @ApiResponse(responseCode = "401")
    })
    public ResponseEntity<Object> destroy(@PathVariable Long id) {
        repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contrato não encontrado")
        );
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @CacheEvict(allEntries = true)
    @Operation(summary = "Atualizar Contrato")
    @ApiResponses({
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400"),
        @ApiResponse(responseCode = "401"),
        @ApiResponse(responseCode = "404")
    })
    public ResponseEntity<Contrato> update(@PathVariable Long id, @RequestBody @Valid Contrato contratoAtualizado) {
        Contrato contrato = repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contrato não encontrado")
        );
        
        contrato.setTipoContrato(contratoAtualizado.getTipoContrato());
        contrato.setDataInicio(contratoAtualizado.getDataInicio());
        contrato.setDataFim(contratoAtualizado.getDataFim());
        contrato.setValor(contratoAtualizado.getValor());
        contrato.setStatus(contratoAtualizado.getStatus());
        contrato.setEmpresa(contratoAtualizado.getEmpresa());
        
        repository.save(contrato);
        
        return ResponseEntity.ok(contrato);
    }
}
