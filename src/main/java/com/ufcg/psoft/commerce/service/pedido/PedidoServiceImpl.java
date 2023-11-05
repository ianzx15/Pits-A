package com.ufcg.psoft.commerce.service.pedido;

import java.util.List;
import java.util.stream.Collectors;
import com.ufcg.psoft.commerce.Util.RetornaEntidades;
import com.ufcg.psoft.commerce.dto.pedido.PedidoEntregadorResponseDTO;
import com.ufcg.psoft.commerce.exception.pedido.PedidoNaoProntoException;
import com.ufcg.psoft.commerce.exception.pedido.SemEntregadoresDispException;
import com.ufcg.psoft.commerce.model.*;
import com.ufcg.psoft.commerce.observer.NotificaEntregaPedido;
import com.ufcg.psoft.commerce.observer.NotificaEntregador;
import com.ufcg.psoft.commerce.observer.NotificaPedidoEmRota;
import com.ufcg.psoft.commerce.observer.NotificaSemEntregadoresDisp;
import com.ufcg.psoft.commerce.repository.*;
import com.ufcg.psoft.commerce.service.estabelecimento.EstabelecimentoService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ufcg.psoft.commerce.Util.Util;
import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.pedido.PedidoResponseDTO;
import com.ufcg.psoft.commerce.exception.cliente.ClienteNotFoundException;
import com.ufcg.psoft.commerce.exception.estabelecimento.EstabelecimentoNaoEncontrado;
import com.ufcg.psoft.commerce.exception.pedido.MetodoDePagamentoIndisponivel;
import com.ufcg.psoft.commerce.exception.pedido.PedidoNaoPertenceAEntidade;
import com.ufcg.psoft.commerce.exception.pedido.PedidoNaoPodeSerCancelado;

@Service
public class PedidoServiceImpl implements PedidoService, NotificaEntregaPedido, NotificaPedidoEmRota, NotificaEntregador {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EstabelecimentoService estabelecimentoService;

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    PizzaRepository pizzaRepository;

    @Autowired
    EntregadorRepository entregadorRepository;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    NotificaSemEntregadoresDisp notificaSemEntregadoresDisp;

