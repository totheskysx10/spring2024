package com.spring.vsurin.bookexchange.domain;

/**
 * Статус обменного процесса.
 */
public enum ExchangeStatus {
    /**
     * Сделано предложение для обмена.
     */
    OFFERED,

    /**
     * Обмен подтвержден всеми вовлеченными сторонами.
     */
    CONFIRMED,

    /**
     * Обмен был отклонен одной или обоими вовлеченными сторонами.
     */
    REJECTED,

    /**
     * Обмен в процессе доставки.
     */
    IN_PROGRESS,

    /**
     * Обмен успешно завершен.
     */
    COMPLETED,

    /**
     * Возникли проблемы при обмене.
     */
    PROBLEMS,

    /**
     * Обмен отменён админом.
     */
    CANCELLED_BY_ADMIN
}


