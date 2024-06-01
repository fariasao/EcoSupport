package com.TPC.ocean.model;

import org.springframework.hateoas.EntityModel;
import com.TPC.ocean.controller.ContratoController;
import com.TPC.ocean.util.HateoasHelper;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contrato {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_Empresa", nullable = false)
    private Empresa empresa;

    @NotBlank(message = "{contrato.tipoContrato.notblank}")
    private String tipoContrato;

    @NotNull(message = "{contrato.dataInicio.notnull}")
    private LocalDate dataInicio;

    private LocalDate dataFim;
    private Double valor;

    @NotBlank(message = "{contrato.status.notblank}")
    private String status;

    public EntityModel<Contrato> toEntityModel() {
        return HateoasHelper.createModelWithLinks(this, ContratoController.class, id);
    }
}
