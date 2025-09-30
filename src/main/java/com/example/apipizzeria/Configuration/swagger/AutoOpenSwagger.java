package com.example.apipizzeria.Configuration.swagger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import java.awt.Desktop;
import java.net.URI;

@Component
@Profile({"dev","local"}) // solo en perfiles dev/local
public class AutoOpenSwagger {

    @Value("${server.port:8080}")
    private int port;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Value("${springdoc.swagger-ui.path:/swagger-ui.html}")
    private String swaggerPath;

    @EventListener(ApplicationReadyEvent.class)
    public void openSwagger() {
        try {
            if (!Desktop.isDesktopSupported()) return; // evita fallar en servidores/headless
            String ctx = (contextPath == null) ? "" : contextPath;
            if (!ctx.startsWith("/")) ctx = "/" + ctx;
            if (ctx.equals("/")) ctx = ""; // normaliza
            String path = (swaggerPath.startsWith("/")) ? swaggerPath : "/" + swaggerPath;
            URI uri = new URI("http://localhost:" + port + ctx + path);
            Desktop.getDesktop().browse(uri);
        } catch (Exception ignored) {
            // Intenta no romper el arranque si no puede abrir el navegador
        }
    }
}