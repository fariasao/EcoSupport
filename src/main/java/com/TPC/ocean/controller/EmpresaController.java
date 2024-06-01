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

import com.TPC.ocean.model.Empresa;
import com.TPC.ocean.repository.EmpresaRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/empresas")
@CacheConfig(cacheNames = "empresas")
@Tag(name = "empresa", description = "Gerenciamento de empresas")
public class EmpresaController {
    @Autowired
    EmpresaRepository repository;

    @Autowired
    PagedResourcesAssembler<Empresa> assembler;

    @GetMapping
    @Cacheable
    @Operation(summary = "Listar Empresas")
    public PagedModel<EntityModel<Empresa>> index(@PageableDefault(size = 5) Pageable pageable) {
        Page<Empresa> page = repository.findAll(pageable);
        return assembler.toModel(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "Listar Empresa por ID")
    public EntityModel<Empresa> show(@PathVariable Long id) {
        Empresa empresa = repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada")
        );
        return empresa.toEntityModel();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(allEntries = true)
    @Operation(summary = "Cadastrar Empresa")
    @ApiResponses({
        @ApiResponse(responseCode = "201"),
        @ApiResponse(responseCode = "400")
    })
    public ResponseEntity<Empresa> create(@RequestBody @Valid Empresa empresa) {
        repository.save(empresa);
        return ResponseEntity
                .created(empresa.toEntityModel().getRequiredLink("self").toUri())
                .body(empresa);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    @CacheEvict(allEntries = true)
    @Operation(summary = "Deletar Empresa")
    @ApiResponses({
        @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "404"),
        @ApiResponse(responseCode = "401")
    })
    public ResponseEntity<Object> destroy(@PathVariable Long id) {
        repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada")
        );
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @CacheEvict(allEntries = true)
    @Operation(summary = "Atualizar Empresa")
    @ApiResponses({
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400"),
        @ApiResponse(responseCode = "401"),
        @ApiResponse(responseCode = "404")
    })
    public ResponseEntity<Empresa> update(@PathVariable Long id, @RequestBody @Valid Empresa empresaAtualizada) {
        Empresa empresa = repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada")
        );
        
        empresa.setNome(empresaAtualizada.getNome());
        empresa.setCnpj(empresaAtualizada.getCnpj());
        empresa.setEmail(empresaAtualizada.getEmail());
        empresa.setTelefone(empresaAtualizada.getTelefone());
        empresa.setEndereco(empresaAtualizada.getEndereco());
        
        repository.save(empresa);
        
        return ResponseEntity.ok(empresa);
    }
}
