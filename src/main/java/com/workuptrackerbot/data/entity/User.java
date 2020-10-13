package com.workuptrackerbot.data.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private String username;

//    @Column(name = "firstname")
//    private String firstName;

    @Column(name = "registr_date")
    private Date registrDate;

    @OneToMany
    @JoinColumn(name="id")
    private List<Project> projects;


    public User() {
    }

    public User(Integer id, String username, Date registrDate) {
        this.id = id;
        this.username = username;
        this.registrDate = registrDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getRegistrDate() {
        return registrDate;
    }

    public void setRegistrDate(Date registrDate) {
        this.registrDate = registrDate;
    }
}
