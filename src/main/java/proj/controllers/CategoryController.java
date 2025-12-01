package proj.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import proj.models.User;
import proj.services.UserService;
import proj.utility.Utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import javax.sql.DataSource;

/**
 * Handles /profile URL and its sub URLs.
 */
@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final UserService userService;
    private final DataSource dataSource; 


    @Autowired
    public CategoryController(UserService userService, DataSource dataSource) {
        this.userService = userService;
        this.dataSource = dataSource; 
    }

    @GetMapping
    public ModelAndView profileOfLoggedInUser() {
        System.out.println("User is attempting to view categories of the logged in user.");
        return profileOfSpecificUser(userService.getLoggedInUser().getUserId());
    }

    @GetMapping("/{userId}")
    public ModelAndView profileOfSpecificUser(@PathVariable("userId") String userId) {
        System.out.println("User is attempting to view categories: " + userId);
        
        ModelAndView mv = new ModelAndView("category_page");

        // Following line populates sample data.
        // You should replace it with actual data from the database.

        // If an error occured, you can set the following property with the
        // error message to show the error message to the user.
         //String errorMessage = "Some error occured!";
       // mv.addObject("errorMessage", errorMessage);

        // Enable the following line if you want to show no content message.
        // Do that if your content list is empty.
         mv.addObject("isNoContent", true);
        return mv;
    }
    
}
