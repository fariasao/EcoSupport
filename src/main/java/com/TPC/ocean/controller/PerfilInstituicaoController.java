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
@Tag(name = "Perfil-Instituição", description = "Gerenciamento de perfis de instituições")
public class PerfilInstituicaoController {
    @Autowired
    PerfilInstituicaoRepository repository;

    @Autowired
    PagedResourcesAssembler<PerfilInstituicao> assembler;

    @GetMapping
    @Cacheable
    @Operation(
        summary = "Listar Perfis de Instituições",
        description = "Retorna uma lista paginada de perfis de instituições"
    )
    @ApiResponses({ 
        @ApiResponse(responseCode = "200", description = "Perfis de instituições listados"),
        @ApiResponse(responseCode = "404", description = "Perfis de instituições não encontrados")
    })
    public PagedModel<EntityModel<PerfilInstituicao>> index(@PageableDefault(size = 5) Pageable pageable) {
        Page<PerfilInstituicao> page = repository.findAll(pageable);
        return assembler.toModel(page);
    }

    @GetMapping("{id}")
    @Operation(
        summary = "Listar Perfil de Instituição por ID",
        description = "Retorna um perfil de instituição específico"
    )
    @ApiResponses({ 
        @ApiResponse(responseCode = "200", description = "Perfil de instituição listado"),
        @ApiResponse(responseCode = "404", description = "Perfil de instituição não encontrado")
    })
    public EntityModel<PerfilInstituicao> show(@PathVariable Long id) {
        PerfilInstituicao perfilInstituicao = repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil de instituição não encontrado")
        );
        return perfilInstituicao.toEntityModel();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(allEntries = true)
    @Operation(
        summary = "Cadastrar Perfil de Instituição",
        description = "Cadastra um novo perfil de instituição"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Perfil de instituição criado"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida")
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
    @Operation(
        summary = "Deletar Perfil de Instituição",
        description = "Deleta um perfil de instituição"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Perfil de instituição deletado"),
        @ApiResponse(responseCode = "404", description = "Perfil de instituição não encontrado"),
        @ApiResponse(responseCode = "401", description = "Sem autorização")
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
    @Operation(
        summary = "Atualizar Perfil de Instituição",
        description = "Atualiza um perfil de instituição específico"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Perfil de instituição atualizado"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida"),
        @ApiResponse(responseCode = "401", description = "Sem autorização"),
        @ApiResponse(responseCode = "404", description = "Perfil de instituição não encontrado")
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
