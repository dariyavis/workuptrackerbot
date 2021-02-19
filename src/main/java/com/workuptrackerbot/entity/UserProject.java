package com.workuptrackerbot.entity;

import javax.persistence.*;

@Entity
@Table(name = "up", uniqueConstraints = { @UniqueConstraint( columnNames = { "user_id", "project_id" } ) })
public class UserProject {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
        @JoinColumn(name = "user_id")
        private UserEntity userEntity;

        @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
        @JoinColumn(name = "project_id")
        private Project project;

        @Column
        private boolean own;

//        @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//        @JoinTable(
//                name = "intervals",
//                joinColumns = @JoinColumn(name = "up_id"),
//                inverseJoinColumns = @JoinColumn(name = "intervals_id")
//        )
//        private List<Interval> intervals = new ArrayList<>();

        public UserProject(UserEntity userEntity, Project project, boolean own) {
                this.userEntity = userEntity;
                this.project = project;
                this.own = own;
        }

        public UserProject() {
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public UserEntity getUserEntity() {
                return userEntity;
        }

        public void setUserEntity(UserEntity userEntity) {
                this.userEntity = userEntity;
        }

        public Project getProject() {
                return project;
        }

        public void setProject(Project project) {
                this.project = project;
        }

        public boolean isOwn() {
                return own;
        }

        public void setOwn(boolean own) {
                this.own = own;
        }

//        public List<Interval> getIntervals() {
//                return intervals;
//        }
//
//        public void setIntervals(List<Interval> intervals) {
//                this.intervals = intervals;
//        }
}
