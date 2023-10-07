package com.ufcg.psoft.commerce.service.pedido;

import java.util.List;

import com.ufcg.psoft.commerce.model.Estabelecimento;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufcg.psoft.commerce.Util.Util;
import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.pedido.PedidoResponseDTO;
import com.ufcg.psoft.commerce.exception.cliente.ClienteNotFoundException;
import com.ufcg.psoft.commerce.exception.estabelecimento.EstabelecimentoNaoEncontrado;
import com.ufcg.psoft.commerce.exception.pedido.PedidoNaoEncontrado;
import com.ufcg.psoft.commerce.exception.pedido.PedidoNaoPertenceAEntidade;
import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.model.Estabelecimento;
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
    public List<Pedido> recuperaTodosPedidos(Long clienteId, String codigoAcesso) {
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(() -> new ClienteNotFoundException());
        Util.verificaCodAcesso(codigoAcesso, cliente.getCodigoAcesso());

        return pedidoRepository.findByClienteId(clienteId);
    }

    @Override
    public Pedido recuperaPedidoPorIdCliente(Long pedidoId, Long clienteId, String codigoAcesso) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(() -> new PedidoNaoEncontrado());
        if (!pedido.getClienteId().equals(clienteId)) {
            throw new PedidoNaoPertenceAEntidade("O pedido nao pertence ao cliente!");
        }
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(() -> new ClienteNotFoundException());
    
        Util.verificaCodAcesso(codigoAcesso, cliente.getCodigoAcesso());

        return pedido;
    }

    @Override
    public List<Pedido> recuperaPedidosPorEstabelecimento(Long estabelecimentoId, String codigoAcesso) {
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId).orElseThrow(() -> new EstabelecimentoNaoEncontrado());
        Util.verificaCodAcesso(codigoAcesso, estabelecimento.getCodigoAcesso());

        return pedidoRepository.findByEstabelecimentoId(estabelecimentoId);
    }

    @Override
    public Pedido recuperaPedidoPorIdEstabelecimento(Long pedidoId, Long estabelecimentoId, String codigoAcesso) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(() -> new PedidoNaoEncontrado());
        if (!pedido.getEstabelecimentoId().equals(estabelecimentoId)) {
            throw new PedidoNaoPertenceAEntidade("O pedido nao pertence ao estabelecimento!");
        }
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId).orElseThrow(() -> new EstabelecimentoNaoEncontrado());
        Util.verificaCodAcesso(codigoAcesso, estabelecimento.getCodigoAcesso());

        return pedido;
    }

    @Override
    public void deletePorCliente(Long pedidoId, Long clienteId, String codigoAcesso){
       Pedido pedido = this.pedidoRepository.findById(pedidoId).orElseThrow(() -> new PedidoNaoEncontrado());
       Cliente cliente = this.clienteRepository.findById(clienteId).orElseThrow(() -> new ClienteNotFoundException());
       Util.verificaCodAcesso(codigoAcesso, cliente.getCodigoAcesso());
       this.pedidoRepository.deleteById(pedidoId);
    }

    @Override
    public void deletePorEstabelecimento(Long pedidoId, Long estabelecimentoId, String codigoAcesso){
        Pedido pedido = this.pedidoRepository.findById(pedidoId).orElseThrow(() -> new PedidoNaoEncontrado());
        Estabelecimento estabelecimento = this.estabelecimentoRepository.findById(estabelecimentoId).orElseThrow(() -> new EstabelecimentoNaoEncontrado());
        Util.verificaCodAcesso(codigoAcesso, estabelecimento.getCodigoAcesso());
        this.pedidoRepository.deleteById(pedidoId);
    }

    @Override
    public void deleteTodosSaboresEstabelecimento(Long estabelecimentoId){
        Estabelecimento estabelecimento = this.estabelecimentoRepository.findById(estabelecimentoId).orElseThrow(() -> new EstabelecimentoNaoEncontrado());
        this.pedidoRepository.deleteAll();
    }

    @Override
    public void deleteTodosSaboresCliente(Long clienteId){
        Cliente cliente = this.clienteRepository.findById(clienteId).orElseThrow(() -> new ClienteNotFoundException());
        this.pedidoRepository.deleteAll();
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

}
