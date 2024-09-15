package com.example.Expense.Tracker.service;

import com.example.Expense.Tracker.entity.Expense;
import com.example.Expense.Tracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Expense not found"));
    }

    public Expense addExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public Expense updateExpense(Long id, Expense updatedExpense) {
        Expense expense = expenseRepository.findById(id).orElseThrow();
        expense.setDescription(updatedExpense.getDescription());
        expense.setAmount(updatedExpense.getAmount());
        expense.setCategory(updatedExpense.getCategory());
        expense.setDate(updatedExpense.getDate());
        return expenseRepository.save(expense);
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    public List<Expense> getMonthlyExpenses(int month) {
        LocalDate start = LocalDate.of(LocalDate.now().getYear(), month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        return expenseRepository.findByDateBetween(start, end);
    }

    public double getTotalExpenses() {
        return getAllExpenses().stream().mapToDouble(Expense::getAmount).sum();
    }

    public double getTotalExpensesForMonth(int month) {
        return getMonthlyExpenses(month).stream().mapToDouble(Expense::getAmount).sum();
    }
}

