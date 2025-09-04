package com.integrator.application.models.configuration;

import com.integrator.application.models.security.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Document {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @Column(columnDefinition = "longblob")
    private byte[] file;
    @Column(columnDefinition = "longblob")
    private String fileUrl;
    private String contentType;
    private String name;
}
