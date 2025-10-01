package com.example.apipizzeria.common.exception;


import com.example.apipizzeria.common.api.ApiResponse;
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

    // ========= EXCEPCIONES PROPIAS =========
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Void>> handleApi(ApiException ex, HttpServletRequest req) {
        HttpStatus st = ex.getStatus();
        var body = ApiResponse.<Void>error(
                st.value(),
                ex.getMessage(),
                Map.of("code", ex.getCode()),
                req.getRequestURI()
        );
        return ResponseEntity.status(st).body(body);
    }

    // ========= VALIDACIONES (DTO @Valid) =========
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleBeanValidation(MethodArgumentNotValidException ex,
                                                                  HttpServletRequest req) {
        Map<String, String> fields = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(fe -> fields.put(fe.getField(), fe.getDefaultMessage()));

        HttpStatus st = HttpStatus.BAD_REQUEST; // o 422 si prefieres
        var body = ApiResponse.<Void>error(
                st.value(),
                "Datos inválidos",
                Map.of("fields", fields),
                req.getRequestURI()
        );
        return ResponseEntity.status(st).body(body);
    }

    // ========= VALIDACIONES (@Validated en path/query) =========
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraint(ConstraintViolationException ex,
                                                              HttpServletRequest req) {
        Map<String, String> fields = new HashMap<>();
        ex.getConstraintViolations()
                .forEach(v -> fields.put(v.getPropertyPath().toString(), v.getMessage()));

        HttpStatus st = HttpStatus.BAD_REQUEST; // o 422
        var body = ApiResponse.<Void>error(
                st.value(),
                "Datos inválidos",
                Map.of("fields", fields),
                req.getRequestURI()
        );
        return ResponseEntity.status(st).body(body);
    }

    // ========= REQUEST MAL FORMADO =========
    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(Exception ex, HttpServletRequest req) {
        HttpStatus st = HttpStatus.BAD_REQUEST;
        var body = ApiResponse.<Void>error(
                st.value(),
                "Solicitud mal formada",
                ex.getMessage(),                  // meta: string con detalle
                req.getRequestURI()
        );
        return ResponseEntity.status(st).body(body);
    }

    // ========= MÉTODO / RUTA =========
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex,
                                                                    HttpServletRequest req) {
        HttpStatus st = HttpStatus.METHOD_NOT_ALLOWED;
        var body = ApiResponse.<Void>error(
                st.value(),
                "Método no permitido",
                Map.of(
                        "method", ex.getMethod(),
                        "supported", ex.getSupportedHttpMethods()
                ),
                req.getRequestURI()
        );
        return ResponseEntity.status(st).body(body);
    }

    // (Opcional) requiere en application.properties:
    // spring.mvc.throw-exception-if-no-handler-found=true
    // spring.web.resources.add-mappings=false
    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoHandler(org.springframework.web.servlet.NoHandlerFoundException ex,
                                                             HttpServletRequest req) {
        HttpStatus st = HttpStatus.NOT_FOUND;
        var body = ApiResponse.<Void>error(
                st.value(),
                "Recurso no encontrado",
                Map.of("pathTried", ex.getRequestURL()),
                req.getRequestURI()
        );
        return ResponseEntity.status(st).body(body);
    }

    // ========= JPA/DB =========
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrity(DataIntegrityViolationException ex,
                                                                 HttpServletRequest req) {
        HttpStatus st = HttpStatus.CONFLICT;
        var body = ApiResponse.<Void>error(
                st.value(),
                "Conflicto con datos persistidos",
                // si quieres, puedes meter el rootCause:
                ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage(),
                req.getRequestURI()
        );
        return ResponseEntity.status(st).body(body);
    }

    // ========= SECURITY =========
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuth(AuthenticationException ex, HttpServletRequest req) {
        HttpStatus st = HttpStatus.UNAUTHORIZED;
        var body = ApiResponse.<Void>error(
                st.value(),
                "No autenticado",
                ex.getMessage(),
                req.getRequestURI()
        );
        return ResponseEntity.status(st).body(body);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleForbidden(AccessDeniedException ex, HttpServletRequest req) {
        HttpStatus st = HttpStatus.FORBIDDEN;
        var body = ApiResponse.<Void>error(
                st.value(),
                "No tienes permisos para acceder a este recurso",
                null,
                req.getRequestURI()
        );
        return ResponseEntity.status(st).body(body);
    }

    // ========= CATCH-ALL =========
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleOther(Exception ex, HttpServletRequest req) {
        HttpStatus st = HttpStatus.INTERNAL_SERVER_ERROR;
        var body = ApiResponse.<Void>error(
                st.value(),
                "Error interno",
                ex.getMessage(),
                req.getRequestURI()
        );
        return ResponseEntity.status(st).body(body);
    }
}