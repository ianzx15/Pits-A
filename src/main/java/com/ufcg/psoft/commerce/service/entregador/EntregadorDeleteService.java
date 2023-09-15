package com.ufcg.psoft.commerce.service.entregador;

import com.ufcg.psoft.commerce.repository.EntregadorRepository;
import com.ufcg.psoft.commerce.service.entregador.interfaces.EntregadorDeleteServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntregadorDeleteService implements EntregadorDeleteServiceInterface {

    @Autowired
    private EntregadorRepository entregadorRepository;

    @Override
    public void delete(Long id) {
        this.entregadorRepository.deleteById(id);
    }
}
