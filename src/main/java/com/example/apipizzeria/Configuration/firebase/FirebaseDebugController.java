package com.example.apipizzeria.Configuration.firebase;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/debug/firebase")
public class FirebaseDebugController {
    private final FirebaseTokenVerifier verifier;
    public FirebaseDebugController(FirebaseTokenVerifier verifier) { this.verifier = verifier; }

    @PostMapping("/verify")
    public ResponseEntity<FirebaseUserClaims> verify(@RequestBody String idToken) {
        var claims = verifier.verify(idToken.replace("\"","").trim());
        return ResponseEntity.ok(claims);
    }
}