package com.trix.crud.service.interfaces;

public interface ValidacoesVeiculosInterface{

    boolean validaPlaca(String placaVeiculo);

    boolean verificaTamanhoDaPlaca(String placaVeiculo);

    boolean validaRenavam(String renavam);

    boolean validaUf(String uf);

    boolean ufExistente(String uf);

    boolean veiculoNaoExiste(String renavam);

    boolean validacoesParaCadastroVeiculo(String renavam, String placa, String ufPlaca);

    boolean verificacaoVeiculoParaAquisicao(String renavam);
}
