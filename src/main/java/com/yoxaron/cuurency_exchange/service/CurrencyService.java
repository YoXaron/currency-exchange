package com.yoxaron.cuurency_exchange.service;

import com.yoxaron.cuurency_exchange.dto.CurrencyDto;
import com.yoxaron.cuurency_exchange.model.Currency;
import com.yoxaron.cuurency_exchange.repository.CurrencyRepository;
import com.yoxaron.cuurency_exchange.utils.ModelMapper;
import com.yoxaron.cuurency_exchange.utils.TransactionManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyService {

    private static final CurrencyService INSTANCE = new CurrencyService();
    private final TransactionManager transactionManager = new TransactionManager();
    private final CurrencyRepository currencyRepository = CurrencyRepository.getInstance();

    public List<CurrencyDto> findAll() {
        return transactionManager.doWithoutTransaction(connection ->
            currencyRepository.findAll(connection).stream()
                    .map(ModelMapper::toCurrencyDto)
                    .toList());
    }

    public Optional<CurrencyDto> findByCode(String code) {
        return transactionManager.doWithoutTransaction(connection ->
                currencyRepository.findByCode(connection, code)
                        .map(ModelMapper::toCurrencyDto));
    }

    public CurrencyDto save(Currency currency) {
        Currency savedCurrency = transactionManager.doInTransaction(connection ->
                currencyRepository.save(connection, currency));
        return ModelMapper.toCurrencyDto(savedCurrency);
    }

    public static CurrencyService getInstance() {
        return INSTANCE;
    }
}
