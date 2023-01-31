package com.trix.crud.service;

import com.trix.crud.dto.NovoVeiculo;
import com.trix.crud.modelo.Veiculo;
import com.trix.crud.repository.VeiculoRepository;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

@Component
public class VeiculoAcoes implements com.trix.crud.service.interfaces.veiculo.VeiculoAcoes{

    private static final String FORMATO_DATA = "yyyy-MM-dd";

    private final VeiculoRepository repository;

    public VeiculoAcoes(VeiculoRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean atribuirCondutorAoVeiculo(String renavam, String cnh) {
        Veiculo veiculo = repository.findById(renavam).get();
        veiculo.setCnhCondutor(cnh);
        repository.save(veiculo);
        return true;
    }

    @Override
    public Veiculo LiberacaoVeiculo(String renavam) {
        Veiculo veiculo = repository.findById(renavam).get();
        veiculo.setCnhCondutor("");
        repository.save(veiculo);
        return veiculo;
    }

    @Override
    public Date dataDaAquisicao() {
        try{
            SimpleDateFormat date = new SimpleDateFormat(FORMATO_DATA);
            date.setLenient(false);
            return date.parse(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        } catch (NullPointerException | ParseException e){
            return null;
        }
    }

    @Override
    public Date converteStringtoData(String data) {
        try{
            SimpleDateFormat date = new SimpleDateFormat(FORMATO_DATA);
            DateTimeFormatter dataFormatoInserido = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter dataFormatoNecessario = DateTimeFormatter.ofPattern(FORMATO_DATA);
            date.setLenient(false);
            return date.parse(LocalDate.parse(data, dataFormatoInserido).format(dataFormatoNecessario));
        } catch (DateTimeParseException | NullPointerException | ParseException e){
            return null;
        }
    }

    @Override
    public Veiculo criaVeiculo(NovoVeiculo novoVeiculo) {
        Veiculo veiculo = new Veiculo();
        veiculo.setRenavam(novoVeiculo.getRenavamNovoVeiculo());
        veiculo.setChassi(novoVeiculo.getChassiNovoVeiculo().toUpperCase());
        veiculo.setUfPlaca(novoVeiculo.getUfPlacaNovoVeiculo().toUpperCase());
        veiculo.setPlaca(novoVeiculo.getPlacaNovoVeiculo());
        veiculo.setAnoFabricacao(novoVeiculo.getAnoFabricacaoNovoVeiculo());
        veiculo.setAnoModelo(novoVeiculo.getAnoModeloNovoVeiculo());
        veiculo.setDataAquisicao(dataDaAquisicao());
        veiculo.setCor(novoVeiculo.getCorNovoVeiculo());
        veiculo.setCnhCondutor("");
        return veiculo;
    }

    @Override
    public Veiculo atualizacaoDados(NovoVeiculo veiculo, Veiculo existente) {
        existente.setPlaca(veiculo.getPlacaNovoVeiculo());
        existente.setChassi(veiculo.getChassiNovoVeiculo());
        existente.setAnoModelo(veiculo.getAnoModeloNovoVeiculo());
        existente.setAnoFabricacao(veiculo.getAnoFabricacaoNovoVeiculo());
        existente.setCor(veiculo.getCorNovoVeiculo());
        existente.setUfPlaca(veiculo.getUfPlacaNovoVeiculo());
        return existente;
    }

    @Override
    public List<Veiculo> addVeiculoListaCondutor(List<Veiculo> lista, String renavam){
        lista.add(repository.findById(renavam).get());
        return lista;
    }
}
