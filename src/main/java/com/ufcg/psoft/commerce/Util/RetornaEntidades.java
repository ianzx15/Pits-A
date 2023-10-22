package com.ufcg.psoft.commerce.Util;

import com.ufcg.psoft.commerce.exception.cliente.ClienteNotFoundException;
import com.ufcg.psoft.commerce.exception.entregador.EntregadorNotFoundException;
import com.ufcg.psoft.commerce.exception.estabelecimento.EstabelecimentoNaoEncontrado;
import com.ufcg.psoft.commerce.exception.pedido.PedidoNaoEncontrado;
import com.ufcg.psoft.commerce.exception.sabor.SaborNaoEncontrado;
import com.ufcg.psoft.commerce.model.*;
import com.ufcg.psoft.commerce.repository.*;

public class RetornaEntidades {

    public static Cliente retornaCliente(Long clienteId, ClienteRepository clienteRepository){
        return clienteRepository.findById(clienteId).orElseThrow(() -> new ClienteNotFoundException());
    }
    public static Estabelecimento retornaEstabelecimento(Long estabelecimentoId, EstabelecimentoRepository estabelecimentoRepository){
        return estabelecimentoRepository.findById(estabelecimentoId).orElseThrow(() -> new EstabelecimentoNaoEncontrado());
    }
    public static Pedido retornaPedido (Long pedidoId, PedidoRepository pedidoRepository){
        return pedidoRepository.findById(pedidoId).orElseThrow(() -> new PedidoNaoEncontrado());
    }

    public static Sabor retornaSabor (Long saborId, SaborRepository saborRepository){
        return saborRepository.findById(saborId).orElseThrow(() -> new SaborNaoEncontrado());
    }

    public static Entregador retornaEntregador (Long entregadorId, EntregadorRepository entregadorRepository){
        return entregadorRepository.findById(entregadorId).orElseThrow(() -> new EntregadorNotFoundException());
    }


}
