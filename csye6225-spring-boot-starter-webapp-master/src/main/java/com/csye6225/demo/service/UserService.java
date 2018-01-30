/**
 * Dhanashree Chavan, 001222495, chavan.d@husky.neu.edu
 * Monas Bhar, 001232781, bhar.m@husky.neu.edu
 * Akanksha Harshe, 001228921 , harshe.a@husky.neu.edu
 * Jyotsna Khatter, 001285017, khatter.j@husky.neu.edu
 **/
package com.csye6225.demo.service;

import com.csye6225.demo.model.Attachment;
import com.csye6225.demo.model.Task;
import com.csye6225.demo.model.User;

public interface UserService {
    public User findByUsername(String username);
    public void saveUser(User user);
    public Task addTask(User user, Task task);
    public Task updateTask(Task task);
    public void deleteTask(User user,Task task);
    public Attachment addAttachment(Task task, Attachment attachment);
    public void saveAndFlush(User user);

}
