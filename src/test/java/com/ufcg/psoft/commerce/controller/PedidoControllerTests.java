package com.ufcg.psoft.commerce.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufcg.psoft.commerce.Util.RetornaEntidades;
import com.ufcg.psoft.commerce.dto.pedido.PedidoEntregadorResponseDTO;
import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.pedido.PedidoResponseDTO;
import com.ufcg.psoft.commerce.exception.CustomErrorType;
import com.ufcg.psoft.commerce.model.*;
import com.ufcg.psoft.commerce.repository.*;
import com.ufcg.psoft.commerce.service.pedido.PedidoServiceImpl;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@DisplayName("Testes do controlador de pedidos")
public class PedidoControllerTests {
  final String URI_PEDIDOS = "/pedidos";

  @Autowired
  MockMvc driver;

  @Autowired
  PedidoRepository pedidoRepository;
  @Autowired
  ClienteRepository clienteRepository;
  @Autowired
  EstabelecimentoRepository estabelecimentoRepository;
  @Autowired
  SaborRepository saborRepository;
  @Autowired
  PizzaRepository pizzaRepository;
  @Autowired
  EntregadorRepository entregadorRepository;

  ObjectMapper objectMapper = new ObjectMapper();
  Cliente cliente;
  Entregador entregador;
  Sabor sabor1;
  Sabor sabor2;
  Pizza pizzaM;
  Pizza pizzaG;
  Estabelecimento estabelecimento;
  Pedido pedido;
  Pedido pedido1;

  Pedido pedido2;
  PedidoPostPutRequestDTO pedidoPostPutRequestDTO;

  private final PrintStream standardOut = System.out;
  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

  @BeforeEach
  void setup() {
    System.setOut(new PrintStream(outputStreamCaptor));
    objectMapper.registerModule(new JavaTimeModule());
    estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
        .codigoAcesso("654321")
        .build());
    sabor1 = saborRepository.save(Sabor.builder()
        .nome("Sabor Um")
        .tipo("salgado")
        .precoM(10.0)
        .precoG(20.0)
        .disponivel(true)
        .build());
    sabor2 = saborRepository.save(Sabor.builder()
        .nome("Sabor Dois")
        .tipo("doce")
        .precoM(15.0)
        .precoG(30.0)
        .disponivel(true)
        .build());
    cliente = clienteRepository.save(Cliente.builder()
        .nome("Anton Ego")
        .endereco("Paris")
        .codigoAcesso("123456")
        .build());
    entregador = entregadorRepository.save(Entregador.builder()
        .nome("Joãozinho")
        .placaVeiculo("ABC-1234")
        .corVeiculo("Azul")
        .tipoVeiculo("Moto")
        .codigoAcesso("101010")
        .build());
    pizzaM = Pizza.builder()
        .sabor1(sabor1)
        .tamanho("media")
        .build();
    pizzaG = Pizza.builder()
        .sabor1(sabor1)
        .sabor2(sabor2)
        .tamanho("grande")
        .build();
    List<Pizza> pizzas = List.of(pizzaM);
    List<Pizza> pizzas1 = List.of(pizzaM, pizzaG);
    pedido = Pedido.builder()
        .preco(10.0)
        .enderecoEntrega("Casa 237")
        .clienteId(cliente.getId())
        .estabelecimentoId(estabelecimento.getId())
        .entregadorId(entregador.getId())
        .pizzas(pizzas)
        .build();
    pedido1 = Pedido.builder()
        .preco(10.0)
        .enderecoEntrega("Casa 237")
        .clienteId(cliente.getId())
        .estabelecimentoId(estabelecimento.getId())
        .entregadorId(entregador.getId())
        .pizzas(pizzas1)
        .build();

