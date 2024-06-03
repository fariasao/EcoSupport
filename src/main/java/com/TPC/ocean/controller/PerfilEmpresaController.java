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

import com.TPC.ocean.model.PerfilEmpresa;
import com.TPC.ocean.repository.PerfilEmpresaRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/perfil-empresa")
@CacheConfig(cacheNames = "perfil-empresa")
@Tag(name = "Perfil-Empresa", description = "Gerenciamento de perfis de empresas")
public class PerfilEmpresaController {
    @Autowired
    PerfilEmpresaRepository repository;

    @Autowired
    PagedResourcesAssembler<PerfilEmpresa> assembler;

    @GetMapping
    @Cacheable
    @Operation(
        summary = "Listar Perfis de Empresas",
        description = "Retorna uma lista paginada de perfis de empresas"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Perfis de empresas listados"),
        @ApiResponse(responseCode = "404", description = "Perfis de empresas não encontrados")
    })
    public PagedModel<EntityModel<PerfilEmpresa>> index(@PageableDefault(size = 5) Pageable pageable) {
        Page<PerfilEmpresa> page = repository.findAll(pageable);
        return assembler.toModel(page);
    }

    @GetMapping("{id}")
    @Operation(
        summary = "Listar Perfil de Empresa por ID",
        description = "Retorna um perfil de empresa específico"
    )
    @ApiResponses({ 
        @ApiResponse(responseCode = "200", description = "Perfil de empresa listado"),
        @ApiResponse(responseCode = "404", description = "Perfil de empresa não encontrado")
    })
    public EntityModel<PerfilEmpresa> show(@PathVariable Long id) {
        PerfilEmpresa perfilEmpresa = repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil de empresa não encontrado")
        );
        return perfilEmpresa.toEntityModel();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(allEntries = true)
    @Operation(
        summary = "Cadastrar Perfil de Empresa",
        description = "Cadastra um novo perfil de empresa"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Perfil de empresa criado"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    public ResponseEntity<PerfilEmpresa> create(@RequestBody @Valid PerfilEmpresa perfilEmpresa) {
        repository.save(perfilEmpresa);
        return ResponseEntity
                .created(perfilEmpresa.toEntityModel().getRequiredLink("self").toUri())
                .body(perfilEmpresa);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @CacheEvict(allEntries = true)
    @Operation(
        summary = "Deletar Perfil de Empresa",
        description = "Deleta um perfil de empresa específico"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Perfil de empresa deletado"),
        @ApiResponse(responseCode = "404", description = "Perfil de empresa não encontrado"),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado")
    })
    public ResponseEntity<Object> destroy(@PathVariable Long id) {
        repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil de empresa não encontrado")
        );
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @CacheEvict(allEntries = true)
    @Operation(
        summary = "Atualizar Perfil de Empresa",
        description = "Atualiza um perfil de empresa específico"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Perfil de empresa atualizado"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida"),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado"),
        @ApiResponse(responseCode = "404", description = "Perfil de empresa não encontrado")
    })
    public ResponseEntity<PerfilEmpresa> update(@PathVariable Long id, @RequestBody @Valid PerfilEmpresa perfilEmpresaAtualizado) {
        PerfilEmpresa perfilEmpresa = repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil de empresa não encontrado")
        );
        
        perfilEmpresa.setUsuario(perfilEmpresaAtualizado.getUsuario());
        perfilEmpresa.setEmpresa(perfilEmpresaAtualizado.getEmpresa());
        
        repository.save(perfilEmpresa);
        
        return ResponseEntity.ok(perfilEmpresa);
    }
}
