package com.api.veiculo.service;

import com.api.veiculo.dto.NovoVeiculo;
import com.api.veiculo.model.Veiculo;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

public interface VeiculoService {
    /**
     * Cadastra um novo veículo no sistema.
     * @param novoVeiculo DTO contendo os dados do veículo a ser cadastrado.
     */
    void cadastrarNovoVeiculo(NovoVeiculo novoVeiculo);

    /**
     * Lista todos os veículos cadastrados.
     * @return Lista de veículos.
     */
    List<Veiculo> findAll();

    /**
     * Busca um veículo específico pelo seu Renavam.
     * @param renavam Renavam do veículo.
     * @return Veiculo encontrado ou null se não existir.
     */
    Veiculo buscaVeiculoComRenavam(String renavam);

    /**
     * Busca veículos agrupados ou filtrados por estado (UF).
     * @param ufDaPlaca Estado de origem da placa.
     * @return Lista de veículos correspondentes.
     */
    List<Veiculo> buscaVeiculoComUfDaPlaca(String ufDaPlaca);

    /**
     * Busca um veículo específico utilizando sua placa completa.
     * @param placa Placa do veículo.
     * @return Veiculo encontrado ou null.
     */
    Veiculo buscaVeiculoComPlaca(String placa);

    /**
     * Busca veículos por parte da placa.
     * @param placa Parte da placa a ser pesquisada.
     * @return Lista de veículos que contêm a parte da placa informada.
     */
    List<Veiculo> buscaPorParteDaPlaca(String placa);

    /**
     * Filtra e retorna veículos adquiridos dentro de um intervalo de datas específico.
     * @param dataInicial Data inicial do intervalo.
     * @param dataFinal Data final do intervalo.
     * @return Lista de veículos cuja data de aquisição está dentro do intervalo.
     */
    List<Veiculo> buscaVeiculosComIntervaloAquisicao(LocalDate dataInicial, LocalDate dataFinal);

    /**
     * Atualiza os dados de um veículo existente.
     * @param veiculo DTO contendo os dados atualizados do veículo.
     */
    void alterarDadosVeiculo(NovoVeiculo veiculo);

    /**
     * Remove um veículo do sistema pelo seu Renavam.
     * @param renavam Renavam do veículo a ser deletado.
     */
    void deletarVeiculo(String renavam);

}
