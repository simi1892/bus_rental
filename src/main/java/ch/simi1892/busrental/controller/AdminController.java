package ch.simi1892.busrental.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String admin() {
        return "admin";
    }
}
