package com.csye6225.demo.controllers;

import com.csye6225.demo.model.Task;
import com.csye6225.demo.model.User;
import com.csye6225.demo.service.UserServiceImpl;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class TaskController {

    private final static Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    UserServiceImpl userServiceImpl;

    @RequestMapping(value = "/tasks", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getAllTasks() {
        String jsonObject = new Gson().toJson(getUser().getTaskList());
        return jsonObject;
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.POST, produces = "application/json", consumes="application/json")
    @ResponseBody
    public String addTask(@RequestBody Task task, HttpServletResponse response){
        Task taskReturn = userServiceImpl.addTask(getUser(),task);
        String jsonObject = new Gson().toJson(taskReturn);
        response.setStatus(HttpServletResponse.SC_OK);
        return jsonObject;
    }

    @RequestMapping(value = "/tasks/{taskId}", method = RequestMethod.PUT, produces = "application/json", consumes="application/json")
    @ResponseBody
    public String updateTask(@RequestBody Task task, @PathVariable(value="taskId") String taskId, HttpServletResponse response){

        Task existingTask = checkAccessToTask(getUser(), taskId);

        String jsonObject;
        if(existingTask != null){

            task.setTaskId(existingTask.getTaskId());
            jsonObject = new Gson().toJson(userServiceImpl.updateTask(task));
            response.setStatus(HttpServletResponse.SC_OK);
        }else{
            jsonObject = new Gson().toJson("unauthorized");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
        return jsonObject;
    }

    @RequestMapping(value = "/tasks/{taskId}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public String deleteTask(@PathVariable(value="taskId") String taskId, HttpServletResponse response){
        System.out.println("entering deletetask");
        User u = getUser();
        Task existingTask = checkAccessToTask(u, taskId);

        String jsonObject;
        System.out.println("got existing task");
        if(existingTask != null){
            System.out.println(existingTask.getTaskId());
            u.getTaskList().remove(existingTask);
            userServiceImpl.saveAndFlush(u);
            jsonObject = new Gson().toJson("task successfully deleted!");
            response.setStatus(HttpServletResponse.SC_OK);
        }else{
            jsonObject = new Gson().toJson("unauthorized");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
        return jsonObject;

    }

    private User getUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userServiceImpl.findByUsername(name);
        return user;
    }

    private Task checkAccessToTask(User user, String taskId){
        List<Task> taskList = getUser().getTaskList();
        for(Task task : taskList)
        {
            if (task.getTaskId().equals(taskId)) {
                System.out.println("task updated!!!");
                return task;
            }
        }
        return null;
    }

}
