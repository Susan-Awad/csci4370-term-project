package proj.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import proj.models.User;
import proj.models.Transaction;
import proj.services.CategoryService;
import proj.services.TransactionService;
import proj.services.UserService;
import proj.utility.Utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;




/**
 * Handles /profile URL and its sub URLs.
 */
@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private final UserService userService;
   private final CategoryService categoryService;
    private final TransactionService transactionService;


    @Autowired
    public TransactionController(UserService userService, TransactionService transactionService, CategoryService categoryService) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.categoryService = categoryService;
    }
    @GetMapping
    public ModelAndView transactionsPage(
        @RequestParam(value ="from", required = false) String from,
        @RequestParam(value="to", required = false) String to, 
        @RequestParam(value="type", required = false) String type,
        @RequestParam(value= "categoryId", required =false) Integer categoryId,
        @RequestParam(value= "editId", required =false) Integer editId
    ) {

        System.out.println("User is attempting to view transaction of the logged in user.");
        ModelAndView mv = new ModelAndView("transactions_page");

        User loggedIn = userService.getLoggedInUser();

        if(loggedIn == null) {
            mv.addObject("errorMsg", "Must be logged in to view transactions.");
            mv.addObject("isNoContent", true); 

            return mv;
        } //if
        int userID = Integer.parseInt(loggedIn.getUserId());

        

        List<Transaction> allTransactions = transactionService.getAllTransactionsForUser(userID);

        // get categories 
        mv.addObject("categories", categoryService.getCategoriesForUser(userID));
        mv.addObject("selectedCategoryId", categoryId);

        LocalDate dateFrom = null;
        LocalDate dateTo = null;

        if(from!= null && !from.isBlank()) {
            dateFrom = LocalDate.parse(from);
        } //if
        if(to!= null && !to.isBlank()) {
            dateTo = LocalDate.parse(to);
        } //if

        String filter = (type== null || type.isBlank()) ? null : type.toUpperCase();

        List<Transaction> filteredList = new ArrayList<>();

        for (Transaction t : allTransactions) {
            LocalDate transDate = t.getDate();
            String transType = t.getType();
            boolean success = true; 

            if (dateFrom != null && transDate.isBefore(dateFrom)) {
                success = false; 
            } //if 
            if (dateTo !=null && transDate.isAfter(dateTo)) {
                success = false;
            } //if
            if (filter != null && (transType == null || !transType.equalsIgnoreCase(filter))) {
                success = false; 
            } //if
            if (categoryId != null && categoryId != 0 && t.getCategoryId() != categoryId) {
                success = false; 
            }
            if (success) {
                filteredList.add(t);
            }
        } //for 
        mv.addObject("transactions", filteredList);
        mv.addObject("isNoContent", filteredList.isEmpty());

       if(editId != null) {
        Transaction transactionToEdit = transactionService.getTransactionById(editId);
        mv.addObject("editTransaction", transactionToEdit);
       } //if
        return mv;
    } //transactionsPage


    /**
     * Add a transaction 
     */
    @PostMapping("/add")
    public String addTransaction(
        @RequestParam("date") LocalDate date, 
        @RequestParam("amount") double amount, 
        @RequestParam("description") String description, 
        @RequestParam("categoryId") int categoryId

    ){
        User user = userService.getLoggedInUser();
        if (user == null) {
            return "redirect:/login";
        } //if
        int userID = Integer.parseInt(user.getUserId());
        transactionService.addTransaction(userID, categoryId, date, amount, description);

        return "redirect:/transactions";
    } //addTransaction

    /** 
     * Delete a transaction
     */
    @PostMapping("/delete/{transactionId}")
    public String deleteTransaction(@PathVariable int transactionId) {
        transactionService.deleteTransaction(transactionId);
        return "redirect:/transactions";
    } //deleteTransaction

     /**
     * Edit a transaction 
     */
    @PostMapping("/edit")
    public String editTransaction(
        @RequestParam("transactionId") int transactionId,
        @RequestParam("date") LocalDate date, 
        @RequestParam("amount") double amount, 
        @RequestParam("description") String description, 
        @RequestParam("categoryId") int categoryId

    ){
        transactionService.updateTransaction(transactionId, categoryId, date, amount, description);
        return "redirect:/transactions";
    } //editTransaction
   
}
