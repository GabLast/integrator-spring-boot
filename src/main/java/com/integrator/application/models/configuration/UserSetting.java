package com.integrator.application.models.configuration;

import com.integrator.application.models.Base;
import com.integrator.application.models.security.User;
import com.integrator.application.utils.GlobalConstants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.TimeZone;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class UserSetting extends Base {

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private User user;
    @Column(nullable = false)
    private String timeZoneString = "America/Santo_Domingo";
    @Column(nullable = false)
    private boolean darkMode = false;
    @Column(nullable = false)
    private String dateFormat = "dd/MM/yy";
    @Column(nullable = false)
    private String dateTimeFormat = "dd/MM/yyyy hh:mm a";
    @Column(nullable = false)
    private String language = "en";

    public TimeZone getTimezone() {
        TimeZone timeZone = TimeZone.getTimeZone(this.timeZoneString);
        if (timeZone == null) {
            timeZone = TimeZone.getTimeZone(GlobalConstants.DEFAULT_TIMEZONE);
        }

        return timeZone;
    }
}
