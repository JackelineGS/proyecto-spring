package com.springboot.proyectospring.service;

import com.springboot.proyectospring.model.Paciente;
import com.springboot.proyectospring.model.Pago;
import com.springboot.proyectospring.model.Paquete;
import com.springboot.proyectospring.repository.PagoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;

    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    @Transactional
    public Pago registrar(Paciente paciente, Paquete paquete, BigDecimal monto,
                          String tipoComprobante, String metodoPago) {
        Pago pago = new Pago();
        pago.setPaciente(paciente);
        pago.setPaquete(paquete);
        pago.setMonto(monto);
        pago.setTipoComprobante(tipoComprobante != null ? tipoComprobante : "boleta");
        pago.setMetodoPago(metodoPago);
        pago.setFecha(LocalDate.now());
        pago.setNumeroComprobante(generarNumero(tipoComprobante));
        return pagoRepository.save(pago);
    }

    private String generarNumero(String tipo) {
        int year = LocalDate.now().getYear();
        String prefix = "factura".equalsIgnoreCase(tipo) ? "F" : "B";
        long total = pagoRepository.count();
        return prefix + "-" + year + "-" + String.format("%05d", total + 1);
    }
}
