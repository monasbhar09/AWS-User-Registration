package com.csye6225.demo.controllers;

import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.Region;
import com.csye6225.demo.AWSConfiguration;
import com.csye6225.demo.model.Attachment;
import com.csye6225.demo.model.Task;
import com.csye6225.demo.model.User;
import com.csye6225.demo.repository.TaskRepository;
import com.csye6225.demo.service.UserServiceImpl;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.AmazonServiceException;


@Controller
public class AttachmentController {
    private final static Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    ServletContext servletContext;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    AmazonS3Client amazonS3Client;

    private String bucketName;


    @RequestMapping(value = "/tasks/{taskId}/attachments", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getAllAttachments(@PathVariable(value="taskId") String taskid, HttpServletResponse response) {
        Task task = checkAccessToTask(getUser(), taskid);
        String jsonObject = new Gson().toJson(task.getAttachments());
        return jsonObject;
    }

    @RequestMapping(value = "/tasks/{taskId}/attachments", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String addAttachment(@RequestParam(value = "file") MultipartFile file, @PathVariable(value="taskId") String taskid, HttpServletRequest request, HttpServletResponse response){
        Task task = checkAccessToTask(getUser(), taskid);
        String jsonObject = null;
        if(task!=null){
            if(!file.isEmpty()){
                try{
                    bucketName = System.getProperty("bucketName");
                    System.out.println("BucketName:"+bucketName);
                    String realPathtoUploads = "/home/ubuntu/uploads/";
                    System.out.println(realPathtoUploads);
                    String orgName = file.getOriginalFilename();
                    String filePath = realPathtoUploads + orgName;

                    File newFile = new File(filePath);
                    newFile.createNewFile();
                    FileOutputStream fs = new FileOutputStream(newFile);
                    fs.write(file.getBytes());
                    fs.close();

                    Attachment attachment = new Attachment();
                    System.out.println("Uploading to S3");
                    System.out.println(file.getOriginalFilename());
                    try {
                        System.out.println("Inside try :");
                        amazonS3Client.putObject(new PutObjectRequest(bucketName,newFile.getName(),newFile));
                        System.out.println("Path to S3"+newFile.getAbsolutePath());
                        attachment.setUrl("https://"+bucketName+".s3.amazonaws.com/"+newFile.getName());
                        attachment = userServiceImpl.addAttachment(task,attachment);
                    } catch (AmazonServiceException e) {
                        System.err.println(e.getErrorMessage());
                    } finally{
                        newFile.delete();
                    }
                    System.out.println("Uploaded to S3");
                    jsonObject =new Gson().toJson(attachment);
                }
                catch(Exception e){
                    e.printStackTrace();
                    jsonObject =new Gson().toJson("Error uploading file");
                }
            } else{
                jsonObject =new Gson().toJson("File empty");
            }
        } else{
            jsonObject = new Gson().toJson("unauthorized");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }

        return jsonObject;
    }



    @RequestMapping(value = "/tasks/{taskId}/attachments/{attachmentId}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public String deleteTask(@PathVariable(value="taskId") String taskId, @PathVariable(value="attachmentId") String attachmentId, HttpServletResponse response){
        System.out.println("entering delete attachment");
        User u = getUser();
        Task existingTask = checkAccessToTask(u, taskId);

        String jsonObject;
        System.out.println("got existing task");
        if(existingTask != null){
            Attachment attachment = checkAttachmentInTask(existingTask, attachmentId);

            if(attachment != null) {
                existingTask.getAttachments().remove(attachment);
                taskRepository.saveAndFlush(existingTask);
                jsonObject = new Gson().toJson("attachment successfully deleted!");
            } else{
                jsonObject = new Gson().toJson("attachment not in task!");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
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
                System.out.println("task found!!");
                return task;
            }
        }
        return null;
    }

    private Attachment checkAttachmentInTask(Task task, String attachmentId) {
        List<Attachment> attachmentList = task.getAttachments();
        for (Attachment attachment : attachmentList) {
            if (attachment.getAttachmentId().equals(attachmentId)) {
                System.out.println("Attachment found!!");
                return attachment;
            }
        }
        return null;
    }




}


