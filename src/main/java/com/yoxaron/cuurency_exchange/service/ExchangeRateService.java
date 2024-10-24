package com.yoxaron.cuurency_exchange.service;

import com.yoxaron.cuurency_exchange.dto.ExchangeRateDto;
import com.yoxaron.cuurency_exchange.repository.ExchangeRateRepository;
import com.yoxaron.cuurency_exchange.utils.ModelMapper;
import com.yoxaron.cuurency_exchange.utils.TransactionManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeRateService {

    private static final ExchangeRateService INSTANCE = new ExchangeRateService();
    private final TransactionManager transactionManager = new TransactionManager();
    private final ExchangeRateRepository exchangeRateRepository = ExchangeRateRepository.getInstance();

    public List<ExchangeRateDto> findAll() {
        return transactionManager.doWithoutTransaction(connection ->
                exchangeRateRepository.findAll(connection).stream()
                        .map(ModelMapper::toExchangeRateDto)
                        .toList());
    }

    public Optional<ExchangeRateDto> findByPairCode(String[] codes) {
        return transactionManager.doWithoutTransaction(connection ->
                exchangeRateRepository.findByPair(connection, codes)
                        .map(ModelMapper::toExchangeRateDto));
    }

    public static ExchangeRateService getInstance() {
        return INSTANCE;
    }
}
