/**
 * Dhanashree Chavan, 001222495, chavan.d@husky.neu.edu
 * Monas Bhar, 001232781, bhar.m@husky.neu.edu
 * Akanksha Harshe, 001228921 , harshe.a@husky.neu.edu
 * Jyotsna Khatter, 001285017, khatter.j@husky.neu.edu
 **/
package com.csye6225.demo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="task")
public class Task {

    public Task() {
        this.attachments=new ArrayList<>();
    }

    @Id
    @GeneratedValue(generator = "strategy-uuid")
    @GenericGenerator(name = "strategy-uuid", strategy = "uuid")
    @Column(name="taskId")
    private String taskId;

    @NotNull
    private String description;



    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinTable(name="taskwithattachments",
            joinColumns=@JoinColumn(name="taskId"),
            inverseJoinColumns=@JoinColumn(name="attachmentId"))
    private List<Attachment> attachments;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }
}
