package com.trix.crud.service.interfaces;

public interface ValidacoesCondutorInterface{
    boolean verificaCnhCondutor(String cnh);

    boolean verificaNomeCondutor(String nome);

    boolean verificacaoParaAquisicaoVeiculo(String cnh);

    boolean verificacaoSeCondutorTemVeiculo(String cnh);

    boolean verificaSeCondutorPossuioVeiculo(String renavam, String cnh);
}
