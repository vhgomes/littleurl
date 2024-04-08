package com.vhomes.littleurl.repository;

import com.vhomes.littleurl.models.URL;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UrlRepository extends JpaRepository<URL, UUID> {
    Optional<URL> findByShortUrl(String shortId);
}
