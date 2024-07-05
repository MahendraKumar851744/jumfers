package com.jumfers.mocktestseries.listeners;

public interface DatabaseOperations<T> {
    void insert(T data);
}
