package com.example.apipizzeria.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ====== EXCEPCIONES PROPIAS ======
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handleApi(ApiException ex, HttpServletRequest req) {
        HttpStatus st = ex.getStatus();
        return ResponseEntity.status(st).body(
                ApiError.of(st.value(), st.getReasonPhrase(), ex.getCode(), ex.getMessage(), req.getRequestURI(), null)
        );
    }

    // ====== VALIDACIONES ======
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleBeanValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> fields = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fe -> fields.put(fe.getField(), fe.getDefaultMessage()));
        var st = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(st).body(
                ApiError.of(st.value(), st.getReasonPhrase(), "VALIDATION_ERROR",
                        "Datos inválidos", req.getRequestURI(), fields)
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraint(ConstraintViolationException ex, HttpServletRequest req) {
        Map<String, String> fields = new HashMap<>();
        ex.getConstraintViolations().forEach(v -> fields.put(v.getPropertyPath().toString(), v.getMessage()));
        var st = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(st).body(
                ApiError.of(st.value(), st.getReasonPhrase(), "CONSTRAINT_VIOLATION",
                        "Datos inválidos", req.getRequestURI(), fields)
        );
    }

    // ====== REQUEST MAL FORMADO ======
    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class
    })
    public ResponseEntity<ApiError> handleBadRequest(Exception ex, HttpServletRequest req) {
        var st = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(st).body(
                ApiError.of(st.value(), st.getReasonPhrase(), "BAD_REQUEST",
                        ex.getMessage(), req.getRequestURI(), null)
        );
    }

    // ====== MÉTODO / RUTA ======
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        var st = HttpStatus.METHOD_NOT_ALLOWED;
        return ResponseEntity.status(st).body(
                ApiError.of(st.value(), st.getReasonPhrase(), "METHOD_NOT_ALLOWED",
                        ex.getMessage(), req.getRequestURI(), null)
        );
    }

    // (Opcional) si habilitas NoHandlerFoundException en properties
    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
    public ResponseEntity<ApiError> handleNoHandler(org.springframework.web.servlet.NoHandlerFoundException ex, HttpServletRequest req) {
        var st = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(st).body(
                ApiError.of(st.value(), st.getReasonPhrase(), "NO_HANDLER",
                        "Recurso no encontrado", req.getRequestURI(), null)
        );
    }

    // ====== JPA/DB ======
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        var st = HttpStatus.CONFLICT;
        return ResponseEntity.status(st).body(
                ApiError.of(st.value(), st.getReasonPhrase(), "DATA_INTEGRITY",
                        "Conflicto con datos persistidos", req.getRequestURI(), null)
        );
    }

    // ====== SECURITY ======
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuth(AuthenticationException ex, HttpServletRequest req) {
        var st = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(st).body(
                ApiError.of(st.value(), st.getReasonPhrase(), "UNAUTHORIZED",
                        ex.getMessage(), req.getRequestURI(), null)
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleForbidden(AccessDeniedException ex, HttpServletRequest req) {
        var st = HttpStatus.FORBIDDEN;
        return ResponseEntity.status(st).body(
                ApiError.of(st.value(), st.getReasonPhrase(), "FORBIDDEN",
                        "No tienes permisos para acceder a este recurso", req.getRequestURI(), null)
        );
    }

    // ====== CATCH-ALL ======
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleOther(Exception ex, HttpServletRequest req) {
        var st = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(st).body(
                ApiError.of(st.value(), st.getReasonPhrase(), "UNEXPECTED_ERROR",
                        (ex.getMessage() != null ? ex.getMessage() : "Error inesperado"),
                        req.getRequestURI(), null)
        );
    }
}