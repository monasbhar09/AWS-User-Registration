/**
 * Dhanashree Chavan, 001222495, chavan.d@husky.neu.edu
 * Monas Bhar, 001232781, bhar.m@husky.neu.edu
 * Akanksha Harshe, 001228921 , harshe.a@husky.neu.edu
 * Jyotsna Khatter, 001285017, khatter.j@husky.neu.edu
 **/
package com.csye6225.demo.service;

import java.util.Arrays;
import java.util.HashSet;

import com.csye6225.demo.auth.BCryptPasswordEncoderBean;
import com.csye6225.demo.model.Attachment;
import com.csye6225.demo.model.Task;
import com.csye6225.demo.model.User;
import com.csye6225.demo.repository.AttachmentRepository;
import com.csye6225.demo.repository.TaskRepository;
import com.csye6225.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private BCryptPasswordEncoderBean bCryptPasswordEncoderBean;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoderBean.bCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void saveAndFlush(User user){
        userRepository.saveAndFlush(user);
    }


    @Override
    public Task addTask(User user, Task task){
        task = taskRepository.save(task);
        user.getTaskList().add(task);
        User updatedUser = userRepository.saveAndFlush(user);
        return task;
    }

    @Override
    public Task updateTask(Task task){
        taskRepository.save(task);
        System.out.println("task updated!!!");
        return task;
    }

    @Override
    public void deleteTask(User user, Task task){
        //use method findOne here!

        taskRepository.delete(task);
        User updatedUser = userRepository.save(user);
        System.out.println("task deleted!!!!!");

    }

    @Override
    public Attachment addAttachment(Task task, Attachment attachment) {
        attachment = attachmentRepository.save(attachment);
        System.out.println("Attachment id:"+attachment.getAttachmentId());
        task.getAttachments().add(attachment);
        System.out.println("Task Id: " + task.getTaskId());
        Task savedTask = taskRepository.saveAndFlush(task);
        System.out.println("task saved successfully");
        return attachment;
    }
}
