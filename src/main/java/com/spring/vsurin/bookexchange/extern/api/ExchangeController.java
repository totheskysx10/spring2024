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

@Tag(name = "���������� ��������", description = "API ��� ���������� �������� �������")
@RestController
@RequestMapping("/exchanges")
public class ExchangeController {

    private final ExchangeService exchangeService;
    private final ExchangeAssembler exchangeAssembler;

    public ExchangeController(ExchangeService exchangeService, ExchangeAssembler exchangeAssembler) {
        this.exchangeService = exchangeService;
        this.exchangeAssembler = exchangeAssembler;
    }

    @Operation(summary = "���������� ����� �� ID", description = "���������� ���������� �� ������ �� ��� ID")
    @GetMapping("/{exchangeId}")
    public ResponseEntity<ExchangeDTO> getExchangeById(@PathVariable long exchangeId) {
        Exchange exchange = exchangeService.getExchangeById(exchangeId);
        return ResponseEntity.ok(exchangeAssembler.toModel(exchange));
    }

    @Operation(summary = "������������� ����-�����", description = "������������� ����-����� ��� ������")
    @PutMapping("/{exchangeId}/set-track")
    public ResponseEntity<Void> setTrackNumber(@PathVariable long exchangeId, @RequestParam long userId, @RequestParam String track) {
        exchangeService.updateTrackSetByUser(userId, exchangeId, track);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "������������� ���������� ����-������", description = "������������� ���������� ����-������ ������")
    @PutMapping("/{exchangeId}/set-no-track")
    public ResponseEntity<Void> setNoTrack(@PathVariable long exchangeId, @RequestParam long userId) {
        exchangeService.setNoTrack(userId, exchangeId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "������������ ��������� �����", description = "������������ ��������� ����� � ������ ������")
    @PutMapping("/{exchangeId}/receive")
    public ResponseEntity<Void> receiveBook(@PathVariable long exchangeId, @RequestParam long userId) {
        exchangeService.receiveBook(exchangeId, userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "������������� ������ ����������� ������", description = "������������� ������ ������ ��� �����������")
    @PutMapping("/{exchangeId}/set-problems-status")
    public ResponseEntity<Void> setProblemsStatus(@PathVariable long exchangeId) {
        exchangeService.setProblemsStatus(exchangeId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "�������� �����", description = "�������� ����� �� ��� ID")
    @PutMapping("/cancel/{exchangeId}")
    public ResponseEntity<Void> cancelExchange(@PathVariable long exchangeId) {
        exchangeService.cancelExchange(exchangeId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "���� ������ �� ������� � ���������", description = "���������� ������ ������� �� ������� � ID ���������")
    @GetMapping("/search")
    public ResponseEntity<List<ExchangeDTO>> searchByStatusAndMember(@RequestParam ExchangeStatus status, @RequestParam long userId) {
        List<ExchangeDTO> exchanges = exchangeService.searchByStatusAndMember(status, userId).stream()
                .map(exchangeAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(exchanges);
    }
}
