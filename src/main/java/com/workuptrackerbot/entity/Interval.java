package com.workuptrackerbot.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "intervals")
public class Interval {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        @OnDelete(action = OnDeleteAction.CASCADE)
        @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
        @JoinColumn(name = "up_id")
        private UserProject userProject;

        @Column(name = "startdate")
        private Timestamp startDate;

        @Column(name = "stopdate")
        private Timestamp stopDate;

        public Interval() {
        }

        public Interval(UserProject userProject, Timestamp startDate) {
                this.userProject = userProject;
                this.startDate = startDate;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public UserProject getUserProject() {
                return userProject;
        }

        public void setUserProject(UserProject userProject) {
                this.userProject = userProject;
        }

        public Timestamp getStartDate() {
                return startDate;
        }

        public void setStartDate(Timestamp startDate) {
                this.startDate = startDate;
        }

        public Timestamp getStopDate() {
                return stopDate;
        }

        public void setStopDate(Timestamp stopDate) {
                this.stopDate = stopDate;
        }
}
