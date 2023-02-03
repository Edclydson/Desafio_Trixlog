package com.trix.crud.service;

import com.trix.crud.modelo.Uf;
import com.trix.crud.modelo.Veiculo;
import com.trix.crud.repository.VeiculoRepository;
import com.trix.crud.service.interfaces.veiculo.ValidacoesVeiculosInterface;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

@Component
public class VeiculoValidacoes implements ValidacoesVeiculosInterface{

    private final VeiculoRepository repository;

    public VeiculoValidacoes(VeiculoRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean placa(String placaVeiculo) {
        return placaVeiculo.matches("[A-Z]{3}\\d[A-Z]\\d{2}|[A-Z]{3}\\d{4}");
    }

    @Override
    public boolean placaContains(String partePlacaVeiculo) {
        return partePlacaVeiculo.matches("(?=.*[a-zA-Z\\d]).+") &&
                !partePlacaVeiculo.matches("(?=.*[-} {,.^?~=+_/*|]).+");
    }

    @Override
    public boolean renavam(String renavam) {
        return renavam.matches("(?=.*\\d).{11}") &&
                !renavam.matches("(?=.*[a-zA-Z} {,.^?~=+_/*|]).+");
    }

    @Override
    public boolean uf(String uf) {
        return !uf.matches("(?=.*\\d[} {,.^?~=+_/*|]).+") &&
                uf.matches("(?=.*[a-zA-Z]).{2}");
    }

    @Override
    public boolean ufExiste(String uf) {
        for (Uf existente : Uf.values()){
            if (existente.toString().equals(uf)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean veiculoNaoExiste(String renavam) {
        return repository.findById(renavam).isEmpty();
    }

    @Override
    public boolean requisitosCadastroVeiculo(String renavam, String placa, String ufPlaca) {
        return veiculoNaoExiste(renavam) &&
                renavam(renavam) &&
                placa(placa) &&
                uf(ufPlaca) &&
                ufExiste(ufPlaca);
    }

    @Override
    public boolean requisitosAquisicaoVeiculo(String renavam) {
        if (renavam(renavam) && !veiculoNaoExiste(renavam)){
            Optional<Veiculo> veiculo = repository.findById(renavam);
            return veiculo.get().getCnhCondutor().equals("");
        }
        return false;
    }

    @Override
    public boolean data(String data) {
        try{
            SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
            date.setLenient(false);
            date.parse(data);
            return true;
        } catch (ParseException e){
            return false;
        }
    }

    @Override
    public boolean formatoData(String dataInicio, String dataFinal){
        return dataInicio.matches("(\\d{2}-\\d{2}-\\d{4})") && dataFinal.matches("(\\d{2}-\\d{2}-\\d{4})");
    }
}
