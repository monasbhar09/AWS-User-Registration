/**
 * Dhanashree Chavan, 001222495, chavan.d@husky.neu.edu
 * Monas Bhar, 001232781, bhar.m@husky.neu.edu
 * Akanksha Harshe, 001228921 , harshe.a@husky.neu.edu
 * Jyotsna Khatter, 001285017, khatter.j@husky.neu.edu
 **/
package com.csye6225.demo.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="user")
public class User {

    @Id
    @Column(name="username")
    private String username;
    @Column(name="password")
    private String password;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinTable(name="userandtasks",
            joinColumns=@JoinColumn(name="username"),
            inverseJoinColumns=@JoinColumn(name="taskId"))
    private List<Task> taskList;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}
