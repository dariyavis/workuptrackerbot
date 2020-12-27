package com.workuptrackerbot.entity;

import javax.persistence.*;

@Entity
@Table(name = "up")
public class UserProject {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "user_id")
        private UserEntity userEntity;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JoinColumn(name = "project_id")
        private Project project;

        private boolean own;

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
}
