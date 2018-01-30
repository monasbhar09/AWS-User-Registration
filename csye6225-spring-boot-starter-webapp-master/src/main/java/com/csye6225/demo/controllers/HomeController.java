/**
 * Dhanashree Chavan, 001222495, chavan.d@husky.neu.edu
 * Monas Bhar, 001232781, bhar.m@husky.neu.edu
 * Akanksha Harshe, 001228921 , harshe.a@husky.neu.edu
 * Jyotsna Khatter, 001285017, khatter.j@husky.neu.edu
 **/
package com.csye6225.demo.controllers;


import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.csye6225.demo.model.User;
import com.csye6225.demo.service.UserServiceImpl;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Controller
public class HomeController {

  private final static Logger logger = LoggerFactory.getLogger(HomeController.class);
  @Autowired
  UserServiceImpl userServiceImpl;
  @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public String welcome(HttpServletResponse response) {

    JsonObject jsonObject = new JsonObject();

    if (SecurityContextHolder.getContext().getAuthentication() != null
        && SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
      jsonObject.addProperty("message", "you are not logged in!!!");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    } else {
      jsonObject.addProperty("message", "you are logged in. current time is " + new Date().toString());
      response.setStatus(HttpServletResponse.SC_OK);
    }

    return jsonObject.toString();
  }

  @RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public String test(HttpServletResponse response) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("message", "authorized for /test");
    return jsonObject.toString();
  }

  @RequestMapping(value = "/testPost", method = RequestMethod.POST, produces = "application/json")
  @ResponseBody
  public String testPost(HttpServletResponse response) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("message", "authorized for /testPost");
    return jsonObject.toString();
  }

  @RequestMapping(value = "/user/register", method = RequestMethod.POST, produces = "application/json", consumes="application/json")
  @ResponseBody
  public String userRegister(@RequestBody User user, HttpServletResponse response) {
    JsonObject jsonObject = new JsonObject();
    if (userServiceImpl.findByUsername(user.getUsername()) != null) {
      jsonObject.addProperty("message", "User already exists.");
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    } else {
      userServiceImpl.saveUser(user);
      jsonObject.addProperty("message", "User added successfully!!");
      response.setStatus(HttpServletResponse.SC_OK);
    }
    return jsonObject.toString();

    }

  @RequestMapping(value = "/forgot-password", method = RequestMethod.POST, produces = "application/json")
  @ResponseBody
  public String forgotPassword(@RequestBody User user,HttpServletResponse response) {
    JsonObject jsonObject = new JsonObject();
     if(userServiceImpl.findByUsername(user.getUsername()) != null){
       AmazonSNSClient snsClient = new AmazonSNSClient(new InstanceProfileCredentialsProvider());
       snsClient.setRegion(Region.getRegion(Regions.US_EAST_1));

       //get topic arn
       String topicArn= snsClient.createTopic("password_reset").getTopicArn();
       PublishRequest publishRequest = new PublishRequest(topicArn, user.getUsername());
       PublishResult publishResult = snsClient.publish(publishRequest);

       //print MessageId of message published to SNS topic
       System.out.println("Password reset message sent!");
       jsonObject.addProperty("message","Reset link has been sent to the email");
       response.setStatus(HttpServletResponse.SC_OK);
     }else{
       jsonObject.addProperty("message", "Invalid username!");
     }
    return jsonObject.toString();

  }




}
