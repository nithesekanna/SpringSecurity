package com.example.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class HelloController {
	
	@GetMapping("/hello")
	public String getMethodName() {
		return "this is a get method";
	}
	
	@GetMapping("/public")
    public String publicAccess() {
        return "Hello, world (no login needed)!";
    }
	
	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
	    return "Hello Admin!";
	}

	@GetMapping("/user")
	@PreAuthorize("hasRole('USER')")
	public String userAccess() {
	    return "Hello User!";
	}


}
