package com.springboot.proyectospring.controller;

import com.springboot.proyectospring.model.Pago;
import com.springboot.proyectospring.repository.PagoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Controller
public class PagoController {

    private final PagoRepository pagoRepository;

    public PagoController(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    @GetMapping("/comprobante/{id}")
    public String comprobante(@PathVariable Long id, Model model) {
        Pago pago = pagoRepository.findConDetalles(id)
                .orElseThrow(() -> new IllegalArgumentException("Comprobante no encontrado: " + id));
        model.addAttribute("pago", pago);

        if ("factura".equalsIgnoreCase(pago.getTipoComprobante()) && pago.getMonto() != null) {
            BigDecimal base = pago.getMonto().divide(BigDecimal.valueOf(1.18), 2, RoundingMode.HALF_UP);
            BigDecimal igv  = pago.getMonto().subtract(base);
            model.addAttribute("baseImponible", base);
            model.addAttribute("igv", igv);
        }

        return "public/comprobante";
    }
}
