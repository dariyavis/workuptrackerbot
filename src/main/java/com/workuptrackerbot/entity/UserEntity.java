package com.workuptrackerbot.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    private Integer id;

    @Column
    private String username;

//    @Column(name = "firstname")
//    private String firstName;

    @Column
    private Long chat_id;

    @Column(name = "registr_date")
    private Date registrDate;

//    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "up",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "project_id")
//    )
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "up",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private List<Project> projects = new ArrayList<>();


    public UserEntity() {
    }

    public UserEntity(Integer id, String username, Long chat_id, Date registrDate) {
        this.id = id;
        this.username = username;
        this.chat_id = chat_id;
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

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public Long getChat_id() {
        return chat_id;
    }

    public void setChat_id(Long chat_id) {
        this.chat_id = chat_id;
    }
}

/*
*
drop table command;
drop table intervals;
drop table up;
drop table projects;
drop table users;


*
*
*
* */