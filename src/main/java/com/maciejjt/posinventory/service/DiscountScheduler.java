package com.maciejjt.posinventory.service;

import com.maciejjt.posinventory.model.Discount;
import com.maciejjt.posinventory.model.Sale;
import com.maciejjt.posinventory.model.enums.SaleStatus;
import com.maciejjt.posinventory.repository.DiscountRepository;
import com.maciejjt.posinventory.repository.ProductRepository;
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
    private final ProductRepository productRepository;

    @Scheduled(cron = "0 0 * * * ?")
    @Transactional
    public void updateSales() {
        List<Sale> salesToStart = saleRepository.findSaleByStatusAndStartDateIsAfter(SaleStatus.UPCOMING, LocalDateTime.now());

        salesToStart.forEach(
                sale -> {
                    sale.setStatus(SaleStatus.IN_PROGRESS);
                    if (sale.getIsAggregating()) {
                        sale.getDiscounts().forEach(discount -> {
                            discount.setActive(true);
                            discountRepository.save(discount);
                        });
                    }
                    saleRepository.save(sale);
                }
        );

        List<Sale> salesToEnd = saleRepository.findSaleByStatusAndEndDateIsAfter(SaleStatus.IN_PROGRESS, LocalDateTime.now());

        salesToEnd.forEach(
                sale -> {
                    discountService.endSale(sale.getId());
                }
        );
    }

    @Scheduled(cron = "0 0 * * * ?")
    @Transactional
    public void updateDiscounts() {
        List<Discount> discountsToStart = discountRepository.findDiscountByActiveAndStartDateIsAfter(false, LocalDateTime.now());

        discountsToStart.forEach(
                discount -> {
                    discount.setActive(true);
                    discountRepository.save(discount);
                }
        );

        List<Discount> discountsToEnd = discountRepository.findDiscountByActiveAndEndDateIsAfter(true, LocalDateTime.now());

        discountsToEnd.forEach(discountService::endDiscount);
    }





}
