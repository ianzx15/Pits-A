package com.ufcg.psoft.commerce.service.entregador;

import com.ufcg.psoft.commerce.dto.entregador.EntregadorGetRequestDTO;
import com.ufcg.psoft.commerce.dto.entregador.EntregadorPostPutRequestDTO;
import com.ufcg.psoft.commerce.model.Entregador;

import java.util.List;

public interface EntregadorServicesInterface {
    public Entregador cadastrar(EntregadorPostPutRequestDTO data);
    public List<EntregadorGetRequestDTO> getAll();
    public EntregadorGetRequestDTO getById(Long id);
    public Entregador update(Long id, String codigoAcesso, EntregadorPostPutRequestDTO data);
    public void delete(Long id, String codigoAcesso);
    public void atualizarDisponibilidade(Long entregadorId, Long estabelecimentoId, String entregadorCodigoDeAcesso,
            Boolean disponibilidade);
}
