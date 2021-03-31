package com.workuptrackerbot.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "action_state")
public class ActionStateEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        @OnDelete(action = OnDeleteAction.CASCADE)
        @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
        @JoinColumn(name = "user_id")
        private UserEntity userEntity;

        @Column
        private String action;

        @Column
        private String data;


        public ActionStateEntity(UserEntity userEntity, String action, String data) {
                this.userEntity = userEntity;
                this.action = action;
        }

        public ActionStateEntity() {
        }

        public ActionStateEntity(UserEntity userEntity) {
                this.userEntity = userEntity;
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

        public String getAction() {
                return action;
        }

        public void setAction(String command) {
                this.action = command;
        }

        public String getData() {
                return data;
        }

        public void setData(String data) {
                this.data = data;
        }
}
