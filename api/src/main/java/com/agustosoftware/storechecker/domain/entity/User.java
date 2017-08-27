package com.agustosoftware.storechecker.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import javax.ws.rs.core.Link;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;

    private String lastName;

    // NOTE: Password encryption is still needed.
    @JsonIgnore
    private String password;

    private String role;

    @JsonProperty(value = "links")
    @Transient
    private List<Link> links;

    public List<Link> getLinks() {
        if (links == null) {
            links = new ArrayList<Link>();
        }
        return links;
    }

}