    pedidoPostPutRequestDTO = PedidoPostPutRequestDTO.builder()
        .enderecoEntrega(pedido.getEnderecoEntrega())
        .pizzas(pedido.getPizzas())
        .build();

  }

  @AfterEach
  void tearDown() {
    System.setOut(standardOut);
    clienteRepository.deleteAll();
    estabelecimentoRepository.deleteAll();
    pedidoRepository.deleteAll();
    saborRepository.deleteAll();
  }

  @Nested
  @DisplayName("Conjunto de casos de verificação dos fluxos básicos API Rest")
  class PedidoVerificacaoFluxosBasicosApiRest {

    @Test
    @DisplayName("Quando criamos um novo pedido com dados válidos")
    void quandoCriamosUmNovoPedidoComDadosValidos() throws Exception {
      // Arrange

      // Act
      String responseJsonString = driver.perform(post(URI_PEDIDOS)
          .contentType(MediaType.APPLICATION_JSON)
          .param("clienteId", cliente.getId().toString())
          .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
          .param("estabelecimentoId", estabelecimento.getId().toString())
          .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
          .andExpect(status().isCreated())
          .andDo(print())// Codigo 201
          .andReturn().getResponse().getContentAsString();

      PedidoResponseDTO resultado = objectMapper.readValue(responseJsonString, PedidoResponseDTO.class);

      // Assert
      assertAll(
          () -> assertNotNull(resultado.getId()),
          () -> assertEquals(pedidoPostPutRequestDTO.getEnderecoEntrega(), resultado.getEnderecoEntrega()),
          () -> assertEquals(pedidoPostPutRequestDTO.getPizzas().get(0).getSabor1(),
              resultado.getPizzas().get(0).getSabor1()),
          () -> assertEquals(pedido.getClienteId(), resultado.getClienteId()),
          () -> assertEquals(pedido.getEstabelecimentoId(), resultado.getEstabelecimentoId()),
          () -> assertEquals(pedido.getPreco(), resultado.getPreco()));
    }

    @Test
    @DisplayName("Quando alteramos um novo pedido com dados válidos")
    void quandoAlteramosPedidoValido() throws Exception {
      // Arrange
      pedidoRepository.save(pedido);
      Long pedidoId = pedido.getId();

      // Act
      String responseJsonString = driver.perform(put(URI_PEDIDOS)
          .contentType(MediaType.APPLICATION_JSON)
          .param("pedidoId", pedido.getId().toString())
          .param("codigoAcesso", cliente.getCodigoAcesso())
          .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
          .andExpect(status().isOk())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();

      Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.class);

      // Assert
      assertAll(
          () -> assertEquals(pedidoId, resultado.getId().longValue()),
          () -> assertEquals(pedidoPostPutRequestDTO.getEnderecoEntrega(), resultado.getEnderecoEntrega()),
          () -> assertEquals(pedidoPostPutRequestDTO.getPizzas().get(0).getSabor1(),
              resultado.getPizzas().get(0).getSabor1()),
          () -> assertEquals(pedido.getClienteId(), resultado.getClienteId()),
          () -> assertEquals(pedido.getEstabelecimentoId(), resultado.getEstabelecimentoId()),
          () -> assertEquals(pedido.getPreco(), resultado.getPreco()));
    }

    @Test
    @DisplayName("Quando alteramos um pedido inexistente")
    void quandoAlteramosPedidoInexistente() throws Exception {
      // Arrange
      // nenhuma necessidade além do setup()

      // Act
      String responseJsonString = driver.perform(put(URI_PEDIDOS)
          .contentType(MediaType.APPLICATION_JSON)
          .param("pedidoId", "999999")
          .param("codigoAcesso", cliente.getCodigoAcesso())
          .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
          .andExpect(status().isBadRequest())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();

      CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

      // Assert
      assertEquals("O pedido consultado nao existe!", resultado.getMessage());
    }

    @Test
    @DisplayName("Quando alteramos um pedido passando codigo de acesso invalido")
    void quandoAlteramosPedidoPassandoCodigoAcessoInvalido() throws Exception {
      // Arrange
      pedidoRepository.save(pedido);

      // Act
      String responseJsonString = driver.perform(put(URI_PEDIDOS)
          .contentType(MediaType.APPLICATION_JSON)
          .param("pedidoId", pedido.getId().toString())
          .param("codigoAcesso", "999999")
          .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
          .andExpect(status().isBadRequest())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();

      CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

      // Assert
      assertEquals("Codigo de acesso invalido!", resultado.getMessage());
    }

    @Test
    @DisplayName("Quando um cliente busca por todos seus pedidos salvos")
    void quandoClienteBuscaTodosPedidos() throws Exception {
      // Arrange
      pedidoRepository.save(pedido);
      pedidoRepository.save(pedido1);

      // Act
      String responseJsonString = driver.perform(get(URI_PEDIDOS)
          .param("clienteId", cliente.getId().toString())
          .param("codigoAcesso", cliente.getCodigoAcesso()))
          .andExpect(status().isOk())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();

      List<Pedido> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
      });

      // Assert
      assertEquals(2, resultado.size());
    }

    @Test
    @DisplayName("Quando um cliente busca por um pedido seu salvo pelo id primeiro")
    void quandoClienteBuscaPedidoPorId() throws Exception {
      // Arrange
      pedidoRepository.save(pedido);

      // Act
      String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + pedido.getId() + "/" + cliente.getId())
          .param("codigoAcesso", cliente.getCodigoAcesso()))
          .andExpect(status().isOk())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();

      Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.class);

      // Assert
      assertAll(
          () -> assertNotNull(resultado.getId()),
          () -> assertEquals(pedidoPostPutRequestDTO.getEnderecoEntrega(), resultado.getEnderecoEntrega()),
          () -> assertEquals(pedidoPostPutRequestDTO.getPizzas().get(0).getSabor1(),
              resultado.getPizzas().get(0).getSabor1()),
          () -> assertEquals(pedido.getClienteId(), resultado.getClienteId()),
          () -> assertEquals(pedido.getEstabelecimentoId(), resultado.getEstabelecimentoId()),
          () -> assertEquals(pedido.getPreco(), resultado.getPreco()));
    }

    @Test
    @DisplayName("Quando um cliente busca por um pedido seu salvo por id inexistente")
    void quandoClienteBuscaPedidoInexistente() throws Exception {
      // Arrange
      // nenhuma necessidade além do setup()

      // Act
      String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + "999999" + "/" + cliente.getId())
          .param("codigoAcesso", cliente.getCodigoAcesso())
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
          .andExpect(status().isBadRequest())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();

      CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

      // Assert
      assertEquals("O pedido consultado nao existe!", resultado.getMessage());
    }

    @Test
    @DisplayName("Quando um cliente busca por um pedido feito por outro cliente")
    void quandoClienteBuscaPedidoDeOutroCliente() throws Exception {
      // Arrange
      pedidoRepository.save(pedido);
      Cliente cliente1 = clienteRepository.save(Cliente.builder()
          .nome("Catarina")
          .endereco("Casinha")
          .codigoAcesso("121212")
          .build());

      // Act
      String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + pedido.getId() + "/" + cliente1.getId())
          .param("codigoAcesso", cliente1.getCodigoAcesso())
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
          .andExpect(status().isBadRequest())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();

      CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

      // Assert
      assertEquals("O pedido nao pertence ao cliente!", resultado.getMessage());
    }

    @Test
    @DisplayName("Quando um estabelecimento busca todos os pedidos feitos nele")
    void quandoEstabelecimentoBuscaTodosPedidos() throws Exception {
      // Arrange
      pedidoRepository.save(pedido);
      pedidoRepository.save(pedido1);

      // Act
      String responseJsonString = driver.perform(get(URI_PEDIDOS + "/" + estabelecimento.getId())
          .param("codigoAcesso", estabelecimento.getCodigoAcesso()))
          .andExpect(status().isOk())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();

      List<Pedido> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
      });

      // Assert
      assertEquals(2, resultado.size());
    }

    @Test
    @DisplayName("Quando um estabelecimento busca por um pedido feito nele salvo pelo id primeiro")
    void quandoEstabelecimentoBuscaPedidoPorId() throws Exception {
      // Arrange
      pedidoRepository.save(pedido);

      // Act
      String responseJsonString = driver
          .perform(get(URI_PEDIDOS + "/" + pedido.getId() + "/" + estabelecimento.getId() + "/"
              + estabelecimento.getCodigoAcesso()))
          .andExpect(status().isOk())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();

      Pedido resultado = objectMapper.readValue(responseJsonString, Pedido.class);

      // Assert
      assertAll(
          () -> assertNotNull(resultado.getId()),
          () -> assertEquals(pedidoPostPutRequestDTO.getEnderecoEntrega(), resultado.getEnderecoEntrega()),
          () -> assertEquals(pedidoPostPutRequestDTO.getPizzas().get(0).getSabor1(),
              resultado.getPizzas().get(0).getSabor1()),
          () -> assertEquals(pedido.getClienteId(), resultado.getClienteId()),
          () -> assertEquals(pedido.getEstabelecimentoId(), resultado.getEstabelecimentoId()),
          () -> assertEquals(pedido.getPreco(), resultado.getPreco()));
    }

    @Test
    @DisplayName("Quando um estabelecimento busca por um pedido feito nele salvo pelo id inexistente")
    void quandoEstabelecimentoBuscaPedidoInexistente() throws Exception {
      // Arrange
      // nenhuma necessidade além do setup()

      // Act
      String responseJsonString = driver.perform(
          get(URI_PEDIDOS + "/" + estabelecimento.getCodigoAcesso() + "/" + estabelecimento.getId() + "/" + "999999")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
          .andExpect(status().isBadRequest())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();

      CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

      // Assert
      assertEquals("O pedido consultado nao existe!", resultado.getMessage());
    }

    @Test
    @DisplayName("Quando um estabelecimento busca por um pedido feito em outro estabelecimento")
    void quandoEstabelecimentoBuscaPedidoDeOutroEstabelecimento() throws Exception {
      // Arrange
      pedidoRepository.save(pedido);
      Estabelecimento estabelecimento1 = estabelecimentoRepository.save(Estabelecimento.builder()
          .codigoAcesso("121212")
          .build());

      // Act
      String responseJsonString = driver
          .perform(get(URI_PEDIDOS + "/" + pedido.getId() + "/" + estabelecimento1.getId() + "/"
              + estabelecimento1.getCodigoAcesso())
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
          .andExpect(status().isBadRequest())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();

      CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

      // Assert
      assertEquals("O pedido nao pertence ao estabelecimento!", resultado.getMessage());
    }

    @Test
    @DisplayName("Quando um cliente excluí um pedido feito por ele salvo")
    void quandoClienteExcluiPedidoSalvo() throws Exception {
      // Arrange
      pedidoRepository.save(pedido);

      // Act
      String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" + pedido.getId() + "/" + cliente.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .param("codigoAcesso", cliente.getCodigoAcesso()))
          .andExpect(status().isNoContent())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();

      // Assert
      assertTrue(responseJsonString.isBlank());
    }

    @Test
    @DisplayName("Quando um cliente excluí um pedido inexistente")
    void quandoClienteExcluiPedidoInexistente() throws Exception {
      // Arrange
      // nenhuma necessidade além do setup()

      // Act
      String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" + "999999" + "/" + cliente.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .param("codigoAcesso", cliente.getCodigoAcesso()))
          .andExpect(status().isBadRequest())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();

      CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

      // Assert
      assertEquals("O pedido consultado nao existe!", resultado.getMessage());
    }

    @Test
    @DisplayName("Quando um cliente excluí todos seus pedidos feitos por ele salvos")
    void quandoClienteExcluiTodosPedidosSalvos() throws Exception {
      // Arrange
      pedidoRepository.save(pedido);
      pedidoRepository.save(Pedido.builder()
          .preco(10.0)
          .enderecoEntrega("Casa 237")
          .clienteId(cliente.getId())
          .estabelecimentoId(estabelecimento.getId())
          .pizzas(List.of(pizzaM, pizzaG))
          .build());

      // Act
      String responseJsonString = driver.perform(delete(URI_PEDIDOS)
          .param("clienteId", cliente.getId().toString())
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNoContent())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();

      // Assert
      assertTrue(responseJsonString.isBlank());
    }

    @Test
    @DisplayName("Quando um estabelencimento excluí um pedido feito nele salvo")
    void quandoEstabelecimentoExcluiPedidoSalvo() throws Exception {
      // Arrange
      pedidoRepository.save(pedido);

      // Act
      String responseJsonString = driver
          .perform(delete(URI_PEDIDOS + "/" + pedido.getId() + "/" + estabelecimento.getId() + "/"
              + estabelecimento.getCodigoAcesso()))
          .andExpect(status().isNoContent())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();

      // Assert
      assertTrue(responseJsonString.isBlank());
    }

    @Test
    @DisplayName("Quando um estabelencimento excluí um pedido inexistente")
    void quandoEstabelecimentoExcluiPedidoInexistente() throws Exception {
      // Arrange
      // nenhuma necessidade além do setup()

      // Act
      String responseJsonString = driver
          .perform(delete(
              URI_PEDIDOS + "/" + "999999" + "/" + estabelecimento.getId() + "/" + estabelecimento.getCodigoAcesso()))
          .andExpect(status().isBadRequest())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();

      CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

      // Assert
      assertEquals("O pedido consultado nao existe!", resultado.getMessage());
    }

    @Test
    @DisplayName("Quando um estabelencimento excluí um pedido feito em outro estabelecimento")
    void quandoEstabelecimentoExcluiPedidoDeOutroEstabelecimento() throws Exception {
      // Arrange
      pedidoRepository.save(pedido);
      Estabelecimento estabelecimento1 = estabelecimentoRepository.save(Estabelecimento.builder()
          .codigoAcesso("121212")
          .build());

      // Act
      String responseJsonString = driver
          .perform(delete(URI_PEDIDOS + "/" + pedido.getId() + "/" + estabelecimento1.getId() + "/"
              + estabelecimento1.getCodigoAcesso()))
          .andExpect(status().isBadRequest())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();

      CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

      // Assert
      assertEquals("O pedido nao pertence ao estabelecimento!", resultado.getMessage());
    }

    @Test
    @DisplayName("Quando um estabelencimento excluí todos os pedidos feitos nele salvos")
    void quandoEstabelecimentoExcluiTodosPedidosSalvos() throws Exception {
      // Arrange
      pedidoRepository.save(pedido);
      pedidoRepository.save(Pedido.builder()
          .preco(10.0)
          .enderecoEntrega("Casa 237")
          .clienteId(cliente.getId())
          .estabelecimentoId(estabelecimento.getId())
          .pizzas(List.of(pizzaM, pizzaG))
          .build());

      // Act
      String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" + estabelecimento.getId())
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNoContent())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();
      // Assert
      assertTrue(responseJsonString.isBlank());
    }

    @Test
    @DisplayName("Quando um cliente busca um pedido feito em um estabelecimento")
    void quandoClienteBuscaPedidoFeitoEmEstabelecimento() throws Exception {
      // Arrange
      pedidoRepository.save(pedido);

      // Act
      String responseJsonString = driver
          .perform(get(URI_PEDIDOS + "/" + "pedido-cliente-estabelecimento" + "/" + cliente.getId() + "/"
              + estabelecimento.getId() + "/" + pedido.getId())
              .contentType(MediaType.APPLICATION_JSON)
              .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
          .andExpect(status().isOk())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();

      List<PedidoResponseDTO> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
      });

      // Assert
      assertEquals(1, resultado.size());
      assertEquals(pedido.getId(), resultado.get(0).getId());
      assertEquals(pedido.getClienteId(), resultado.get(0).getClienteId());
      assertEquals(pedido.getEstabelecimentoId(), resultado.get(0).getEstabelecimentoId());
    }

    @Test
    @DisplayName("Quando um cliente busca um pedido feito em um estabelecimento inexistente")
    void quandoClienteBuscaPedidoFeitoEmEstabelecimentoInexistente() throws Exception {
      // Arrange
      pedidoRepository.save(pedido);

      // Act
      String responseJsonString = driver
          .perform(get(URI_PEDIDOS + "/" + "pedido-cliente-estabelecimento" + "/" + cliente.getId() + "/" + "999999"
              + "/" + pedido.getId())
              .contentType(MediaType.APPLICATION_JSON)
              .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
          .andExpect(status().isBadRequest())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();

      CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

      // Assert
      assertEquals("O estabelecimento consultado nao existe!", resultado.getMessage());
    }

    @Test
    @DisplayName("Quando um cliente busca um pedido feito em um estabelecimento com pedido inexistente")
    void quandoClienteBuscaPedidoFeitoEmEstabelecimentoComPedidoInexistente() throws Exception {
      // Arrange
      pedidoRepository.save(pedido);

      // Act
      String responseJsonString = driver
          .perform(get(URI_PEDIDOS + "/" + "pedido-cliente-estabelecimento" + "/" + cliente.getId() + "/"
              + estabelecimento.getId() + "/" + "999999")
              .contentType(MediaType.APPLICATION_JSON)
              .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
          .andExpect(status().isBadRequest())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();

      CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

      // Assert
      assertEquals("O pedido consultado nao existe!", resultado.getMessage());
    }

    @Test
    @DisplayName("Quando um cliente busca um pedido feito em um estabelecimento com cliente inexistente")
    void quandoClienteBuscaPedidoFeitoEmEstabelecimentoComClienteInexistente() throws Exception {
      // Arrange
      pedidoRepository.save(pedido);

      // Act
      String responseJsonString = driver
          .perform(get(URI_PEDIDOS + "/" + "pedido-cliente-estabelecimento" + "/" + "999999" + "/"
              + estabelecimento.getId() + "/" + pedido.getId())
              .contentType(MediaType.APPLICATION_JSON)
              .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
          .andExpect(status().isBadRequest())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();

      CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

      // Assert
      assertEquals("O cliente consultado nao existe!", resultado.getMessage());
    }

    @Test
    @DisplayName("Quando um cliente busca todos os pedidos feitos naquele estabelcimento com pedidoId null")
    void quandoClienteBuscaTodosPedidosFeitosNaqueleEstabelecimentoComPedidoIdNull() throws Exception {
      // Arrange
      pedidoRepository.save(pedido);

      // Act
      String responseJsonString = driver
          .perform(
              get(URI_PEDIDOS + "/pedidos-cliente-estabelecimento/" + cliente.getId() + "/" + estabelecimento.getId())
                  .contentType(MediaType.APPLICATION_JSON)
                  .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
          .andExpect(status().isOk())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();

      List<PedidoResponseDTO> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
      });

      // Assert
      assertEquals(1, resultado.size());
      assertEquals(pedido.getId(), resultado.get(0).getId());
      assertEquals(pedido.getClienteId(), resultado.get(0).getClienteId());
      assertEquals(pedido.getEstabelecimentoId(), resultado.get(0).getEstabelecimentoId());
    }

    @Test
    @DisplayName("Quando um cliente cancela um pedido")
    void quandoClienteCancelaPedido() throws Exception {
      // Arrange
      pedidoRepository.save(pedido);

      // Act
      String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" +
          pedido.getId() + "/cancelar-pedido")
          .contentType(MediaType.APPLICATION_JSON)
          .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
          .andExpect(status().isNoContent())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();

      // Assert
      assertTrue(responseJsonString.isBlank());
    }

    @Test
    @DisplayName("Quando um cliente cancela um pedido incancelavel")
    void quandoUmClienteCancelaUmPedidoIncancelavel() throws Exception {
        // Arrange
        Pedido pedidoTest = pedidoRepository.save(Pedido.builder()
          .preco(30.0)
          .enderecoEntrega("Casa 237")
          .clienteId(cliente.getId())
          .estabelecimentoId(estabelecimento.getId())
          .pizzas(List.of(pizzaM))
          .statusEntrega("Pedido entregue")
          .build());

        // Act
        String responseJsonString = driver.perform(delete(URI_PEDIDOS + "/" +
          pedidoTest.getId() + "/cancelar-pedido")
          .contentType(MediaType.APPLICATION_JSON)
          .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
          .andExpect(status().isBadRequest())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();
          
          CustomErrorType resultado = objectMapper.readValue(responseJsonString, CustomErrorType.class);

        // Assert
        assertEquals("Pedido nao pode ser cancelado!", resultado.getMessage());
    }

    @Test
    @DisplayName("Quando um cliente busca todos os pedidos feitos naquele estabelcimento com status")
    void quandoClienteBuscaTodosPedidosFeitosNaqueleEstabelecimentoComStatus()
        throws Exception {
      // Arrange
      Pedido pedido3 = pedidoRepository.save(Pedido.builder()
          .preco(30.0)
          .enderecoEntrega("Casa 237")
          .clienteId(cliente.getId())
          .estabelecimentoId(estabelecimento.getId())
          .pizzas(List.of(pizzaM))
          .statusEntrega("Pedido entregue")
          .build());
      Pedido pedido4 = pedidoRepository.save(Pedido.builder()
          .preco(30.0)
          .enderecoEntrega("Casa 237")
          .clienteId(cliente.getId())
          .estabelecimentoId(estabelecimento.getId())
          .pizzas(List.of(pizzaM))
          .statusEntrega("Pedido em preparo")
          .build());

      // Act
      String responseJsonString = driver.perform(get(URI_PEDIDOS +
          "/pedidos-cliente-estabelecimento/" + cliente.getId() + "/" +
          estabelecimento.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
          .andExpect(status().isOk())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();

      List<PedidoResponseDTO> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
      });

      // Assert
      assertEquals(2, resultado.size());
      assertEquals(pedido3.getId(), resultado.get(1).getId());
      assertEquals(pedido3.getClienteId(), resultado.get(0).getClienteId());
      assertEquals(pedido3.getEstabelecimentoId(),
          resultado.get(0).getEstabelecimentoId());
      assertEquals(pedido4.getId(), resultado.get(0).getId());
      assertEquals(pedido4.getClienteId(), resultado.get(1).getClienteId());
      assertEquals(pedido4.getEstabelecimentoId(),
          resultado.get(1).getEstabelecimentoId());

    }

    @Test
    @DisplayName("Quando um cliente busca todos os pedidos feitos naquele estabelcimento filtrados por entrega")
    void quandoClienteBuscaTodosPedidosFeitosNaqueleEstabelecimentoComPedidosFiltradosPorEntrega()
        throws Exception {
      // Arrange
      Pedido pedido3 = pedidoRepository.save(Pedido.builder()
          .preco(30.0)
          .enderecoEntrega("Casa 237")
          .clienteId(cliente.getId())
          .estabelecimentoId(estabelecimento.getId())
          .pizzas(List.of(pizzaM))
          .statusEntrega("Pedido em preparo")
          .build());
        pedidoRepository.save(Pedido.builder()
          .preco(50.0)
          .enderecoEntrega("Casa 237")
          .clienteId(cliente.getId())
          .estabelecimentoId(estabelecimento.getId())
          .pizzas(List.of(pizzaM))
          .statusEntrega("Pedido em preparo")
          .build());

      // Act
      String responseJsonString = driver.perform(get(URI_PEDIDOS +
          "/pedidos-cliente-estabelecimento/" + cliente.getId() + "/" +
          estabelecimento.getId() + "/" + pedido3.getStatusEntrega())
          .contentType(MediaType.APPLICATION_JSON)
          .param("clienteCodigoAcesso", cliente.getCodigoAcesso()))
          .andExpect(status().isOk())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();

      List<PedidoResponseDTO> resultado = objectMapper.readValue(responseJsonString, new TypeReference<>() {
      });

      // Assert
      assertEquals(2, resultado.size());
      assertEquals(pedido3.getId(), resultado.get(0).getId());
      assertEquals(pedido3.getClienteId(), resultado.get(0).getClienteId());
      assertEquals(pedido3.getEstabelecimentoId(),
          resultado.get(0).getEstabelecimentoId());
      assertEquals(pedido3.getId(), resultado.get(0).getId());
      assertEquals(pedido3.getClienteId(), resultado.get(0).getClienteId());
      assertEquals(pedido3.getEstabelecimentoId(),
          resultado.get(0).getEstabelecimentoId());
    }
  }

  @Nested
  @DisplayName("Alteração de estado de pedido")
  public class AlteracaoEstadoPedidoTest {
    Pedido pedido1;

    @BeforeEach
    void setUp() {
      pedido1 = pedidoRepository.save(Pedido.builder()
          .estabelecimentoId(estabelecimento.getId())
          .clienteId(cliente.getId())
          .enderecoEntrega("Rua 1")
          .pizzas(List.of(pizzaG))
          .preco(10.0)
          .build());
    }

    @Test
    @DisplayName("Quando o estabelecimento associa um pedido a um entregador")
    void quandoEstabelecimentoAssociaPedidoEntregador() throws Exception {
      // Arrange
      pedidoRepository.save(pedido);
      pedido.setStatusEntrega("Pedido pronto");
      LinkedList<Entregador> entregadores = new LinkedList<>();
      entregadores.add(entregador);
      estabelecimento.setEntregadoresDisponiveis(entregadores);

      // Act
      String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" + pedido.getId() + "/associar-pedido-entregador")
          .contentType(MediaType.APPLICATION_JSON)
          .param("estabelecimentoId", estabelecimento.getId().toString())
          .param("estabelecimentoCodigoAcesso", estabelecimento.getCodigoAcesso())
          .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
          .andExpect(status().isOk())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();
      PedidoEntregadorResponseDTO resultado = objectMapper.readValue(responseJsonString,
          PedidoEntregadorResponseDTO.class);

      // Assert
      assertEquals(resultado.getStatusEntrega(), "Pedido em rota");
      assertEquals(entregador.getId(), resultado.getEntregadorId());
    }

    @Test
    @DisplayName("Quando o cliente confirma a entrega de um pedido")
    void quandoClienteConfirmaEntregaPedido() throws Exception {
      // Arrange
      pedidoRepository.save(pedido);
      pedido.setStatusEntrega("Pedido em rota");

      // Act
      String responseJsonString = driver.perform(put(URI_PEDIDOS + "/" +
          pedido.getId() + "/" + cliente.getId() + "/cliente-confirmar-entrega")
          .contentType(MediaType.APPLICATION_JSON)
          .param("clienteCodigoAcesso", cliente.getCodigoAcesso())
          .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
          .andExpect(status().isOk())
          .andDo(print())
          .andReturn().getResponse().getContentAsString();

      PedidoResponseDTO resultado = objectMapper.readValue(responseJsonString,
          PedidoResponseDTO.class);

      // Assert
      assertEquals(resultado.getStatusEntrega(), "Pedido entregue");
      assertTrue(outputStreamCaptor.toString()
          .trim().contains("Olá estabelecimento " + resultado.getEstabelecimentoId() + ", o pedido " + resultado.getId()
              + " mudou de status para Pedido entregue!"));
    }



}

  @Nested
  @DisplayName("Conjunto de casos de teste da confirmação de pagamento de um pedido")
  public class PedidoConfirmarPagamentoTests {

    Pedido pedido1;

    @BeforeEach
    void setUp() {
      pedido1 = pedidoRepository.save(Pedido.builder()
              .estabelecimentoId(estabelecimento.getId())
              .clienteId(cliente.getId())
              .enderecoEntrega("Rua 1")
              .pizzas(List.of(pizzaG))
              .preco(10.0)
              .build());
    }

    @Test
    @DisplayName("Quando confirmamos o pagamento de um pedido por cartão de crédito")
    void confirmaPagamentoCartaoCredito() throws Exception {
      // Arrange
      // Act
      String responseJsonString = driver.perform(patch(URI_PEDIDOS + "/" + cliente.getId() + "/confirmar-pagamento")
                      .contentType(MediaType.APPLICATION_JSON)
                      .param("codigoAcessoCliente", cliente.getCodigoAcesso())
                      .param("pedidoId", pedido1.getId().toString())
                      .param("metodoPagamento", "CREDITO"))
              .andExpect(status().isOk()) // Codigo 200
              .andReturn().getResponse().getContentAsString();
      // Assert
      PedidoResponseDTO resultado = objectMapper.readValue(responseJsonString, PedidoResponseDTO.class);
      assertAll(
              () -> assertTrue(resultado.getStatusPagamento()),
              () -> assertEquals(10, resultado.getPreco()));
    }

    @Test
    @DisplayName("Quando confirmamos o pagamento de um pedido por cartão de débito")
    void confirmaPagamentoCartaoDebito() throws Exception {
      // Arrange
      // Act
      String responseJsonString = driver.perform(patch(URI_PEDIDOS + "/" + cliente.getId() + "/confirmar-pagamento")
                      .contentType(MediaType.APPLICATION_JSON)
                      .param("codigoAcessoCliente", cliente.getCodigoAcesso())
                      .param("pedidoId", pedido1.getId().toString())
                      .param("metodoPagamento", "DEBITO"))
              .andExpect(status().isOk()) // Codigo 200
              .andReturn().getResponse().getContentAsString();
      // Assert
      PedidoResponseDTO resultado = objectMapper.readValue(responseJsonString, PedidoResponseDTO.class);
      assertAll(
              () -> assertTrue(resultado.getStatusPagamento()),
              () -> assertEquals(9.75, resultado.getPreco()));
    }

    @Test
    @DisplayName("Quando confirmamos o pagamento de um pedido por cartão de crédito")
    void confirmaPagamentoPIX() throws Exception {
      // Arrange
      // Act
      String responseJsonString = driver.perform(patch(URI_PEDIDOS + "/" + cliente.getId() + "/confirmar-pagamento")
                      .contentType(MediaType.APPLICATION_JSON)
                      .param("codigoAcessoCliente", cliente.getCodigoAcesso())
                      .param("pedidoId", pedido1.getId().toString())
                      .param("metodoPagamento", "PIX"))
              .andExpect(status().isOk()) // Codigo 200
              .andReturn().getResponse().getContentAsString();
      // Assert
      PedidoResponseDTO resultado = objectMapper.readValue(responseJsonString, PedidoResponseDTO.class);
      assertAll(
              () -> assertTrue(resultado.getStatusPagamento()),
              () -> assertEquals(9.5, resultado.getPreco()));
    }

  }

  @Nested
  @Transactional
  @DisplayName("Conjunto de casos de teste da mudança de status de um pedido")
  class AlteraStatusPedido {

    Pedido pedido1;

    @Autowired
    AssociacaoRepository associacaoRepository;
    @Autowired
    PedidoServiceImpl pedidoService;

    @BeforeEach
    void setUp() {
      associacaoRepository.save(Associacao.builder()
          .entregador(entregador)
          .estabelecimento(estabelecimento)
          .status(true)
          .build());
      pedido1 = pedidoRepository.save(Pedido.builder()
          .estabelecimentoId(estabelecimento.getId())
          .clienteId(cliente.getId())
          .enderecoEntrega("Rua 1")
          .pizzas(List.of(pizzaG))
          .preco(10.0)
          .build());
    }

    @AfterEach
    void breakDown() {
      associacaoRepository.deleteAll();
    }

    @Test
    @DisplayName("Quando pedido status muda de Pedido recebido para Pedido em preparo")
    void mudaStatusParaPedidoEmPreparo() throws Exception {

      String responseJsonString = driver.perform(patch(URI_PEDIDOS + "/" + cliente.getId() + "/confirmar-pagamento")
          .contentType(MediaType.APPLICATION_JSON)
          .param("codigoAcessoCliente", cliente.getCodigoAcesso())
          .param("pedidoId", pedido1.getId().toString())
          .param("metodoPagamento", "PIX"))
          .andExpect(status().isOk()) // Codigo 200
          .andReturn().getResponse().getContentAsString();
      // Assert
      PedidoResponseDTO resultado = objectMapper.readValue(responseJsonString, PedidoResponseDTO.class);
      assertEquals("Pedido em preparo", resultado.getStatusEntrega());
    }

    @Test
    @DisplayName("Quando pedido status muda de Pedido em preparo para Pedido pronto e existe entregador disponivel")
    void mudaStatusParaPedidoProntoEntregadorDisponivel() throws Exception {

      Estabelecimento estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
          .codigoAcesso("654321")
          .build());

      Sabor sabor1 = saborRepository.save(Sabor.builder()
          .nome("Sabor Um")
          .tipo("salgado")
          .precoM(10.0)
          .precoG(20.0)
          .disponivel(true)
          .build());

      Cliente cliente = clienteRepository.save(Cliente.builder()
          .nome("Anton Ego")
          .endereco("Paris")
          .codigoAcesso("123456")
          .build());
      Entregador entregador = entregadorRepository.save(Entregador.builder()
          .nome("Joãozinho")
          .placaVeiculo("ABC-1234")
          .corVeiculo("Azul")
          .tipoVeiculo("Moto")
          .codigoAcesso("101010")
          .build());
      estabelecimento.getEntregadoresDisponiveis().add(entregador);
      Pizza pizzaM = Pizza.builder()
          .sabor1(sabor1)
          .tamanho("media")
          .build();

      List<Pizza> pizzas = List.of(pizzaM);
      Pedido pedido = Pedido.builder()
          .preco(10.0)
          .enderecoEntrega("Casa 237")
          .clienteId(cliente.getId())
          .estabelecimentoId(estabelecimento.getId())
          .entregadorId(0L)
          .pizzas(pizzas)
          .build();
      pedidoRepository.save(pedido);

      String responseJsonString = driver.perform(patch(URI_PEDIDOS + "/" + pedido.getId() + "/status-pedido/pronto")
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk()) // Codigo 200
          .andReturn().getResponse().getContentAsString();
      // Assert
      PedidoResponseDTO resultado = objectMapper.readValue(responseJsonString, PedidoResponseDTO.class);
      assertAll(
          () -> assertEquals("Pedido em rota", resultado.getStatusEntrega()),
          () -> assertTrue(outputStreamCaptor.toString()
              .trim().contains("Olá entregador " + entregador.getId() + ", o pedido " + pedido.getId()
                  + " está disponível para entrega!")));
    }

    @Test
    @DisplayName("Quando pedido status muda de Pedido em preparo para Pedido pronto e entregador indisponivel")
    void mudaStatusParaPedidoProntoEntregadorIndisponivel() throws Exception {

      Estabelecimento estabelecimento = estabelecimentoRepository.save(Estabelecimento.builder()
          .codigoAcesso("654321")
          .build());

      Sabor sabor1 = saborRepository.save(Sabor.builder()
          .nome("Sabor Um")
          .tipo("salgado")
          .precoM(10.0)
          .precoG(20.0)
          .disponivel(true)
          .build());

      Cliente cliente = clienteRepository.save(Cliente.builder()
          .nome("Anton Ego")
          .endereco("Paris")
          .codigoAcesso("123456")
          .build());
      entregadorRepository.save(Entregador.builder()
          .nome("Joãozinho")
          .placaVeiculo("ABC-1234")
          .corVeiculo("Azul")
          .tipoVeiculo("Moto")
          .codigoAcesso("101010")
          .build());
      Pizza pizzaM = Pizza.builder()
          .sabor1(sabor1)
          .tamanho("media")
          .build();
      List<Pizza> pizzas = List.of(pizzaM);
      Pedido pedido = Pedido.builder()
          .preco(10.0)
          .enderecoEntrega("Casa 237")
          .clienteId(cliente.getId())
          .estabelecimentoId(estabelecimento.getId())
          .entregadorId(0L)
          .pizzas(pizzas)
          .build();
      pedidoRepository.save(pedido);

      String responseJsonString = driver.perform(patch(URI_PEDIDOS + "/" + pedido.getId() + "/status-pedido/pronto")
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk()) // Codigo 200
          .andReturn().getResponse().getContentAsString();
      // Assert
      PedidoResponseDTO resultado = objectMapper.readValue(responseJsonString, PedidoResponseDTO.class);
      assertAll(
          () -> assertEquals("Pedido pronto", resultado.getStatusEntrega()),
          () -> assertTrue(pedido.getEntregadorId() == 0));
    }

    @Test
    @DisplayName("Muda Disponibilidade de entregador quando tiver um pedido sem entregador")
    void mudaDisponibilidadeEntregadorPedidoSemEntregador() throws Exception {
      
      pedidoRepository.save(pedido);
      pedidoService.preparaPedido(pedido.getId());

      assertEquals(estabelecimento.getPedidosSemEntregador().get(0).getId(), pedido.getId());

      driver.perform(put("/entregadores/" + entregador.getId() + "/disponibilidade")
          .param("estabelecimentoId", estabelecimento.getId().toString())
          .param("disponibilidade", "true")
          .param("codigoAcesso", entregador.getCodigoAcesso()));

      assertAll(
          () -> assertTrue(estabelecimento.getPedidosSemEntregador().isEmpty()),
          () -> assertEquals(pedido.getEntregadorId(), entregador.getId()));
    }

    @Test
    @DisplayName("Notifica cliente qaundo não há entregadores disponiveis")
    void notificaClienteQuandoNaoHaEntregadoresDisponiveis() throws Exception {
      // Arrange
      pedidoRepository.save(pedido);
      pedido.setStatusEntrega("Pedido em preparo");
      estabelecimento.getEntregadoresDisponiveis().clear();
      var cliente = RetornaEntidades.retornaCliente(pedido.getClienteId(), clienteRepository);

      // Act
      String responseJsonString = driver.perform(patch(URI_PEDIDOS + "/" + pedido.getId() + "/status-pedido/pronto")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(pedidoPostPutRequestDTO)))
              .andExpect(status().isOk())
              .andDo(print())
              .andReturn().getResponse().getContentAsString();

      PedidoResponseDTO resultado = objectMapper.readValue(responseJsonString,
              PedidoResponseDTO.class);

      // Assert
      assertTrue(outputStreamCaptor.toString()
              .trim().contains("Olá " + cliente.getNome() + ", seu pedido nao saiu para entrega, pois nao ha entregadores disponiveis"));
    }
  }
}
