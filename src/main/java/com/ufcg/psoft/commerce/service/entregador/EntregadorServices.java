package com.ufcg.psoft.commerce.service.entregador;

import com.ufcg.psoft.commerce.Util.RetornaEntidades;
import com.ufcg.psoft.commerce.Util.Util;
import com.ufcg.psoft.commerce.dto.entregador.EntregadorGetRequestDTO;
import com.ufcg.psoft.commerce.dto.entregador.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.exception.associacao.AssociacaoNotFoundException;
import com.ufcg.psoft.commerce.exception.entregador.EntregadorNotFoundException;
import com.ufcg.psoft.commerce.model.Associacao;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Pedido;
import com.ufcg.psoft.commerce.repository.AssociacaoRepository;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
import com.ufcg.psoft.commerce.repository.PedidoRepository;
import com.ufcg.psoft.commerce.service.pedido.PedidoServiceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EntregadorServices implements EntregadorServicesInterface {

    @Autowired
    private EntregadorRepository entregadorRepository;

    @Autowired
    private AssociacaoRepository associacaoRepository;

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    private PedidoServiceImpl pedidoService;


    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Entregador cadastrar(EntregadorPostPutRequestDTO data) {
        return this.entregadorRepository.save(this.modelMapper.map(data, Entregador.class));
    }

    @Override
    public List<EntregadorGetRequestDTO> getAll() {
        Collection<Entregador> allEntregadores = this.entregadorRepository.findAll();
        return allEntregadores
                .stream()
                .map(entregador -> this.modelMapper
                        .map(entregador, EntregadorGetRequestDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public EntregadorGetRequestDTO getById(Long id) {
        Entregador entregador = RetornaEntidades.retornaEntregador(id, this.entregadorRepository);
        return modelMapper.map(entregador, EntregadorGetRequestDTO.class);
    }

    @Override
    public Entregador update(Long id, String codigoAcesso, EntregadorPostPutRequestDTO data) {

        this.entregadorRepository.findById(id).map(record -> {
            Util.verificaCodAcesso(codigoAcesso, record.getCodigoAcesso());
            record.setNome(data.getNome());
            record.setTipoVeiculo(data.getTipoVeiculo());
            record.setPlacaVeiculo(data.getPlacaVeiculo());
            record.setCorVeiculo(data.getCorVeiculo());
            record.setCodigoAcesso(data.getCodigoAcesso());

            return this.entregadorRepository.save(record);
        }).orElseThrow(EntregadorNotFoundException::new);

        return this.entregadorRepository.findById(id).get();
    }

    @Override
    public void delete(Long id, String codigoAcesso) {
        Entregador entity = RetornaEntidades.retornaEntregador(id, this.entregadorRepository);
        Util.verificaCodAcesso(codigoAcesso, entity.getCodigoAcesso());
        this.entregadorRepository.deleteById(id);
    }

    @Override
    public void atualizarDisponibilidade(Long entregadorId, Long estabelecimentoId, String entregadorCodigoDeAcesso,
            Boolean disponibilidade) {
        Estabelecimento estabelecimento = RetornaEntidades.retornaEstabelecimento(estabelecimentoId,
                estabelecimentoRepository);
        Entregador entregador = RetornaEntidades.retornaEntregador(entregadorId, entregadorRepository);
        Util.verificaCodAcesso(entregadorCodigoDeAcesso, entregador.getCodigoAcesso());

        try {
            Associacao associacao = associacaoRepository.findByEstabelecimentoIdAndEntregadorId(estabelecimentoId,
                    entregadorId);
            associacao.setDisponibilidade(disponibilidade);
            if (disponibilidade) {
                estabelecimento.getEntregadoresDisponiveis().add(entregador);

                if(!estabelecimento.getPedidosSemEntregador().isEmpty()) {   
                    Pedido pedido = estabelecimento.getPedidosSemEntregador().remove(0);       
                    pedidoService.atribuiEntregadorAutomaticamente(pedido);
                }
            } else {
                estabelecimento.getEntregadoresDisponiveis().remove(entregador);
            }
            estabelecimentoRepository.flush();
            associacaoRepository.flush();

        } catch (Exception e) {
            throw new AssociacaoNotFoundException();
        }
    }
}
