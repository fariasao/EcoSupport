package com.TPC.ocean.model;

import org.springframework.hateoas.EntityModel;
import com.TPC.ocean.controller.PerfilEmpresaController;
import com.TPC.ocean.util.HateoasHelper;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_perfis_empresa")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilEmpresa {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_Usuario", nullable = false)
    @NotNull(message = "{perfilEmpresa.idUsuario.notnull}")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "ID_Empresa", nullable = false)
    @NotNull(message = "{perfilEmpresa.idEmpresa.notnull}")
    private Empresa empresa;

    public EntityModel<PerfilEmpresa> toEntityModel() {
        return HateoasHelper.createModelWithLinks(this, PerfilEmpresaController.class, id);
    }
}
