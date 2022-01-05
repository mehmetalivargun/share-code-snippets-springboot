package com.mehmetalivargun.snippets.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({"id", "time"})
public class Code {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
    private static final LocalDateTime NO_EXPIRY =
            LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0);

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter
                            (name = "uuid_ge_strategy_class",
                                    value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                            )
            }
    )
    @Column(name = "id", nullable = false)
    private UUID id;
    private String code;
    private LocalDateTime date = LocalDateTime.now();
    private long time = 0L;
    private long views = 0L;
    private long viewLimit = 0L;
    private long timeLimit = 0L;
    private LocalDateTime expiryDate = NO_EXPIRY;

    public String getDate() {
        return date.format(FORMATTER);
    }



    public void increaseViewCount() {
        views++;
    }
    public void setTime(long time) {
        if (0 < time) {
            this.time = time;
        }
    }

    public void setTimeLimit(long timeLimit) {
        if (0 < timeLimit) {
            this.timeLimit = timeLimit;
            this.expiryDate = date.plusMinutes(timeLimit);
        }
    }
    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isAccessible() {
        return (viewLimit == 0 || views < viewLimit)
                && (timeLimit == 0 || LocalDateTime.now().isBefore(date));
    }

    @JsonGetter("remainingSeconds")
    public long getRemainingSeconds(){
        if (0 < timeLimit) {
            return 0;
        }
        return date.isBefore(expiryDate) ? Duration.between(date, expiryDate).getSeconds() : 0;
    }
    //@JsonIgnore
    public boolean isRestrictedByViews() {
        return 0 < viewLimit;
    }

    //@JsonIgnore
    public boolean isRestrictedByTime() {
        return 0 < timeLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Code that = (Code) o;

        return id.equals(that.id);
    }
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
