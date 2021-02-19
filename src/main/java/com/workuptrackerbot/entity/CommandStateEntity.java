package com.workuptrackerbot.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "command")
public class CommandStateEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        @OnDelete(action = OnDeleteAction.CASCADE)
        @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
        @JoinColumn(name = "user_id")
        private UserEntity userEntity;

        @Column
        private String command;

        @Column
        private Integer index;


        public CommandStateEntity(UserEntity userEntity, String command, Integer index) {
                this.userEntity = userEntity;
                this.command = command;
                this.index = index;
        }

        public CommandStateEntity() {
        }

        public CommandStateEntity(UserEntity userEntity) {
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

        public String getCommand() {
                return command;
        }

        public void setCommand(String command) {
                this.command = command;
        }

        public Integer getIndex() {
                return index;
        }

        public void setIndex(Integer index) {
                this.index = index;
        }
}
