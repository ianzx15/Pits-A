package com.ufcg.psoft.commerce.service.associacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufcg.psoft.commerce.Util.Util;
import com.ufcg.psoft.commerce.exception.EntregadorNotFoundException;
import com.ufcg.psoft.commerce.exception.estabelecimento.EstabelecimentoNaoEncontrado;
import com.ufcg.psoft.commerce.model.Associacao;
import com.ufcg.psoft.commerce.model.AssociacaoKey;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.repository.AssociacaoRepository;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;

@Service
public class AssociacaoServiceImpl implements AssociacaoService {

    @Autowired
    private AssociacaoRepository associacaoRepository;

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    private EntregadorRepository entregadorRepository;

    @Override
    public Associacao criar(Long entregadorId, String entregadorCodigoAcesso, Long estabelecimentoId) {
        Entregador entregador = entregadorRepository.findById(entregadorId)
            .orElseThrow(EntregadorNotFoundException::new);

        Util.verificaCodAcesso(entregadorCodigoAcesso, entregador.getCodigoAcesso());

        Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId)
            .orElseThrow(EstabelecimentoNaoEncontrado::new);

        return associacaoRepository.save(Associacao.builder()
            .estabelecimento(estabelecimento)
            .entregador(entregador)
            .build());

    }

    @Override
    public Associacao atualizar(Long entregadorId, String estabelecimentoCodigoAcesso, Long estabelecimentoId) {
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId)
            .orElseThrow(EstabelecimentoNaoEncontrado::new);
            
        Entregador entregador = entregadorRepository.findById(entregadorId)
            .orElseThrow(EntregadorNotFoundException::new);
        Util.verificaCodAcesso(estabelecimentoCodigoAcesso, estabelecimento.getCodigoAcesso());
        
        Associacao associacao = associacaoRepository.findByEstabelecimentoIdAndEntregadorId(estabelecimentoId, entregadorId);


        associacao.setStatus(true);

        associacaoRepository.flush();
        return associacao;
    }
    
}
