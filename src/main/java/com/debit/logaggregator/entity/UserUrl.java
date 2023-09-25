package com.debit.logaggregator.entity;

import com.debit.logaggregator.dto.UserUrlDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Bogdan Lesin
 */
@Entity(name = "user_url")
public class UserUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String url;

    @Column
    private String comment;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(nullable = false, name = "nextRequestTime")
    private Date nextRequestTime;

    @Column(nullable = false, name = "period_in_minutes")
    private Integer periodInMinutes;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties("userUrls")
    private User user;

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(final Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getNextRequestTime() {
        return nextRequestTime;
    }

    public void setNextRequestTime(final Date nextRequestTime) {
        this.nextRequestTime = nextRequestTime;
    }

    public Integer getPeriodInMinutes() {
        return periodInMinutes;
    }

    public void setPeriodInMinutes(final Integer periodInMinutes) {
        this.periodInMinutes = periodInMinutes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public UserUrl updateWithoutId(UserUrl newData) {
        this.setUrl(newData.getUrl());
        this.setComment(newData.getComment());
        this.setCreatedAt(newData.getCreatedAt());
        this.setNextRequestTime(newData.getNextRequestTime());
        this.setPeriodInMinutes(newData.getPeriodInMinutes());
        this.setUser(newData.getUser());
        return this;
    }

    public UserUrl updateWithoutId(UserUrlDTO newData) {
        this.setUrl(newData.url());
        this.setComment(newData.comment());
        this.setNextRequestTime(newData.nextRequestTime());
        this.setPeriodInMinutes(newData.periodInMinutes());
        return this;
    }
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserUrl userUrl = (UserUrl) o;
        return Objects.equals(id, userUrl.id)
                && Objects.equals(url, userUrl.url)
                && Objects.equals(comment, userUrl.comment)
                && Objects.equals(createdAt, userUrl.createdAt)
                && Objects.equals(nextRequestTime, userUrl.nextRequestTime)
                && Objects.equals(periodInMinutes, userUrl.periodInMinutes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, comment, createdAt, nextRequestTime, periodInMinutes, user);
    }

    @Override
    public String toString() {
        return "UserUrl{"
                + "id=" + id
                + ", url='" + url + '\''
                + ", comment='" + comment + '\''
                + ", created_at=" + createdAt
                + ", next_request_time=" + nextRequestTime
                + ", period_in_minutes=" + periodInMinutes
                + ", user=" + user
                + '}';
    }
}