    @Override
    public PedidoResponseDTO criar(Long clienteId, String clienteCodigoAcesso, Long estabelecimentoId,
            PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {

        Cliente cliente = RetornaEntidades.retornaCliente(clienteId, this.clienteRepository);
        Util.verificaCodAcesso(clienteCodigoAcesso, cliente.getCodigoAcesso());
        estabelecimentoRepository.findById(estabelecimentoId).orElseThrow(() -> new EstabelecimentoNaoEncontrado());

        if (pedidoPostPutRequestDTO.getEnderecoEntrega() == null) {
            pedidoPostPutRequestDTO.setEnderecoEntrega(cliente.getEndereco());
        }

        pizzaRepository.saveAll(pedidoPostPutRequestDTO.getPizzas());

        Pedido pedido = pedidoRepository.save(Pedido.builder()
                .clienteId(clienteId)
                .estabelecimentoId(estabelecimentoId)
                .entregadorId(null)
                .enderecoEntrega(pedidoPostPutRequestDTO.getEnderecoEntrega())
                .preco(calculaPrecoPedido(pedidoPostPutRequestDTO.getPizzas()))
                .pizzas(pedidoPostPutRequestDTO.getPizzas())
                .build());

        return modelMapper.map(pedido, PedidoResponseDTO.class);
    }

    @Override
    public PedidoResponseDTO atualizar(Long pedidoId, String clienteCodigoAcesso,
            PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {
        Pedido pedido = RetornaEntidades.retornaPedido(pedidoId, this.pedidoRepository);
        Cliente cliente = RetornaEntidades.retornaCliente(pedido.getClienteId(), this.clienteRepository);

        Util.verificaCodAcesso(clienteCodigoAcesso, cliente.getCodigoAcesso());

        if (pedidoPostPutRequestDTO.getEnderecoEntrega() != null) {
            pedido.setEnderecoEntrega(pedidoPostPutRequestDTO.getEnderecoEntrega());
        } else {
            pedido.setEnderecoEntrega(cliente.getEndereco());
        }

        pizzaRepository.saveAll(pedidoPostPutRequestDTO.getPizzas());

        pedido.setPizzas(pedidoPostPutRequestDTO.getPizzas());
        pedido.setPreco(calculaPrecoPedido(pedido.getPizzas()));

        pedidoRepository.saveAndFlush(pedido);

        return modelMapper.map(pedido, PedidoResponseDTO.class);
    }

    @Override
    public List<Pedido> recuperaTodosPedidosCliente(Long clienteId, String codigoAcesso) {
        Cliente cliente = RetornaEntidades.retornaCliente(clienteId, this.clienteRepository);
        Util.verificaCodAcesso(codigoAcesso, cliente.getCodigoAcesso());

        return pedidoRepository.findByClienteId(clienteId);
    }

    @Override
    public Pedido recuperaPedidoPorIdCliente(Long pedidoId, Long clienteId, String codigoAcesso) {
        Pedido pedido = RetornaEntidades.retornaPedido(pedidoId, this.pedidoRepository);
        if (!pedido.getClienteId().equals(clienteId)) {
            throw new PedidoNaoPertenceAEntidade("O pedido nao pertence ao cliente!");
        }
        Cliente cliente = RetornaEntidades.retornaCliente(clienteId, this.clienteRepository);

        Util.verificaCodAcesso(codigoAcesso, cliente.getCodigoAcesso());

        return pedido;
    }

    @Override
    public List<Pedido> recuperaTodosPedidosEstabelecimento(Long estabelecimentoId, String codigoAcesso) {
        Estabelecimento estabelecimento = RetornaEntidades.retornaEstabelecimento(estabelecimentoId, this.estabelecimentoRepository);
        Util.verificaCodAcesso(codigoAcesso, estabelecimento.getCodigoAcesso());

        return pedidoRepository.findByEstabelecimentoId(estabelecimentoId);
    }

    @Override
    public Pedido recuperaPedidoPorIdEstabelecimento(Long pedidoId, Long estabelecimentoId, String codigoAcesso) {
        Pedido pedido = RetornaEntidades.retornaPedido(pedidoId, this.pedidoRepository);
        if (!pedido.getEstabelecimentoId().equals(estabelecimentoId)) {
            throw new PedidoNaoPertenceAEntidade("O pedido nao pertence ao estabelecimento!");
        }
        Estabelecimento estabelecimento = RetornaEntidades.retornaEstabelecimento(estabelecimentoId, this.estabelecimentoRepository);
        Util.verificaCodAcesso(codigoAcesso, estabelecimento.getCodigoAcesso());

        return pedido;
    }

    @Override
    public List<PedidoResponseDTO> clienteRecuperaPedidoPorEstabelecimento(Long clienteId, Long estabelecimentoId,
            Long pedidoId, String clienteCodigoAcesso) {
        estabelecimentoRepository.findById(estabelecimentoId).orElseThrow(() -> new EstabelecimentoNaoEncontrado());
        clienteRepository.findById(clienteId).orElseThrow(() -> new ClienteNotFoundException());
        if (pedidoId == null) {
            List<Pedido> pedidos = pedidoRepository.findByClienteIdAndEstabelecimentoId(clienteId, estabelecimentoId);
            return pedidos.stream().sorted((p1, p2) -> {
                if (!p1.getStatusEntrega().equals("Pedido entregue")) {
                    return -1;
                } else {
                    return 1;
                }
            }).map(p -> modelMapper
                    .map(p, PedidoResponseDTO.class))
                    .collect(Collectors.toList());
        }

        Pedido pedido = RetornaEntidades.retornaPedido(pedidoId, this.pedidoRepository);

        if (!pedido.getClienteId().equals(clienteId)) {
            throw new PedidoNaoPertenceAEntidade("O pedido nao pertence ao cliente!");
        } else if (!pedido.getEstabelecimentoId().equals(estabelecimentoId)) {
            throw new PedidoNaoPertenceAEntidade("O pedido nao pertence ao estabelecimento!");
        }

        Cliente cliente = RetornaEntidades.retornaCliente(clienteId, this.clienteRepository);

        Util.verificaCodAcesso(clienteCodigoAcesso, cliente.getCodigoAcesso());

        return List.of(modelMapper.map(pedido, PedidoResponseDTO.class));
    }

    @Override
    public void deletePorCliente(Long pedidoId, Long clienteId, String codigoAcesso) {
        Pedido pedido = RetornaEntidades.retornaPedido(pedidoId, this.pedidoRepository);
        if (!pedido.getClienteId().equals(clienteId)) {
            throw new PedidoNaoPertenceAEntidade("O pedido nao pertence ao cliente!");
        }
        Cliente cliente = RetornaEntidades.retornaCliente(clienteId, this.clienteRepository);
        Util.verificaCodAcesso(codigoAcesso, cliente.getCodigoAcesso());

        pedidoRepository.deleteById(pedidoId);
    }

    @Override
    public List<PedidoResponseDTO> recuperaHistoricoFiltradoPorEntrega(Long clientId, Long estabelecientoId,
            String codigoAcessoCliente, String statusEntrega) {
        Cliente cliente = RetornaEntidades.retornaCliente(clientId, this.clienteRepository);
        Util.verificaCodAcesso(codigoAcessoCliente, cliente.getCodigoAcesso());
        List<Pedido> pedidos = pedidoRepository.findByClienteIdAndEstabelecimentoIdAndStatusEntrega(clientId,
                estabelecientoId, statusEntrega);

        return pedidos.stream().map(p -> modelMapper
                .map(p, PedidoResponseDTO.class))
                .collect(Collectors.toList());

    }

    @Override
    public void deletePorEstabelecimento(Long pedidoId, Long estabelecimentoId, String codigoAcesso) {
        Pedido pedido = RetornaEntidades.retornaPedido(pedidoId, this.pedidoRepository);
        if (!pedido.getEstabelecimentoId().equals(estabelecimentoId)) {
            throw new PedidoNaoPertenceAEntidade("O pedido nao pertence ao estabelecimento!");
        }
        Estabelecimento estabelecimento = RetornaEntidades.retornaEstabelecimento(estabelecimentoId, this.estabelecimentoRepository);
        Util.verificaCodAcesso(codigoAcesso, estabelecimento.getCodigoAcesso());

        this.pedidoRepository.deleteById(pedidoId);
    }

    @Override
    public void deleteTodosPedidosEstabelecimento(Long estabelecimentoId) {
        estabelecimentoRepository.findById(estabelecimentoId).orElseThrow(() -> new EstabelecimentoNaoEncontrado());
        pedidoRepository.deleteByEstabelecimentoId(estabelecimentoId);
    }

    @Override
    public void deleteTodosPedidosCliente(Long clienteId) {
        clienteRepository.findById(clienteId).orElseThrow(() -> new ClienteNotFoundException());
        pedidoRepository.deleteByClienteId(clienteId);
    }

    private Double calculaPrecoPedido(List<Pizza> pizzas) {
        Double preco = 0.0;
        for (Pizza pizza : pizzas) {
            preco += calculaSubTotal(pizza);
        }
        return preco;
    }

    private Double calculaSubTotal(Pizza pizza) {
        Double valor = 0.0;
        if (pizza.getTamanho().equals("media")) {
            valor += pizza.getSabor1().getPrecoM();
        } else if (pizza.getTamanho().equals("grande")) {
            valor += pizza.getSabor1().getPrecoG();
            if (pizza.getSabor2() != null) {
                valor += pizza.getSabor2().getPrecoG();
                valor /= 2;
            }
        }
        return valor;
    }

    @Override
    public PedidoResponseDTO confirmarPagamento(Long clientId, Long pedidoId, String codigoAcessoCliente,
            String metodoPagamento) {
        List<String> metodosDisponiveis = List.of("PIX", "CREDITO", "DEBITO");
        if (!metodosDisponiveis.contains(metodoPagamento)) {
            throw new MetodoDePagamentoIndisponivel();
        }
        Cliente cliente = RetornaEntidades.retornaCliente(clientId, this.clienteRepository);
        Util.verificaCodAcesso(codigoAcessoCliente, cliente.getCodigoAcesso());
        Pedido pedido = RetornaEntidades.retornaPedido(pedidoId, this.pedidoRepository);

        pedido.setStatusPagamento(true);
        if (metodoPagamento.equals("PIX")) {
            pedido.setPreco(pedido.getPreco() * 0.95);
        } else if (metodoPagamento.equals("DEBITO")) {
            pedido.setPreco(pedido.getPreco() * 0.975);
        }

        pedido.setStatusEntrega("Pedido em preparo");
        pedidoRepository.flush();

        return modelMapper.map(pedido, PedidoResponseDTO.class);
    }

    @Override
    public PedidoResponseDTO confirmarEntrega(Long pedidoId, Long clienteId, String clienteCodigoAcesso) {
        Pedido pedido = RetornaEntidades.retornaPedido(pedidoId, this.pedidoRepository);
        Cliente cliente = RetornaEntidades.retornaCliente(clienteId, this.clienteRepository);
        Util.verificaCodAcesso(clienteCodigoAcesso, cliente.getCodigoAcesso());

        pedido.setStatusEntrega("Pedido entregue");
        RetornaEntidades.retornaEstabelecimento(pedido.getEstabelecimentoId(), this.estabelecimentoRepository);
        this.pedidoRepository.flush();
        notificaEntrega(pedido.getEstabelecimentoId(), pedidoId);

        return modelMapper.map(pedido, PedidoResponseDTO.class);
    }

    @Override
    public PedidoResponseDTO preparaPedido(Long pedidoId) {
        Pedido pedido = RetornaEntidades.retornaPedido(pedidoId, pedidoRepository);
        pedido.setStatusEntrega("Pedido pronto");
        this.pedidoRepository.flush();

        atribuiEntregadorAutomaticamente(pedido);

        return modelMapper.map(pedido, PedidoResponseDTO.class);
    }

    @Override
    public PedidoEntregadorResponseDTO atribuiEntregador(Long pedidoId, String estabelecimentoCodigoAcesso, Long estabelecimentoId) {
        Pedido pedido = RetornaEntidades.retornaPedido(pedidoId, pedidoRepository);
        
        if (pedido.getEstabelecimentoId() != estabelecimentoId) {
            throw new PedidoNaoPertenceAEntidade("O pedido nao pertence ao estabelecimento!");
        }

        Estabelecimento estabelecimento = RetornaEntidades.retornaEstabelecimento(estabelecimentoId, estabelecimentoRepository);
        
        Util.verificaCodAcesso(estabelecimentoCodigoAcesso , estabelecimento.getCodigoAcesso());

        if (!pedido.getStatusEntrega().equals("Pedido pronto")) {
            throw new PedidoNaoProntoException();
        }

        if (estabelecimento.getEntregadoresDisponiveis().isEmpty()) {
            throw new SemEntregadoresDispException();
        }

        Entregador entregador = estabelecimento.getEntregadoresDisponiveis().get(0);

        pedido.setEntregadorId(entregador.getId());
        pedido.setStatusEntrega("Pedido em rota");
        notificaEmRota(pedidoId, estabelecimentoId, entregador);

        this.entregadorRepository.flush();
        this.pedidoRepository.flush();

        return modelMapper.map(pedido, PedidoEntregadorResponseDTO.class);
    }

    @Override
    public void notificaEntrega(Long estabelecimentoId, Long pedidoId) {

        System.out.println("Olá estabelecimento " + estabelecimentoId + ", o pedido " + pedidoId + " mudou de status para Pedido entregue!");
    }

    @Override
    public void notificaEmRota(Long pedidoId, Long clientId, Entregador entregador) {
        System.out.println("Olá Cliente, " + clientId +  " o seu pedido " + pedidoId + " está em rota, o entregador responsável é: " + entregador.toString());
    }

    @Override
    public void notificaEntregador(Long pedidoId, Long entregadorId) {
        System.out.println("Olá entregador " + entregadorId + ", o pedido " + pedidoId + " está disponível para entrega!");
    }

    @Override
    public void cancelarPedido(Long pedidoId, String clienteCodigoAcesso) {
        Pedido pedido = RetornaEntidades.retornaPedido(pedidoId, pedidoRepository);
        Cliente cliente = RetornaEntidades.retornaCliente(pedido.getClienteId(), clienteRepository);

        Util.verificaCodAcesso(cliente.getCodigoAcesso(), clienteCodigoAcesso);

        if (pedido.getStatusEntrega().equals("Pedido recebido") || pedido.getStatusEntrega().equals("Pedido em preparo")) {
            pedidoRepository.deleteById(pedidoId);
        } else {
            throw new PedidoNaoPodeSerCancelado();
        }

    }

    public void atribuiEntregadorAutomaticamente(Pedido pedido) {
        Estabelecimento estabelecimento = RetornaEntidades.retornaEstabelecimento(pedido.getEstabelecimentoId(), estabelecimentoRepository);

        if (!estabelecimento.getEntregadoresDisponiveis().isEmpty()) {
            Entregador entregador = estabelecimento.getEntregadoresDisponiveis().remove(0);
            estabelecimento.getEntregadoresDisponiveis().add(entregador);

            pedido.setEntregadorId(entregador.getId());
            pedido.setStatusEntrega("Pedido em rota");
            notificaEntregador(pedido.getId(), pedido.getEntregadorId());

            this.pedidoRepository.flush();
        } else {
            var cliente = RetornaEntidades.retornaCliente(pedido.getClienteId(), clienteRepository);
            notificaSemEntregadoresDisp.notificaSemEntregadoresDisp(cliente.getNome());
            estabelecimento.getPedidosSemEntregador().add(pedido);
            this.estabelecimentoRepository.flush();
        }
    }
}
