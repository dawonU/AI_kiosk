package com.e1i4.kiosk.service;

import com.e1i4.kiosk.entity.Sales;
import com.e1i4.kiosk.repository.SalesRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class SalesService {
    private final SalesRepository salesRepository;

    public SalesService(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    public Sales saveSale(Sales sale) {
        sale.setSalesAt(LocalDateTime.now());
        return salesRepository.save(sale);
    }
}