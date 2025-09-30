package com.example.apipizzeria.Configuration.swagger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SwaggerUrlLogger {

    private static final Logger log = LoggerFactory.getLogger(SwaggerUrlLogger.class);

    @Value("${server.port:8080}")
    private int port;

    @Value("${server.address:localhost}")
    private String address;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    // Para springdoc 1.x y 2.x vale /swagger-ui.html (redirige a /swagger-ui/index.html)
    @Value("${springdoc.swagger-ui.path:/swagger-ui.html}")
    private String swaggerPath;

    @EventListener(ApplicationReadyEvent.class)
    public void logSwaggerUrl() {
        String host = (address == null || address.isBlank() || "0.0.0.0".equals(address)) ? "localhost" : address;
        String ctx = (contextPath == null || contextPath.isBlank() || "/".equals(contextPath)) ? "" :
                (contextPath.startsWith("/") ? contextPath : "/" + contextPath);
        String path = swaggerPath.startsWith("/") ? swaggerPath : "/" + swaggerPath;

        String url = "http://" + host + ":" + port + ctx + path;

        // En log (clickeable en la mayoría de IDEs)
        log.info("🔗 Swagger UI: {}", url);

        // También directo a STDOUT por si el logger filtra INFO
        System.out.println("🔗 Swagger UI: " + url);
    }
}