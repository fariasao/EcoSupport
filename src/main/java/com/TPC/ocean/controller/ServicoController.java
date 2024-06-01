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

import com.TPC.ocean.model.Servico;
import com.TPC.ocean.repository.ServicoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/servicos")
@CacheConfig(cacheNames = "servicos")
@Tag(name = "servico", description = "Gerenciamento de serviços")
public class ServicoController {
    @Autowired
    ServicoRepository repository;

    @Autowired
    PagedResourcesAssembler<Servico> assembler;

    @GetMapping
    @Cacheable
    @Operation(summary = "Listar Serviços")
    public PagedModel<EntityModel<Servico>> index(@PageableDefault(size = 5) Pageable pageable) {
        Page<Servico> page = repository.findAll(pageable);
        return assembler.toModel(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Listar Serviço por ID")
    public EntityModel<Servico> show(@PathVariable Long id) {
        Servico servico = repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado")
        );
        return servico.toEntityModel();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(allEntries = true)
    @Operation(summary = "Cadastrar Serviço")
    @ApiResponses({
        @ApiResponse(responseCode = "201"),
        @ApiResponse(responseCode = "400")
    })
    public ResponseEntity<Servico> create(@RequestBody @Valid Servico servico) {
        repository.save(servico);
        return ResponseEntity
                .created(servico.toEntityModel().getRequiredLink("self").toUri())
                .body(servico);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @CacheEvict(allEntries = true)
    @Operation(summary = "Deletar Serviço")
    @ApiResponses({
        @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "404"),
        @ApiResponse(responseCode = "401")
    })
    public ResponseEntity<Object> destroy(@PathVariable Long id) {
        repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado")
        );
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @CacheEvict(allEntries = true)
    @Operation(summary = "Atualizar Serviço")
    @ApiResponses({
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400"),
        @ApiResponse(responseCode = "401"),
        @ApiResponse(responseCode = "404")
    })
    public ResponseEntity<Servico> update(@PathVariable Long id, @RequestBody @Valid Servico servicoAtualizado) {
        Servico servico = repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado")
        );
        
        servico.setDataServico(servicoAtualizado.getDataServico());
        servico.setDescricao(servicoAtualizado.getDescricao());
        servico.setStatus(servicoAtualizado.getStatus());
        servico.setEmpresa(servicoAtualizado.getEmpresa());
        
        repository.save(servico);
        
        return ResponseEntity.ok(servico);
    }
}
