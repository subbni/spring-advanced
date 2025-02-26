package org.example.expert.domain.common.exception;

public class AdminAccessDeniedException extends RuntimeException {
    public AdminAccessDeniedException(String message) { super(message); }
}
