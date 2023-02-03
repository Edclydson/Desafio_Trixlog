package com.trix.crud.veiculo;

import com.trix.crud.ApplicationConfigTest;
import com.trix.crud.dto.NovoVeiculo;
import com.trix.crud.modelo.Condutor;
import com.trix.crud.modelo.Veiculo;
import com.trix.crud.repository.VeiculoRepository;
import com.trix.crud.service.VeiculoAcoes;
import com.trix.crud.service.VeiculoService;
import com.trix.crud.service.VeiculoValidacoes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TestVeiculoService extends ApplicationConfigTest {

    @MockBean
    VeiculoRepository repository;

    @MockBean
    VeiculoValidacoes validacao;

    @MockBean
    VeiculoAcoes acao;

    @Autowired
    VeiculoService service;

    private Veiculo veiculo;

    private NovoVeiculo novoveiculo;

    private Condutor condutor;

    private UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();


    @BeforeEach
    void setup(){
        veiculo = Mockito.mock(Veiculo.class);
        condutor = Mockito.mock(Condutor.class);

        novoveiculo = new NovoVeiculo();
        novoveiculo.setRenavamNovoVeiculo("44745162039");
        novoveiculo.setPlacaNovoVeiculo("HWJ6E63");
        novoveiculo.setUfPlacaNovoVeiculo("CE");
        novoveiculo.setCorNovoVeiculo("Amarelo");
        novoveiculo.setAnoFabricacaoNovoVeiculo("2022");
        novoveiculo.setAnoModeloNovoVeiculo("2023");
        novoveiculo.setChassiNovoVeiculo("76482157359875462");

    }

    @Test
    void DeveRetornarCreated_AoCadastrarNovoVeiculo(){

        Mockito.when(validacao.requisitosCadastroVeiculo(ArgumentMatchers.anyString(),
                                                            ArgumentMatchers.anyString(),
                                                                ArgumentMatchers.anyString())).thenReturn(true);

        Mockito.when(acao.criaVeiculo(ArgumentMatchers.any(NovoVeiculo.class))).thenReturn(veiculo);

        ResponseEntity response = service.cadastrarNovoVeiculo(novoveiculo,uriBuilder.path("/cadastraveiculo").build(novoveiculo));

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        Mockito.verify(repository).save(ArgumentMatchers.any(Veiculo.class));
    }

    @Test
    void DeveRetornarStatusCodeOKComErro_AoCadastrarNovoVeiculo(){

        Mockito.when(validacao.requisitosCadastroVeiculo(ArgumentMatchers.anyString(),
                                                            ArgumentMatchers.anyString(),
                                                                ArgumentMatchers.anyString())).thenReturn(false);

        ResponseEntity response = service.cadastrarNovoVeiculo(novoveiculo,uriBuilder.path("/cadastraveiculo").build(novoveiculo));

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Cadastro não realizado!",response.getBody());
    }

    @Test
    void DeveRetornarListDeVeiculos_AoListarVeiculos(){
        Mockito.when(repository.findAll()).thenReturn(Collections.singletonList(veiculo));

        List<Veiculo> response = service.findAll();

        assertNotNull(response);
        assertEquals(ArrayList.class,response.getClass());
        assertTrue(response.contains(veiculo));
    }

    @Test
    void DeveRetornarStatusCodeOKComSucesso_AoBuscarVeiculoPeloRenavam(){
        Mockito.when(validacao.renavam(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(validacao.veiculoNaoExiste(ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(repository.findById(ArgumentMatchers.anyString())).thenReturn(Optional.of(veiculo));

        ResponseEntity response = service.buscaVeiculoComRenavam("44745162039");

        assertNotNull(response);
        assertEquals(Optional.class, response.getBody().getClass());
        assertEquals(Optional.of(veiculo),response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());
        Mockito.verify(repository).findById(ArgumentMatchers.anyString());

    }

    @Test
    void DeveRetornarStatusCodeOKComErro_AoBuscarVeiculoPeloRenavam(){
        Mockito.when(validacao.renavam(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(validacao.veiculoNaoExiste(ArgumentMatchers.anyString())).thenReturn(true);
        //veiculo não cadastrado

        ResponseEntity response = service.buscaVeiculoComRenavam("44745162039");

        assertNotNull(response);
        assertEquals("Veículo não encontrado!",response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());


        Mockito.when(validacao.renavam(ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(validacao.veiculoNaoExiste(ArgumentMatchers.anyString())).thenReturn(false);
        //renavam invalido

        response = service.buscaVeiculoComRenavam("44745162O39");

        assertEquals("O renavam 44745162O39 não é válido!", response.getBody());
    }

    @Test
    void DeveRetornarOKComSucesso_AoBuscarVeiculoPelaUfDaPlaca(){
        List<Veiculo> listaDeVeiculos = new ArrayList<>();
        listaDeVeiculos.add(veiculo);
        Mockito.when(validacao.uf(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(validacao.ufExiste(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(repository.findByufPlaca(ArgumentMatchers.anyString())).thenReturn(listaDeVeiculos);

        ResponseEntity response = service.buscaVeiculoComUfDaPlaca("RR");

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(ArrayList.class,response.getBody().getClass());
        Mockito.verify(repository).findByufPlaca(ArgumentMatchers.anyString());

    }

    @Test
    void DeveRetornarStatusCodeOKComErro_AoBuscarVeiculoPelaUfDaPlaca(){
        Mockito.when(validacao.uf(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(validacao.ufExiste(ArgumentMatchers.anyString())).thenReturn(false);
        //passando uf inexistente
        ResponseEntity response = service.buscaVeiculoComUfDaPlaca("XA");

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Digite uma localidade válida",response.getBody());


        Mockito.when(validacao.uf(ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(validacao.ufExiste(ArgumentMatchers.anyString())).thenReturn(true);
        //passando uf inválida
        response = service.buscaVeiculoComUfDaPlaca("*~");

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Digite uma localidade válida",response.getBody());

        Mockito.when(validacao.uf(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(validacao.ufExiste(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(repository.findByufPlaca(ArgumentMatchers.anyString())).thenReturn(Collections.emptyList());
        //sem veiculos para a localidade informada

        response = service.buscaVeiculoComUfDaPlaca("AC");

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Não encontramos nenhum veículo dessa localidade",response.getBody());

    }

    @Test
    void DeveRetornarOKComListaDeVeiculos_AoBuscarVeiculoPelaPlaca(){
        List<Veiculo> listaDeVeiculos = new ArrayList<>();
        listaDeVeiculos.add(veiculo);

        Mockito.when(validacao.placa(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(repository.findByPlaca(ArgumentMatchers.anyString())).thenReturn(listaDeVeiculos);
        //passando a placa completa

        ResponseEntity response = service.buscaVeiculoComPlaca("HWJ6E63");

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        Mockito.verify(repository).findByPlaca(ArgumentMatchers.anyString());


        Mockito.when(validacao.placaContains(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(service.buscaPorParteDaPlaca(ArgumentMatchers.anyString())).thenReturn(listaDeVeiculos);
        //passando parte da placa

        response = service.buscaVeiculoComPlaca("HWJ");

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        Mockito.verify(repository).findByPlacaContaining(ArgumentMatchers.anyString());
    }

    @Test
    void DeveRetornarOKComErro_AoBuscarVeiculoPelaPlaca(){
        Mockito.when(validacao.placa(ArgumentMatchers.anyString())).thenReturn(false);

        //passando formato invalido da placa
        ResponseEntity response = service.buscaVeiculoComPlaca("HWJ6563");

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("A placa informada não é válida!",response.getBody());


        Mockito.when(validacao.placa(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(repository.findByPlaca(ArgumentMatchers.anyString())).thenReturn(Collections.emptyList());

        //informando placa completa e retornando nenhum veiculo
        response = service.buscaVeiculoComPlaca("HWJ6563");

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Não encontramos nenhum veículo",response.getBody());


        Mockito.when(validacao.placaContains(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(service.buscaPorParteDaPlaca(ArgumentMatchers.anyString())).thenReturn(Collections.emptyList());
        //passando parte da placa e retornando nenhum veiculo

        response = service.buscaVeiculoComPlaca("HWJ");

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Não encontramos nenhum veículo",response.getBody());


        Mockito.when(validacao.placaContains(ArgumentMatchers.anyString())).thenReturn(false);
        //passando placa invalida

        response = service.buscaVeiculoComPlaca("H*J");

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("A placa informada não é válida!",response.getBody());

    }

    @Test
    void DeveRetornarOkComListaDeVeiculos_AoBuscarVeiculosPeloIntervaloDeAquisicao() throws ParseException {
        List<Veiculo> listaDeVeiculos = new ArrayList<>();
        listaDeVeiculos.add(veiculo);
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeFormatter dataFormatoInserido = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter dataFormatoNecessario = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        date.setLenient(false);
        Date dataInicial = date.parse(LocalDate.parse("01-01-2023", dataFormatoInserido).format(dataFormatoNecessario));

        Mockito.when(repository.findByintevalo(ArgumentMatchers.any(Date.class),ArgumentMatchers.any(Date.class))).thenReturn(listaDeVeiculos);
        Mockito.when(validacao.formatoData(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(validacao.data(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(acao.converteStringtoData(ArgumentMatchers.anyString())).thenReturn(dataInicial);

        ResponseEntity response = service.buscaVeiculosComIntervaloAquisicao("01-01-2023","01-01-2023");

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(ArrayList.class,response.getBody().getClass());
        Mockito.verify(repository).findByintevalo(ArgumentMatchers.any(Date.class),ArgumentMatchers.any(Date.class));
    }

    @Test
    void DeveRetornarOkComErro_AoBuscarVeiculosPeloIntervaloDeAquisicao() throws ParseException {

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeFormatter dataFormatoInserido = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter dataFormatoNecessario = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        date.setLenient(false);
        Date dataInicial = date.parse(LocalDate.parse("01-01-2023", dataFormatoInserido).format(dataFormatoNecessario));

        Mockito.when(repository.findByintevalo(ArgumentMatchers.any(Date.class),ArgumentMatchers.any(Date.class)))
                .thenReturn(Collections.emptyList());
        Mockito.when(validacao.formatoData(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(validacao.data(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(acao.converteStringtoData(ArgumentMatchers.anyString())).thenReturn(dataInicial);

        // informando um intervalo onde não há veiculos adquiridos
        ResponseEntity response = service.buscaVeiculosComIntervaloAquisicao("01-01-2023","01-01-2023");

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Não há registros de veculos adquiridos no intervalo informado!",response.getBody());
        Mockito.verify(repository).findByintevalo(ArgumentMatchers.any(Date.class),ArgumentMatchers.any(Date.class));


        // informando uma data inválida
        Mockito.when(validacao.formatoData(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(validacao.data(ArgumentMatchers.anyString())).thenReturn(false);

        response = service.buscaVeiculosComIntervaloAquisicao("32-01-2023","13-01-2023");

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Por favor informe uma data válida! Ex: 31-12-2022",response.getBody());


        // informando uma data com formato errado
        Mockito.when(validacao.formatoData(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(validacao.data(ArgumentMatchers.anyString())).thenReturn(true);
        response = service.buscaVeiculosComIntervaloAquisicao("O1-01-23","13-01-23");

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Por favor informe uma data válida! Ex: 31-12-2022",response.getBody());


        // informando uma data com separadores diferentes
        Mockito.when(validacao.formatoData(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(validacao.data(ArgumentMatchers.anyString())).thenReturn(true);

        response = service.buscaVeiculosComIntervaloAquisicao("01/01/2023","13/01/2023");

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Por favor informe uma data válida! Ex: 31-12-2022",response.getBody());
    }

    @Test
    void DeveRetornarOKComSucesso_AoAlterarDadosVeiculo(){
        Mockito.when(repository.findById(novoveiculo.getRenavamNovoVeiculo())).thenReturn(Optional.of(veiculo));
        Mockito.when(acao.atualizacaoDados(ArgumentMatchers.any(NovoVeiculo.class),ArgumentMatchers.any(Veiculo.class))).thenReturn(veiculo);

        ResponseEntity response = service.alterarDadosVeiculo(novoveiculo);

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Alterações salvas com sucesso",response.getBody());
    }

    @Test
    void DeveRetornarOKComErro_AoAlterarDadosVeiculo(){
        // veiculo não consta na base de dados
        Mockito.when(repository.findById(novoveiculo.getRenavamNovoVeiculo())).thenReturn(Optional.empty());

        ResponseEntity response = service.alterarDadosVeiculo(novoveiculo);

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Não existe nenhum veiculo com o renavam informado!",response.getBody());

    }

    @Test
    void DeveRetornarNoContent_AoDeletarVeiculo(){
        Mockito.when(validacao.renavam(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(validacao.veiculoNaoExiste(ArgumentMatchers.anyString())).thenReturn(false);

        ResponseEntity response = service.deletarVeiculo("44745162039");

        assertEquals(HttpStatus.NO_CONTENT,response.getStatusCode());
        Mockito.verify(repository).deleteById(ArgumentMatchers.anyString());
    }

    @Test
    void DeveRetornarOKComErro_AoDeletarVeiculo(){
        //renavam invalido
        Mockito.when(validacao.renavam(ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(validacao.veiculoNaoExiste(ArgumentMatchers.anyString())).thenReturn(false);

        ResponseEntity response = service.deletarVeiculo("4474516203");
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Informe um renavam válido!",response.getBody());

        // veiculo não encontrado
        Mockito.when(validacao.renavam(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(validacao.veiculoNaoExiste(ArgumentMatchers.anyString())).thenReturn(true);

        response = service.deletarVeiculo("44745162039");

        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());

    }
}
