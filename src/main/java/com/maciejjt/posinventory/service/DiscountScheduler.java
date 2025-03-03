package com.maciejjt.posinventory.service;

import com.maciejjt.posinventory.model.Sale;
import com.maciejjt.posinventory.model.enums.SaleStatus;
import com.maciejjt.posinventory.repository.DiscountRepository;
import com.maciejjt.posinventory.repository.SaleRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Data
public class DiscountScheduler {

    private final DiscountRepository discountRepository;
    private final SaleRepository saleRepository;
    private final DiscountService discountService;

    @Scheduled(cron = "0 0 * * * ?")
    @Transactional
    public void updateSales() {

        List<Sale> salesToStart = saleRepository.findSaleByStatusAndStartDateIsAfter(SaleStatus.UPCOMING, LocalDateTime.now());

        salesToStart.forEach(
                sale -> {
                    sale.setStatus(SaleStatus.IN_PROGRESS);
                    saleRepository.save(sale);
                }
        );

        List<Sale> salesToEnd = saleRepository.findSaleByStatusAndEndDateIsAfter(SaleStatus.IN_PROGRESS, LocalDateTime.now());

        salesToStart.forEach(
                sale -> {
                    discountService.endSale(sale.getId());
                }
        );


    }


}
