package com.TPC.ocean.model;

import org.springframework.hateoas.EntityModel;
import com.TPC.ocean.controller.PerfilInstituicaoController;
import com.TPC.ocean.util.HateoasHelper;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_perfis_instituicao")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilInstituicao {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_Usuario", nullable = false)
    @NotNull(message = "{perfilInstituicao.idUsuario.notnull}")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "ID_Instituicao", nullable = false)
    @NotNull(message = "{perfilInstituicao.idInstituicao.notnull}")
    private Instituicao instituicao;

    public EntityModel<PerfilInstituicao> toEntityModel() {
        return HateoasHelper.createModelWithLinks(this, PerfilInstituicaoController.class, id);
    }
}
