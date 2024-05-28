package com.spring.vsurin.bookexchange.extern.api;

import com.spring.vsurin.bookexchange.app.ExchangeService;
import com.spring.vsurin.bookexchange.domain.Exchange;
import com.spring.vsurin.bookexchange.domain.ExchangeStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Управление обменами", description = "API для управления обменами книгами")
@RestController
@RequestMapping("/exchanges")
public class ExchangeController {

    private final ExchangeService exchangeService;
    private final ExchangeAssembler exchangeAssembler;

    public ExchangeController(ExchangeService exchangeService, ExchangeAssembler exchangeAssembler) {
        this.exchangeService = exchangeService;
        this.exchangeAssembler = exchangeAssembler;
    }

    @Operation(summary = "Возвращает обмен по ID", description = "Возвращает информацию об обмене по его ID")
    @GetMapping("/{exchangeId}")
    public ResponseEntity<ExchangeDTO> getExchangeById(@PathVariable long exchangeId) {
        Exchange exchange = exchangeService.getExchangeById(exchangeId);
        return ResponseEntity.ok(exchangeAssembler.toModel(exchange));
    }

    @Operation(summary = "Устанавливает трек-номер", description = "Устанавливает трек-номер для обмена")
    @PutMapping("/{exchangeId}/set-track")
    public ResponseEntity<Void> setTrackNumber(@PathVariable long exchangeId, @RequestParam long userId, @RequestParam String track) {
        exchangeService.updateTrackSetByUser(userId, exchangeId, track);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Устанавливает отсутствие трек-номера", description = "Устанавливает отсутствие трек-номера обмена")
    @PutMapping("/{exchangeId}/set-no-track")
    public ResponseEntity<Void> setNoTrack(@PathVariable long exchangeId, @RequestParam long userId) {
        exchangeService.setNoTrack(userId, exchangeId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Подтверждает получение книги", description = "Подтверждает получение книги в рамках обмена")
    @PutMapping("/{exchangeId}/receive")
    public ResponseEntity<Void> receiveBook(@PathVariable long exchangeId, @RequestParam long userId) {
        exchangeService.receiveBook(exchangeId, userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Устанавливает статус проблемного обмена", description = "Устанавливает статус обмена как проблемного")
    @PutMapping("/{exchangeId}/set-problems-status")
    public ResponseEntity<Void> setProblemsStatus(@PathVariable long exchangeId) {
        exchangeService.setProblemsStatus(exchangeId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Отменяет обмен", description = "Отменяет обмен по его ID")
    @PutMapping("/cancel/{exchangeId}")
    public ResponseEntity<Void> cancelExchange(@PathVariable long exchangeId) {
        exchangeService.cancelExchange(exchangeId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Ищет обмены по статусу и участнику", description = "Возвращает список обменов по статусу и ID участника")
    @GetMapping("/search")
    public ResponseEntity<List<ExchangeDTO>> searchByStatusAndMember(@RequestParam ExchangeStatus status, @RequestParam long userId) {
        List<ExchangeDTO> exchanges = exchangeService.searchByStatusAndMember(status, userId).stream()
                .map(exchangeAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(exchanges);
    }
}
