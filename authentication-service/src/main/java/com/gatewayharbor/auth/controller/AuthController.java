
package com.gatewayharbor.auth.controller;

import com.gatewayharbor.auth.model.User;
import com.gatewayharbor.auth.repository.UserRepository;
import com.gatewayharbor.auth.security.JwtUtils;
import com.gatewayharbor.auth.security.services.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication and API key management endpoints")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    PasswordEncoder encoder;
    
    @Autowired
    JwtUtils jwtUtils;
    
    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Authenticates a user with username and password, returns JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful authentication"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
    })
    public ResponseEntity<?> authenticateUser(@RequestBody Map<String, String> loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.get("username"), loginRequest.get("password")));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .toList();
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", jwt);
        response.put("id", userDetails.getId());
        response.put("username", userDetails.getUsername());
        response.put("email", userDetails.getEmail());
        response.put("roles", roles);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Creates a new user account with basic USER role")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Username or email already in use")
    })
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.get("username"))) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error: Username is already taken!");
            return ResponseEntity.badRequest().body(response);
        }
        
        if (userRepository.existsByEmail(signUpRequest.get("email"))) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error: Email is already in use!");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Create new user's account
        User user = new User();
        user.setUsername(signUpRequest.get("username"));
        user.setEmail(signUpRequest.get("email"));
        user.setPassword(encoder.encode(signUpRequest.get("password")));
        
        Set<String> strRoles = new HashSet<>();
        strRoles.add("USER");
        user.setRoles(strRoles);
        
        userRepository.save(user);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully!");
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/generate-api-key")
    @Operation(summary = "Generate API key", description = "Generates a new API key for the specified user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "API key generated successfully"),
        @ApiResponse(responseCode = "400", description = "User not found")
    })
    public ResponseEntity<?> generateApiKey(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        Optional<User> userOptional = userRepository.findByUsername(username);
        
        if (userOptional.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error: User not found!");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Generate a random API key
        String apiKey = UUID.randomUUID().toString();
        User user = userOptional.get();
        user.setApiKey(apiKey);
        userRepository.save(user);
        
        Map<String, String> response = new HashMap<>();
        response.put("apiKey", apiKey);
        response.put("message", "API key generated successfully!");
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/validate-api-key")
    @Operation(summary = "Validate API key", description = "Checks if the provided API key is valid")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns validation result")
    })
    public ResponseEntity<?> validateApiKey(@RequestBody Map<String, String> request) {
        String apiKey = request.get("apiKey");
        
        boolean isValid = userRepository.findByApiKey(apiKey).isPresent();
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", isValid);
        return ResponseEntity.ok(response);
    }
}
