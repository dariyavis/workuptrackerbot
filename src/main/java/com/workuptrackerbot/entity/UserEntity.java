package com.workuptrackerbot.entity;

import org.checkerframework.common.aliasing.qual.Unique;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Unique
    @Column
    private Integer tlgId;

    @Column
    private String username;

//    @Column(name = "firstname")
//    private String firstName;


    @Column(name = "registr_date")
    private Date registrDate;

//    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "up",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "project_id")
//    )
//    private List<Project> projects = new ArrayList<>();


    public UserEntity() {
    }

    public UserEntity(Integer tlgId, String username, Date registrDate) {
        this.tlgId = tlgId;
        this.username = username;
        this.registrDate = registrDate;
    }

    public Integer getTlgId() {
        return tlgId;
    }

    public void setTlgId(Integer tlg_id) {
        this.tlgId = tlg_id;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

/*
*
drop table action_state;
drop table intervals;
drop table up;
drop table projects;
drop table users;


*
*
*
* */