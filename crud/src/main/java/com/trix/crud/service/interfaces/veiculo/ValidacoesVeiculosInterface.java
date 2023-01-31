package com.trix.crud.service.interfaces.veiculo;

public interface ValidacoesVeiculosInterface{

    boolean placa(String placaVeiculo);

    boolean placaContains(String partePlacaVeiculo);

    boolean renavam(String renavam);

    boolean uf(String uf);

    boolean ufExiste(String uf);

    boolean veiculoNaoExiste(String renavam);

    boolean requisitosCadastroVeiculo(String renavam, String placa, String ufPlaca);

    boolean requisitosAquisicaoVeiculo(String renavam);

    boolean data(String data);
}
