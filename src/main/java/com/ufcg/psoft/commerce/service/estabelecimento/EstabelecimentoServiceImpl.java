package com.ufcg.psoft.commerce.service.estabelecimento;

import com.ufcg.psoft.commerce.dto.estabelecimento.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.estabelecimento.EstabelecimentoResponseDTO;
import com.ufcg.psoft.commerce.exception.estabelecimento.CodigoAcessoInvalido;
import com.ufcg.psoft.commerce.model.Estabelecimento;
import com.ufcg.psoft.commerce.repository.EstabelecimentoRepository;
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

    public EstabelecimentoResponseDTO recuperarEstabelecimento(String codigoAcesso, Long id) {
        Estabelecimento estabelecimento = this.getEstabelecimento(codigoAcesso, id);

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

    public EstabelecimentoResponseDTO atualizarEstabelecimento(String codigoAcesso, Long id, EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO) {
        Estabelecimento estabelecimento = this.getEstabelecimento(codigoAcesso, id);

        estabelecimento.setCodigoAcesso(estabelecimentoPostPutRequestDTO.getCodigoAcesso());

        estabelecimentoRepository.flush();

        return modelMapper.map(estabelecimento, EstabelecimentoResponseDTO.class);
    }

    public void deletarEstabelecimento(String codigoAcesso, Long id) {
        Estabelecimento estabelecimento = this.getEstabelecimento(codigoAcesso, id);

        this.estabelecimentoRepository.deleteById(estabelecimento.getId());
    }

    public Estabelecimento getEstabelecimento(String codigoAcesso, Long id) {
         Estabelecimento estabelecimento = this.estabelecimentoRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Estabelecimento não encontrado!")
        );

        if (estabelecimento.getCodigoAcesso() == codigoAcesso) {
            throw new CodigoAcessoInvalido("Codigo de acesso Invalido");
        }

        return estabelecimento;
    }

}
