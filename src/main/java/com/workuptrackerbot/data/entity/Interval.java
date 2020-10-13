package com.workuptrackerbot.data.entity;

import javax.persistence.*;
import java.sql.Date;

//@Entity
//@Table(name = "intervals")
public class Interval {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "project_id")
        private User user;

        @Column(name = "startdate")
        private Date startDate;

        @Column(name = "stopdate")
        private Date stopDate;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "project_id")
        private Project project;



}
