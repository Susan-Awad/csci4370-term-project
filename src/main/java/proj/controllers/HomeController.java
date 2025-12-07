package proj.controllers;


import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import proj.services.UserService;
import proj.services.TransactionService;
import proj.models.Transaction;

/**
 * This controller handles the home page and some of it's sub URLs.
 */
@Controller
@RequestMapping
public class HomeController {
    @Autowired
    UserService userService;
    @Autowired
    TransactionService transactionService;

    @GetMapping({"/", "/home"})
    public ModelAndView webpage (@RequestParam(name = "error", required = false) String error) {
        ModelAndView mv = new ModelAndView("home_page");

        var user = userService.getLoggedInUser();
        if (user == null) {
            mv.setViewName("redirect:/login");
            return mv;
        }
        
        int userId = Integer.parseInt(user.getUserId());
        mv.addObject("firstName", user.getFirstName());

        YearMonth month = YearMonth.now();
        LocalDate monthStart = month.atDay(1);
        LocalDate nextMonthStart = month.plusMonths(1).atDay(1);
        mv.addObject("currentMonth", month.toString());

        BigDecimal income = transactionService.getTotalIncomeForMonth(userId, monthStart, nextMonthStart);
        BigDecimal expense = transactionService.getTotalExpenseForMonth(userId, monthStart, nextMonthStart);
        BigDecimal net = income.subtract(expense);

        mv.addObject("totalIncome", income);
        mv.addObject("totalExpense", expense);
        mv.addObject("net", net);

        List<Transaction> recent = transactionService.getRecentTransactions(userId, 5);
        mv.addObject("transactions", recent);

        mv.addObject("errorMessage", error);

        return mv;
    }
}
