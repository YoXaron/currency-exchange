package com.yoxaron.cuurency_exchange.service;

import com.yoxaron.cuurency_exchange.dto.ExchangeRateDto;
import com.yoxaron.cuurency_exchange.dto.ExchangeRateRequestDto;
import com.yoxaron.cuurency_exchange.exception.InternalServerError;
import com.yoxaron.cuurency_exchange.exception.NotFoundException;
import com.yoxaron.cuurency_exchange.model.ExchangeRate;
import com.yoxaron.cuurency_exchange.repository.CurrencyRepository;
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
    private final CurrencyRepository currencyRepository = CurrencyRepository.getInstance();

    public List<ExchangeRateDto> findAll() {
        return transactionManager.doWithoutTransaction(connection ->
                exchangeRateRepository.findAll(connection).stream()
                        .map(ModelMapper::toExchangeRateDto)
                        .toList());
    }

    public Optional<ExchangeRateDto> findByPairCode(ExchangeRateRequestDto requestDto) {
        return transactionManager.doWithoutTransaction(connection ->
                exchangeRateRepository.findByPair(connection,
                                requestDto.getBaseCurrencyCode(), requestDto.getTargetCurrencyCode())
                        .map(ModelMapper::toExchangeRateDto));
    }

    public ExchangeRateDto save(ExchangeRateRequestDto requestDto) {
        return transactionManager.doInTransaction(connection -> {
            var baseOptional = currencyRepository.findByCode(connection, requestDto.getBaseCurrencyCode());
            var targetOptional = currencyRepository.findByCode(connection, requestDto.getTargetCurrencyCode());
            if (baseOptional.isEmpty() || targetOptional.isEmpty()) {
                throw new NotFoundException();
            }
            var exchangeRate = new ExchangeRate(null, baseOptional.get(), targetOptional.get(), requestDto.getRate());
            var savedExchangeRate = exchangeRateRepository.save(connection, exchangeRate);
            return ModelMapper.toExchangeRateDto(savedExchangeRate);
        });
    }

    public ExchangeRateDto update(ExchangeRateRequestDto requestDto) {
        return transactionManager.doInTransaction(connection -> {
            var exchangeRateOptional = exchangeRateRepository.findByPair(connection,
                    requestDto.getBaseCurrencyCode(), requestDto.getTargetCurrencyCode());

            if (exchangeRateOptional.isEmpty()) {
                throw new NotFoundException();
            }
            var exchangeRate = exchangeRateOptional.get();
            exchangeRate.setRate(requestDto.getRate());
            if (!exchangeRateRepository.update(connection, exchangeRate)) {
                throw new InternalServerError();
            }
            return ModelMapper.toExchangeRateDto(exchangeRate);
        });
    }

    public static ExchangeRateService getInstance() {
        return INSTANCE;
    }
}
