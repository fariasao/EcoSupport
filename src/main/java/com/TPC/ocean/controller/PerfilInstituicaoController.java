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

import com.TPC.ocean.model.PerfilInstituicao;
import com.TPC.ocean.repository.PerfilInstituicaoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/perfil-instituicao")
@CacheConfig(cacheNames = "perfil-instituicao")
@Tag(name = "perfilInstituicao", description = "Gerenciamento de perfis de instituições")
public class PerfilInstituicaoController {
    @Autowired
    PerfilInstituicaoRepository repository;

    @Autowired
    PagedResourcesAssembler<PerfilInstituicao> assembler;

    @GetMapping
    @Cacheable
    @Operation(summary = "Listar Perfis de Instituições")
    public PagedModel<EntityModel<PerfilInstituicao>> index(@PageableDefault(size = 5) Pageable pageable) {
        Page<PerfilInstituicao> page = repository.findAll(pageable);
        return assembler.toModel(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Listar Perfil de Instituição por ID")
    public EntityModel<PerfilInstituicao> show(@PathVariable Long id) {
        PerfilInstituicao perfilInstituicao = repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil de instituição não encontrado")
        );
        return perfilInstituicao.toEntityModel();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(allEntries = true)
    @Operation(summary = "Cadastrar Perfil de Instituição")
    @ApiResponses({
        @ApiResponse(responseCode = "201"),
        @ApiResponse(responseCode = "400")
    })
    public ResponseEntity<PerfilInstituicao> create(@RequestBody @Valid PerfilInstituicao perfilInstituicao) {
        repository.save(perfilInstituicao);
        return ResponseEntity
                .created(perfilInstituicao.toEntityModel().getRequiredLink("self").toUri())
                .body(perfilInstituicao);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @CacheEvict(allEntries = true)
    @Operation(summary = "Deletar Perfil de Instituição")
    @ApiResponses({
        @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "404"),
        @ApiResponse(responseCode = "401")
    })
    public ResponseEntity<Object> destroy(@PathVariable Long id) {
        repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil de instituição não encontrado")
        );
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @CacheEvict(allEntries = true)
    @Operation(summary = "Atualizar Perfil de Instituição")
    @ApiResponses({
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400"),
        @ApiResponse(responseCode = "401"),
        @ApiResponse(responseCode = "404")
    })
    public ResponseEntity<PerfilInstituicao> update(@PathVariable Long id, @RequestBody @Valid PerfilInstituicao perfilInstituicaoAtualizado) {
        PerfilInstituicao perfilInstituicao = repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil de instituição não encontrado")
        );
        
        perfilInstituicao.setUsuario(perfilInstituicaoAtualizado.getUsuario());
        perfilInstituicao.setInstituicao(perfilInstituicaoAtualizado.getInstituicao());
        
        repository.save(perfilInstituicao);
        
        return ResponseEntity.ok(perfilInstituicao);
    }
}
