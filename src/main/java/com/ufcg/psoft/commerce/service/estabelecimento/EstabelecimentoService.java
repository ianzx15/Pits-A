package com.ufcg.psoft.commerce.service.estabelecimento;

import com.ufcg.psoft.commerce.dto.estabelecimento.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.estabelecimento.EstabelecimentoResponseDTO;
import com.ufcg.psoft.commerce.model.Estabelecimento;

import java.util.List;

public interface EstabelecimentoService {

    public EstabelecimentoResponseDTO recuperarEstabelecimento(String codigoAcesso, Long id);
    public List<EstabelecimentoResponseDTO> recuperarTodosEstabelecimentos();
    public EstabelecimentoResponseDTO criarEstabelecimento(EstabelecimentoPostPutRequestDTO estabelecimento);
    public EstabelecimentoResponseDTO atualizarEstabelecimento(String codigoAcesso, Long id,  EstabelecimentoPostPutRequestDTO estabelecimento);
    public void deletarEstabelecimento(String codigoAcesso, Long id);
    public Estabelecimento getEstabelecimento(String codigoAcesso, Long id);

}
