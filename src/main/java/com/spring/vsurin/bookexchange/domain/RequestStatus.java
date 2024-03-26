package com.spring.vsurin.bookexchange.domain;

/**
 * Статус запроса.
 */
public enum RequestStatus {
    /**
     * Запрос актуален и ожидает рассмотрения.
     */
    ACTUAL,

    /**
     * Запрос был отклонён.
     */
    REJECTED,

    /**
     * Запрос был принят.
     */
    ACCEPTED;
}
