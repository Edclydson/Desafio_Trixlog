package com.trix.crud.service.interfaces;

public interface ValidacoesVeiculosInterface{

    boolean validaPlaca(String placaVeiculo);

    boolean placaContains(String partePlacaVeiculo);

    boolean validaRenavam(String renavam);

    boolean validaUf(String uf);

    boolean ufExistente(String uf);

    boolean veiculoNaoExiste(String renavam);

    boolean validacoesParaCadastroVeiculo(String renavam, String placa, String ufPlaca);

    boolean verificacaoVeiculoParaAquisicao(String renavam);

    boolean validaData(String data);
}
