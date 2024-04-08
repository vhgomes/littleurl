package com.vhomes.littleurl.controller;

import com.vhomes.littleurl.DTOS.CreateShortUrlDTO;
import com.vhomes.littleurl.models.URL;
import com.vhomes.littleurl.repository.UrlRepository;
import com.vhomes.littleurl.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;
import java.util.UUID;

@RestController
public class UrlController {
    private final UserRepository userRepository;
    private final UrlRepository urlRepository;

    public UrlController(UserRepository userRepository, UrlRepository urlRepository) {
        this.userRepository = userRepository;
        this.urlRepository = urlRepository;
    }

    @GetMapping("/urls")
    public ResponseEntity<Optional<URL>> getAllUrls(JwtAuthenticationToken jwtAuthenticationToken) {
        var user = userRepository.findById(UUID.fromString(jwtAuthenticationToken.getName())).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var urls = urlRepository.findById(user.getUserId());
        return ResponseEntity.ok(urls);
    }

    @PostMapping("/urls")
    public ResponseEntity<URL> createShortUrl(JwtAuthenticationToken jwtAuthenticationToken, @RequestBody CreateShortUrlDTO shortUrlDTO) {
        var user = userRepository.findById(UUID.fromString(jwtAuthenticationToken.getName())).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var url = new URL();
        url.setOriginalUrl(shortUrlDTO.originalUrl());
        url.setCreatedBy(user);
        url.setClicksOnLink(0);
        URL saved = urlRepository.save(url);

        var id = saved.generateRandomShortUUID(saved.getUrl_id());
        url.setShortUrl(id);

        urlRepository.save(url);

        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public RedirectView redirectTo(@PathVariable("id") String urlId) {
        URL urlToRedirect = urlRepository.findByShortUrl(urlId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        urlToRedirect.upVisitedClicks();
        urlRepository.save(urlToRedirect);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(urlToRedirect.getOriginalUrl());
        return redirectView;
    }
}




