package com.yoxaron.cuurency_exchange.service;

import com.yoxaron.cuurency_exchange.dto.ExchangeDto;
import com.yoxaron.cuurency_exchange.dto.ExchangeRequestDto;
import com.yoxaron.cuurency_exchange.model.ExchangeRate;
import com.yoxaron.cuurency_exchange.repository.ExchangeRateRepository;
import com.yoxaron.cuurency_exchange.utils.TransactionManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static com.yoxaron.cuurency_exchange.utils.ModelMapper.roundToSignificantFigures;
import static com.yoxaron.cuurency_exchange.utils.ModelMapper.toCurrencyDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeService {

    private static final ExchangeService INSTANCE = new ExchangeService();
    private final TransactionManager transactionManager = new TransactionManager();
    private final ExchangeRateRepository exchangeRateRepository = ExchangeRateRepository.getInstance();

    public Optional<ExchangeDto> exchange(ExchangeRequestDto requestDto) {
        var baseCode = requestDto.getFrom();
        var targetCode = requestDto.getTo();
        var amount = requestDto.getAmount();

        return transactionManager.doWithoutTransaction(connection -> {
            var erOptional = exchangeRateRepository.findByPair(connection, baseCode, targetCode);
            if (erOptional.isPresent()) {
                return Optional.of(directExchange(erOptional.get(), amount));
            }

            erOptional = exchangeRateRepository.findByPair(connection, targetCode, baseCode);
            if (erOptional.isPresent()) {
                return Optional.of(reverseExchange(erOptional.get(), amount));
            }

            var erUsdBaseOptional = exchangeRateRepository.findByPair(connection, "USD", baseCode);
            var erUsdTargetOptional = exchangeRateRepository.findByPair(connection, "USD", targetCode);
            if (erUsdBaseOptional.isPresent() && erUsdTargetOptional.isPresent()) {
                return Optional.of(crossExchange(erUsdBaseOptional.get(), erUsdTargetOptional.get(), amount));
            }

            return Optional.empty();
        });
    }

    private ExchangeDto directExchange(ExchangeRate exchangeRate, BigDecimal amount) {
        var rate = exchangeRate.getRate();
        var convertedAmount = amount.multiply(rate);
        return new ExchangeDto(
                toCurrencyDto(exchangeRate.getBaseCurrency()),
                toCurrencyDto(exchangeRate.getTargetCurrency()),
                roundToSignificantFigures(rate),
                roundToSignificantFigures(amount),
                roundToSignificantFigures(convertedAmount)
        );
    }

    private ExchangeDto reverseExchange(ExchangeRate exchangeRate, BigDecimal amount) {
        var rate = BigDecimal.ONE.divide(exchangeRate.getRate(), 4, RoundingMode.HALF_UP);
        var convertedAmount = amount.multiply(rate);
        return new ExchangeDto(
                toCurrencyDto(exchangeRate.getTargetCurrency()),
                toCurrencyDto(exchangeRate.getBaseCurrency()),
                roundToSignificantFigures(rate),
                roundToSignificantFigures(amount),
                roundToSignificantFigures(convertedAmount)
        );
    }

    private ExchangeDto crossExchange(ExchangeRate baseUsd, ExchangeRate targetUsd, BigDecimal amount) {
        var rate = targetUsd.getRate().divide(baseUsd.getRate(), 4, RoundingMode.HALF_UP);
        var convertedAmount = amount.multiply(rate);
        return new ExchangeDto(
                toCurrencyDto(baseUsd.getTargetCurrency()),
                toCurrencyDto(targetUsd.getTargetCurrency()),
                roundToSignificantFigures(rate),
                roundToSignificantFigures(amount),
                roundToSignificantFigures(convertedAmount)
        );
    }

    public static ExchangeService getInstance() {
        return INSTANCE;
    }
}
