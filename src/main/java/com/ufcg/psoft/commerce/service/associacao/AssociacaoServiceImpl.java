package com.ufcg.psoft.commerce.service.associacao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufcg.psoft.commerce.Util.Util;
import com.ufcg.psoft.commerce.exception.entregador.EntregadorNotFoundException;
import com.ufcg.psoft.commerce.exception.estabelecimento.EstabelecimentoNaoEncontrado;
import com.ufcg.psoft.commerce.model.Associacao;
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
        Entregador entregador = retornaEntregador(entregadorId);

        Util.verificaCodAcesso(entregadorCodigoAcesso, entregador.getCodigoAcesso());

        Estabelecimento estabelecimento = retornaEstabelecimento(estabelecimentoId);

        return associacaoRepository.save(Associacao.builder()
            .estabelecimento(estabelecimento)
            .entregador(entregador)
            .build());

    }

    @Override
    public Associacao atualizar(Long entregadorId, String estabelecimentoCodigoAcesso, Long estabelecimentoId) {
        Estabelecimento estabelecimento = retornaEstabelecimento(estabelecimentoId);
        retornaEntregador(entregadorId);

        Util.verificaCodAcesso(estabelecimentoCodigoAcesso, estabelecimento.getCodigoAcesso());

        Associacao associacao = associacaoRepository.findByEstabelecimentoIdAndEntregadorId(estabelecimentoId, entregadorId);

        associacao.setStatus(true);

        associacaoRepository.flush();
        return associacao;
    }


    private Estabelecimento retornaEstabelecimento(Long estabelecimentoId){
        return this.estabelecimentoRepository.findById(estabelecimentoId).orElseThrow(() -> new EstabelecimentoNaoEncontrado());
    }

    private Entregador retornaEntregador(Long entregadorId){
        return this.entregadorRepository.findById(entregadorId).orElseThrow(() -> new EntregadorNotFoundException());
    }
}
