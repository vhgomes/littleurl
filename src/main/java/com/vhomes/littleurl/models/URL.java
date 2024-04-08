package com.vhomes.littleurl.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tb_url")
public class URL {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID url_id;
    private String originalUrl;
    private String shortUrl;
    private Integer clicksOnLink;
    @JoinColumn(name = "user_id")
    @ManyToOne
    private User createdBy;
    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant updatedAt;

    public URL(UUID url_id, String originalUrl, String shortUrl, Integer clicksOnLink, User createdBy, User createdBy1, Instant createdAt, Instant updatedAt) {
        this.url_id = url_id;
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.clicksOnLink = clicksOnLink;
        this.createdBy = createdBy1;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public URL() {

    }

    public UUID getUrl_id() {
        return url_id;
    }

    public void setUrl_id(UUID url_id) {
        this.url_id = url_id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public Integer getClicksOnLink() {
        return clicksOnLink;
    }

    public void setClicksOnLink(Integer clicksOnLink) {
        this.clicksOnLink = clicksOnLink;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public void upVisitedClicks() {
        this.clicksOnLink++;
    }

    public String generateRandomShortUUID(UUID urlId) {
        long mostSignificantBits = urlId.getMostSignificantBits();
        long leastSignificantBits = urlId.getLeastSignificantBits();

        long combinedBits = mostSignificantBits ^ leastSignificantBits;
        StringBuilder shortUUID = new StringBuilder();
        while (combinedBits != 0) {
            shortUUID.append("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt((int) (combinedBits % 62)));
            combinedBits /= 62;
        }

        while (shortUUID.length() < 8) {
            shortUUID.append('0');
        }

        return shortUUID.reverse().toString();
    }
}
