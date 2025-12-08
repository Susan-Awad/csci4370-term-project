package proj.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
@RequestMapping("/profile")
public class ProfileController {

    // UserService has user login and registration related functions.
    private final UserService userService;
    private final DataSource dataSource; 

    /**
     * See notes in AuthInterceptor.java regarding how this works 
     * through dependency injection and inversion of control.
     */
    @Autowired
    public ProfileController(UserService userService, DataSource dataSource) {
        this.userService = userService;
        this.dataSource = dataSource; 
    }

    /**
     * This function handles /profile URL itself.
     * This serves the webpage that shows posts of the logged in user.
     */
    @GetMapping
    public ModelAndView profileOfLoggedInUser() {
        System.out.println("User is attempting to view profile of the logged in user.");
        return profileOfSpecificUser(userService.getLoggedInUser().getUserId());
    }

    /**
     * Handles form submission from the Change Details section.
     * Blank fields mean "keep the existing value".
     */
    @PostMapping("/update")
    public ModelAndView updateProfile(@RequestParam("firstName") String firstName,
                                      @RequestParam("lastName") String lastName,
                                      @RequestParam("username") String username) {

        User loggedIn = userService.getLoggedInUser();
        if (loggedIn == null) {
            return new ModelAndView("redirect:/login");
        }

        int userId = Integer.parseInt(loggedIn.getUserId());

        // Get current values from DB (so they stay correct even if session is stale).
        User currentUser = userService.getUserById(userId);
        String currentFirst = (currentUser != null) ? currentUser.getFirstName() : loggedIn.getFirstName();
        String currentLast  = (currentUser != null) ? currentUser.getLastName()  : loggedIn.getLastName();
        String currentUsername = userService.getUsernameByUserId(userId);

        // If a field is blank, keep the existing value.
        String newFirst = (firstName == null || firstName.isBlank())
                ? currentFirst
                : firstName;

        String newLast = (lastName == null || lastName.isBlank())
                ? currentLast
                : lastName;

        String newUsername = (username == null || username.isBlank())
                ? currentUsername
                : username;

        userService.updateUserProfile(userId, newFirst, newLast, newUsername);

        return new ModelAndView("redirect:/profile");
    }

    /**
     * This function handles /profile/{userId} URL.
     * This serves the webpage that shows posts of a specific user given by userId.
     * See comments in PeopleController.java in followUnfollowUser function regarding 
     * how path variables work.
     */
    @GetMapping("/{userId}")
    public ModelAndView profileOfSpecificUser(@PathVariable("userId") String userId) {
        System.out.println("User is attempting to view profile: " + userId);

        // Require login
        User loggedIn = userService.getLoggedInUser();
        if (loggedIn == null) {
            return new ModelAndView("redirect:/login");
        }

        // See notes on ModelAndView in BookmarksController.java.
        ModelAndView mv = new ModelAndView("posts_page");

        // Figure out which user we are showing and also get their username.
        User profileUser = null;
        String profileUsername = null;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT userId, username, firstName, lastName FROM user WHERE userId = ?")) {

            stmt.setInt(1, Integer.parseInt(userId));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("userId");
                    String first = rs.getString("firstName");
                    String last  = rs.getString("lastName");
                    profileUsername = rs.getString("username");

                    profileUser = new User(id, first, last);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Fallback: if that userId did not exist, at least show the logged-in user.
        if (profileUser == null) {
            profileUser = loggedIn;
        }

        // Attach the user and username to the model so Mustache can render it.
        mv.addObject("profileUser", profileUser);
        mv.addObject("profileUsername", profileUsername);

        // For now, still show the "No content to show." message.
        mv.addObject("isNoContent", true);

        return mv;
    }
}