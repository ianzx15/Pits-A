package com.ufcg.psoft.commerce.service.pedido;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufcg.psoft.commerce.Util.Util;
import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.pedido.PedidoResponseDTO;
import com.ufcg.psoft.commerce.exception.cliente.ClienteNotFoundException;
import com.ufcg.psoft.commerce.exception.estabelecimento.EstabelecimentoNaoEncontrado;
import com.ufcg.psoft.commerce.exception.pedido.PedidoNaoEncontrado;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.model.Pizza;
import com.ufcg.psoft.commerce.repository.ClienteRepository;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.repository.PizzaRepository;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    PizzaRepository pizzaRepository;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    @Override
    public PedidoResponseDTO criar(Long clienteId, String clienteCodigoAcesso, Long estabelecimentoId,
            PedidoPostPutRequestDTO pedidoPostPutRequestDTO) {

        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(() -> new ClienteNotFoundException());
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
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(() -> new PedidoNaoEncontrado());
        Cliente cliente = clienteRepository.findById(pedido.getClienteId()).get();

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
    public List<Pedido> recuperaTodosCliente(Long clienteId) {
        clienteRepository.findById(clienteId).orElseThrow(() -> new ClienteNotFoundException());
        return pedidoRepository.findByClienteId(clienteId);
    }

    private Double calculaPrecoPedido(List<Pizza> pizzas) {
        Double preco = 0.0;
        for (Pizza pizza : pizzas) {
           preco += pizza.calculaSubTotal();
        }
        return preco;
    }

}
