package com.trix.crud.service.interfaces;

public interface ValidacoesCondutorInterface{
    boolean cnhValida(String cnh);

    boolean nomeCondutor(String nome);

    boolean requisitosAquisicaoVeiculo(String cnh);

    boolean temAlgumVeiculo(String cnh);

    boolean possuiOVeiculo(String renavam, String cnh);
}
