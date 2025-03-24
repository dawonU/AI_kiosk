package com.e1i4.kiosk.controller;

import com.e1i4.kiosk.entity.Sales;
import com.e1i4.kiosk.service.SalesService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sales")
public class SalesController {
    private final SalesService salesService;

    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @PostMapping
    public Sales saveSale(@RequestBody Sales sale) {
        return salesService.saveSale(sale);
    }
}