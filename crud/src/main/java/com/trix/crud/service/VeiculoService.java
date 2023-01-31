package com.trix.crud.service;

import com.trix.crud.dto.NovoVeiculo;
import com.trix.crud.modelo.Veiculo;
import com.trix.crud.repository.VeiculoRepository;
import com.trix.crud.service.interfaces.veiculo.VeiculoInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class VeiculoService implements VeiculoInterface{


    private final VeiculoRepository repository;

    private final VeiculoValidacoes validacao;
    private final VeiculoAcoes acao;

    private static final String MENSAGEM_RETORNO = "A placa informada não é válida!";

    public VeiculoService(VeiculoRepository repository, VeiculoValidacoes validacao, VeiculoAcoes acao) {
        this.repository = repository;
        this.validacao = validacao;
        this.acao = acao;
    }

    @Override
    public ResponseEntity cadastrarNovoVeiculo(NovoVeiculo novoVeiculo, URI uri) {
        if (validacao.requisitosCadastroVeiculo(
                novoVeiculo.getRenavamNovoVeiculo(),
                novoVeiculo.getPlacaNovoVeiculo(),
                novoVeiculo.getUfPlacaNovoVeiculo())
        ){
            repository.save(acao.criaVeiculo(novoVeiculo));
            return ResponseEntity.created(uri).build();
        }

        return ResponseEntity.ok("Cadastro não realizado!");
    }


    @Override
    public List<Veiculo> findAll() {
        Iterable<Veiculo> iterableVeiculo = repository.findAll();
        return StreamSupport.stream(iterableVeiculo.spliterator(),false).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity buscaVeiculoComRenavam(String renavam) {
        if (validacao.renavam(renavam)){
            if (repository.findById(renavam).isPresent()){
                return ResponseEntity.ok(repository.findById(renavam));
            } else {
                return ResponseEntity.ok("Veículo não encontrado!");
            }
        }
        return ResponseEntity.ok("O renavam " + renavam + " não é válido!");
    }

    @Override
    public ResponseEntity buscaVeiculoComUfDaPlaca(String ufDaPlaca) {
        ufDaPlaca = ufDaPlaca.toUpperCase();
        if (validacao.uf(ufDaPlaca) && validacao.ufExiste(ufDaPlaca)){
            List<Veiculo> resposta = repository.findByufPlaca(ufDaPlaca);
            if (!resposta.isEmpty()){
                return ResponseEntity.ok(resposta);
            } else {
                return ResponseEntity.ok("Não encontramos nenhum veículo dessa localidade");
            }
        }
        return ResponseEntity.ok("Digite uma localidade válida");
    }

    @Override
    public ResponseEntity buscaVeiculoComPlaca(String placa) {
        List<Veiculo> resposta;
        if ((placa.length() == 7) && validacao.placa(placa)){
            resposta = repository.findByPlaca(placa);
            return  !resposta.isEmpty() ? ResponseEntity.ok(resposta) :
                        ResponseEntity.ok("Não encontramos nenhum veículo");
        }
        else if((placa.length() >= 2 && placa.length() <= 6) && validacao.placaContains(placa)) {
            resposta = buscaPorParteDaPlaca(placa);
            return !resposta.isEmpty() ?
                        ResponseEntity.ok(resposta) :
                        ResponseEntity.ok("Não encontramos nenhum veículo");
        }
        return ResponseEntity.ok(MENSAGEM_RETORNO);
    }



    @Override
    public List<Veiculo> buscaPorParteDaPlaca(String placa){
        return repository.findByPlacaContaining(placa);
    }

    @Override
    public ResponseEntity buscaVeiculosComIntervaloAquisicao(String dataInicial, String dataFinal) {
        List<Veiculo> veiculosIntervalo;
        if (dataInicial.matches("(\\d{2}-\\d{2}-\\d{4})") && dataFinal.matches("(\\d{2}-\\d{2}-\\d{4})")
                && validacao.data(dataInicial) && validacao.data(dataFinal)){
            veiculosIntervalo = repository.findByintevalo(acao.converteStringtoData(dataInicial), acao.converteStringtoData(dataFinal));
            return !veiculosIntervalo.isEmpty() ? ResponseEntity.ok(veiculosIntervalo) :
                    ResponseEntity.ok("Não há registros de veculos adquiridos no intervalo informado!");
        }
        return ResponseEntity.ok("Por favor informe uma data válida! Ex: 31-12-2022");
    }

    @Override
    public ResponseEntity alterarDadosVeiculo(NovoVeiculo veiculo) {
            Optional<Veiculo> existente = repository.findById(veiculo.getRenavamNovoVeiculo());
        if (existente.isPresent()){
            repository.save(acao.atualizacaoDados(veiculo, existente.get()));
            return ResponseEntity.ok("Alterações salvas com sucesso");
        }
        return ResponseEntity.ok("Não existe nenhum veiculo com o renavam informado!");
    }


    @Override
    public ResponseEntity deletarVeiculo(String renavam) {
        if (validacao.renavam(renavam)){
            if (repository.findById(renavam).isPresent()){
                repository.deleteById(renavam);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Informe um renavam válido!");
    }

}
