package com.example.apipizzeria.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private OffsetDateTime timestamp;
    private int status;          // HTTP status code (200, 201, 400, …)
    private boolean success;     // true en éxitos
    private String message;      // mensaje humano (opcional)
    private T data;              // payload
    private Object meta;         // extra: paginación, etc. (opcional)
    private String path;         // request path (opcional)

    public ApiResponse() {}

    private ApiResponse(int status, boolean success, String message, T data, Object meta, String path) {
        this.timestamp = OffsetDateTime.now();
        this.status = status;
        this.success = success;
        this.message = message;
        this.data = data;
        this.meta = meta;
        this.path = path;
    }

    // --------- Fábricas de éxito ----------
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(200, true, null, data, null, null);
    }

    public static <T> ApiResponse<T> ok(T data, String message) {
        return new ApiResponse<>(200, true, message, data, null, null);
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(201, true, null, data, null, null);
    }

    public static <T> ApiResponse<T> accepted(T data) {
        return new ApiResponse<>(202, true, null, data, null, null);
    }

    /** 204: por conveniencia, data = null */
    public static <T> ApiResponse<T> noContent(String message) {
        return new ApiResponse<>(204, true, message, null, null, null);
    }

    /** Éxito con meta (ej. paginación) */
    public static <T> ApiResponse<T> withMeta(int httpStatus, T data, Object meta, String message) {
        return new ApiResponse<>(httpStatus, true, message, data, meta, null);
    }

    // --------- Fábrica para error (la usa el @ControllerAdvice) ----------
    public static <T> ApiResponse<T> error(int httpStatus, String message, Object details, String path) {
        return new ApiResponse<>(httpStatus, false, message, null, details, path);
    }

    // getters & setters
    public OffsetDateTime getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public Object getMeta() { return meta; }
    public String getPath() { return path; }

    public void setPath(String path) { this.path = path; }
    public void setMeta(Object meta) { this.meta = meta; }
    public void setMessage(String message) { this.message = message; }
}