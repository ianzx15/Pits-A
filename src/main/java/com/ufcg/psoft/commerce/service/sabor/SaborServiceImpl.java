package com.ufcg.psoft.commerce.service.sabor;

import java.util.List;
import java.util.Set;

import com.ufcg.psoft.commerce.model.Cliente;
import com.ufcg.psoft.commerce.service.cliente.Notification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufcg.psoft.commerce.dto.sabor.SaborDisponibilidadePatchDTO;
import com.ufcg.psoft.commerce.dto.sabor.SaborPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.sabor.SaborResponseDTO;
import com.ufcg.psoft.commerce.exception.sabor.SaborNaoEncontrado;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.model.Sabor;
import com.ufcg.psoft.commerce.repository.SaborRepository;
import com.ufcg.psoft.commerce.service.estabelecimento.EstabelecimentoService;

@Service
public class SaborServiceImpl implements SaborService {
  @Autowired
  private SaborRepository saborRepository;
  @Autowired
  private EstabelecimentoService estabelecimentoService;
  @Autowired
  private ModelMapper modelMapper;
  @Autowired
  private Notification notification;

  @Override
  public SaborResponseDTO recuperar(Long estabelecimendoId, String codigoDeAcceso, Long saborId) {
    estabelecimentoService.getEstabelecimento(codigoDeAcceso, estabelecimendoId);

    return modelMapper.map(
        saborRepository.findById(saborId).orElseThrow(() -> new SaborNaoEncontrado()),
        SaborResponseDTO.class);
  }

  @Override
  public List<SaborResponseDTO> recuperarTodos(Long estabelecimendoId, String codigoDeAcceso) {
    Estabelecimento estabelecimento = estabelecimentoService.getEstabelecimento(codigoDeAcceso, estabelecimendoId);
    List<Sabor> sabores = estabelecimento.getSabores();

    return sabores.stream().map((sabor) -> {
      return modelMapper.map(sabor, SaborResponseDTO.class);
    }).toList();
  }

  @Override
  public SaborResponseDTO criar(Long estabelecimendoId, String codigoDeAcceso,
      SaborPostPutRequestDTO saborPostPutRequestDTO) {
    Estabelecimento estabelecimento = estabelecimentoService.getEstabelecimento(codigoDeAcceso, estabelecimendoId);
    Sabor sabor = modelMapper.map(saborPostPutRequestDTO, Sabor.class);

    sabor.setEstabelecimento(estabelecimento);
    saborRepository.saveAndFlush(sabor);

    return modelMapper.map(sabor, SaborResponseDTO.class);
  }

  @Override
  public SaborResponseDTO atualizar(Long estabelecimendoId, String codigoDeAcceso, Long saborId,
      SaborPostPutRequestDTO saborPostPutRequestDTO) {
    estabelecimentoService.getEstabelecimento(codigoDeAcceso, estabelecimendoId);
    saborRepository.findById(saborId).orElseThrow(() -> new SaborNaoEncontrado());

    Sabor sabor = modelMapper.map(saborPostPutRequestDTO, Sabor.class);
    sabor.setId(saborId);

    return modelMapper.map(saborRepository.saveAndFlush(sabor), SaborResponseDTO.class);
  }

  @Override
  public void deletar(Long estabelecimendoId, String codigoDeAcceso, Long saborId) {
    estabelecimentoService.getEstabelecimento(codigoDeAcceso, estabelecimendoId);
    saborRepository.findById(saborId).orElseThrow(() -> new SaborNaoEncontrado());

    saborRepository.deleteById(saborId);
  }

  @Override
  public SaborResponseDTO atualizarDisponibilidade(Long estabelecimendoId, String codigoDeAcceso, Long saborId,
      SaborDisponibilidadePatchDTO disponibilidadePatchDTO) {
    estabelecimentoService.getEstabelecimento(codigoDeAcceso, estabelecimendoId);

    Sabor sabor = retornaSabor(saborId);
    sabor.setDisponivel(disponibilidadePatchDTO.getDisponivel());

    if (disponibilidadePatchDTO.getDisponivel()) {
      this.notificaDisponibilidade(sabor);
    }

    return modelMapper.map(sabor, SaborResponseDTO.class);
  }

  private void notificaDisponibilidade(Sabor sabor) {
    Set<Cliente> clientesInteressados = sabor.getClientesInteressados();
    if (clientesInteressados.size() > 0) {
      clientesInteressados.forEach(cliente -> this.notification.notificate(sabor.getNome(),cliente.getNome()));
    }
  }

  private Sabor retornaSabor(Long saborId){
    return this.saborRepository.findById(saborId).orElseThrow(() -> new SaborNaoEncontrado());
  }

}
