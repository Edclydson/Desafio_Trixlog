package com.api.veiculo.mapper;

import com.api.veiculo.dto.NovoVeiculo;
import com.api.veiculo.model.Veiculo;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VeiculoMapper {

    @Mapping(target = "renavam", source = "novoVeiculo.renavamNovoVeiculo")
    @Mapping(target = "placa", source = "novoVeiculo.placaNovoVeiculo")
    @Mapping(target = "ufPlaca", source = "novoVeiculo.ufPlacaNovoVeiculo")
    @Mapping(target = "dataAquisicao", source = "novoVeiculo.dataAquisicaoNovoVeiculo")
    @Mapping(target = "anoFabricacao", source = "novoVeiculo.anoFabricacaoNovoVeiculo")
    @Mapping(target = "cor", source = "novoVeiculo.corNovoVeiculo")
    @Mapping(target = "chassi", source = "novoVeiculo.chassiNovoVeiculo")
    @Mapping(target = "anoModelo", source = "novoVeiculo.anoModeloNovoVeiculo")
    @Mapping(target = "cnhCondutor", source = "novoVeiculo.chnCondutorNovoVeiculo")
    Veiculo toVeiculo(NovoVeiculo novoVeiculo);

    NovoVeiculo toDto(Veiculo veiculo);

    com.api.veiculo.dto.VeiculoResponse toVeiculoResponse(Veiculo veiculo);

    void updateVeiculoFromDto(NovoVeiculo novoVeiculo, @MappingTarget Veiculo veiculo);

}
