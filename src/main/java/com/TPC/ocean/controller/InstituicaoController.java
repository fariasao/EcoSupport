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

import com.TPC.ocean.model.Instituicao;
import com.TPC.ocean.repository.InstituicaoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/instituicoes")
@CacheConfig(cacheNames = "instituicoes")
@Tag(name = "instituicao", description = "Gerenciamento de instituições")
public class InstituicaoController {
    @Autowired
    InstituicaoRepository repository;

    @Autowired
    PagedResourcesAssembler<Instituicao> assembler;

    @GetMapping
    @Cacheable
    @Operation(summary = "Listar Instituições")
    public PagedModel<EntityModel<Instituicao>> index(@PageableDefault(size = 5) Pageable pageable) {
        Page<Instituicao> page = repository.findAll(pageable);
        return assembler.toModel(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Listar Instituição por ID")
    public EntityModel<Instituicao> show(@PathVariable Long id) {
        Instituicao instituicao = repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Instituição não encontrada")
        );
        return instituicao.toEntityModel();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(allEntries = true)
    @Operation(summary = "Cadastrar Instituição")
    @ApiResponses({
        @ApiResponse(responseCode = "201"),
        @ApiResponse(responseCode = "400")
    })
    public ResponseEntity<Instituicao> create(@RequestBody @Valid Instituicao instituicao) {
        repository.save(instituicao);
        return ResponseEntity
                .created(instituicao.toEntityModel().getRequiredLink("self").toUri())
                .body(instituicao);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @CacheEvict(allEntries = true)
    @Operation(summary = "Deletar Instituição")
    @ApiResponses({
        @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "404"),
        @ApiResponse(responseCode = "401")
    })
    public ResponseEntity<Object> destroy(@PathVariable Long id) {
        repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Instituição não encontrada")
        );
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @CacheEvict(allEntries = true)
    @Operation(summary = "Atualizar Instituição")
    @ApiResponses({
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400"),
        @ApiResponse(responseCode = "401"),
        @ApiResponse(responseCode = "404")
    })
    public ResponseEntity<Instituicao> update(@PathVariable Long id, @RequestBody @Valid Instituicao instituicaoAtualizada) {
        Instituicao instituicao = repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Instituição não encontrada")
        );
        
        instituicao.setNome(instituicaoAtualizada.getNome());
        instituicao.setCnpj(instituicaoAtualizada.getCnpj());
        instituicao.setEmail(instituicaoAtualizada.getEmail());
        instituicao.setTelefone(instituicaoAtualizada.getTelefone());
        instituicao.setEndereco(instituicaoAtualizada.getEndereco());
        
        repository.save(instituicao);
        
        return ResponseEntity.ok(instituicao);
    }
}
