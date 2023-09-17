package com.ufcg.psoft.commerce.service.sabor;

import java.util.List;

import com.ufcg.psoft.commerce.dto.sabor.SaborDisponibilidadePatchDTO;
import com.ufcg.psoft.commerce.dto.sabor.SaborPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.sabor.SaborResponseDTO;

public interface SaborService {
    public SaborResponseDTO recuperar(Long estabelecimendoId, String codigoDeAcceso, Long saborId);

    public List<SaborResponseDTO> recuperarTodos(Long estabelecimendoId, String codigoDeAcceso);

    public SaborResponseDTO criar(Long estabelecimendoId, String codigoDeAcceso,
            SaborPostPutRequestDTO saborPostPutRequestDTO);

    public SaborResponseDTO atualizar(Long estabelecimendoId, String codigoDeAcceso, Long saborId,
            SaborPostPutRequestDTO saborPostPutRequestDTO);

    public void deletar(Long estabelecimendoId, String codigoDeAcceso, Long saborId);

    public SaborResponseDTO atualizarDisponibilidade(Long estabelecimendoId, String codigoDeAcceso, Long saborId,
            SaborDisponibilidadePatchDTO disponibilidadePatchDTO);

}