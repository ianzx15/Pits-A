package com.ufcg.psoft.commerce.services.estabelecimento;

import com.ufcg.psoft.commerce.dto.estabelecimento.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.estabelecimento.EstabelecimentoResponseDTO;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.repositories.EstabelecimentoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstabelecimentoServiceImpl implements EstabelecimentoService {

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    ModelMapper modelMapper;

    public EstabelecimentoResponseDTO recuperarEstabelecimento(Long id) {
        Estabelecimento estabelecimento = this.estabelecimentoRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Estabelecimento não encontrado!")
        );

        return modelMapper.map(estabelecimento, EstabelecimentoResponseDTO.class);
    }

    public List<EstabelecimentoResponseDTO> recuperarTodosEstabelecimentos() {
        List<Estabelecimento> estabelecimentos = this.estabelecimentoRepository.findAll();

        return estabelecimentos.stream()
                .map(estabelecimento -> modelMapper.map(estabelecimento, EstabelecimentoResponseDTO.class))
                .collect(Collectors.toList());
    }

    public EstabelecimentoResponseDTO criarEstabelecimento(EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO) {
            Estabelecimento estabelecimento = this.estabelecimentoRepository.save(
                Estabelecimento.builder().codigoAcesso(estabelecimentoPostPutRequestDTO.getCodigoAcesso()).build()
            );
            EstabelecimentoResponseDTO est = modelMapper.map(estabelecimento, EstabelecimentoResponseDTO.class);
            return est;
    }

    public EstabelecimentoResponseDTO atualizarEstabelecimento(Long id, EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO) {
        Estabelecimento estabelecimento = this.estabelecimentoRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Estabelecimento não encontrado!")
        );

        estabelecimento.setCodigoAcesso(estabelecimentoPostPutRequestDTO.getCodigoAcesso());

        estabelecimentoRepository.flush();

        return modelMapper.map(estabelecimento, EstabelecimentoResponseDTO.class);
    }

    public void deletarEstabelecimento(Long id) {
        Estabelecimento estabelecimento = this.estabelecimentoRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Estabelecimento não encontrado!")
        );

        this.estabelecimentoRepository.deleteById(estabelecimento.getId());
    }

    public Boolean existeEstabelecimento(Long id) {
        return this.estabelecimentoRepository.existsById(id);
    }

}
