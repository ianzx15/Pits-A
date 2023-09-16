package com.ufcg.psoft.commerce.service.entregador;

import com.ufcg.psoft.commerce.Util.Util;
import com.ufcg.psoft.commerce.exception.InvalidIdException;
import com.ufcg.psoft.commerce.model.Entregador;
import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import com.ufcg.psoft.commerce.service.entregador.interfaces.EntregadorDeleteServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntregadorDeleteService implements EntregadorDeleteServiceInterface {

    @Autowired
    private EntregadorRepository entregadorRepository;

    @Override
    public void delete(Long id, String codAcesso) {
        Entregador entity = this.entregadorRepository.findById(id).orElseThrow(InvalidIdException::new);
        Util.verificaCodAcesso(codAcesso, entity.getCodigoAcesso());
        this.entregadorRepository.deleteById(id);
    }
}
