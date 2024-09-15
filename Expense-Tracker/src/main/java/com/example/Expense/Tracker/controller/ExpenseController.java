package com.example.Expense.Tracker.controller;

import com.example.Expense.Tracker.entity.Expense;
import com.example.Expense.Tracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<Expense> addExpense(@RequestBody Expense expense){
        return new ResponseEntity<>(expenseService.addExpense(expense), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Expense> getALLExpenses(){
        return expenseService.getAllExpenses();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id,@RequestBody Expense expense){
        return new ResponseEntity<>(expenseService.updateExpense(id, expense), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/summary")
    public double getTotalExpenses() {
        return expenseService.getTotalExpenses();
    }

    @GetMapping("/summary/{month}")
    public double getTotalExpensesForMonth(@PathVariable int month) {
        return expenseService.getTotalExpensesForMonth(month);
    }

}
