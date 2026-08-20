package com.api.veiculo.service.impl;

import com.api.veiculo.dto.NovoVeiculo;
import com.api.veiculo.mapper.VeiculoMapper;
import com.api.veiculo.model.Uf;
import com.api.veiculo.model.Veiculo;
import com.api.veiculo.repository.VeiculoRepository;
import com.api.veiculo.service.VeiculoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class VeiculoServiceImpl implements VeiculoService {

    private final VeiculoRepository repository;
    private final VeiculoMapper mapper;

    public VeiculoServiceImpl(VeiculoRepository repository, VeiculoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    @Override
    public void cadastrarNovoVeiculo(NovoVeiculo novoVeiculo) {
        validarRegrasDeNegocio(novoVeiculo);
        
        Veiculo veiculo = mapper.toVeiculo(novoVeiculo);
        repository.save(veiculo);
    }

    private void validarRegrasDeNegocio(NovoVeiculo novoVeiculo) {
        if (repository.existsByRenavam(novoVeiculo.renavamNovoVeiculo())) {
            throw new IllegalArgumentException("Já existe um veículo com este Renavam.");
        }
        if (repository.existsByPlaca(novoVeiculo.placaNovoVeiculo())) {
            throw new IllegalArgumentException("Já existe um veículo com esta Placa.");
        }
        
        try {
            Uf.valueOf(novoVeiculo.ufPlacaNovoVeiculo().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("UF inválida: " + novoVeiculo.ufPlacaNovoVeiculo());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Veiculo> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Veiculo buscaVeiculoComRenavam(String renavam) {
        return repository.findByRenavam(renavam);
    }



    @Override
    @Transactional(readOnly = true)
    public List<Veiculo> buscaVeiculoComUfDaPlaca(String ufDaPlaca) {
        return repository.findByUfPlaca(ufDaPlaca);
    }

    @Override
    @Transactional(readOnly = true)
    public Veiculo buscaVeiculoComPlaca(String placa) {
        return repository.findByPlaca(placa).stream().findFirst().orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Veiculo> buscaPorParteDaPlaca(String placa) {
        return repository.findByPlacaContaining(placa);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Veiculo> buscaVeiculosComIntervaloAquisicao(LocalDate dataInicial, LocalDate dataFinal) {
        return repository.findByIntervalo(dataInicial, dataFinal);
    }

    @Override
    public void alterarDadosVeiculo(NovoVeiculo veiculo) {
        Veiculo veiculoExistente = repository.findByRenavam(veiculo.renavamNovoVeiculo());
        if (veiculoExistente != null) {
            // Regra de negócio: Renavam e Chassi são imutáveis após o cadastro
            veiculoExistente.setPlaca(veiculo.placaNovoVeiculo());
            veiculoExistente.setUfPlaca(veiculo.ufPlacaNovoVeiculo());
            veiculoExistente.setDataAquisicao(veiculo.dataAquisicaoNovoVeiculo());
            veiculoExistente.setAnoFabricacao(veiculo.anoFabricacaoNovoVeiculo());
            veiculoExistente.setCor(veiculo.corNovoVeiculo());
            veiculoExistente.setAnoModelo(veiculo.anoModeloNovoVeiculo());
            veiculoExistente.setCnhCondutor(veiculo.chnCondutorNovoVeiculo());
            
            repository.save(veiculoExistente);
        } else {
            throw new IllegalArgumentException("Veículo não encontrado para alteração.");
        }
    }

    @Override
    public void deletarVeiculo(String renavam) {
        repository.deleteByRenavam(renavam);
    }
}
