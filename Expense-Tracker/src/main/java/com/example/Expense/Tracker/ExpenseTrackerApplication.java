package com.example.Expense.Tracker;

import com.example.Expense.Tracker.entity.Expense;
import com.example.Expense.Tracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class ExpenseTrackerApplication implements CommandLineRunner {

	@Autowired
	private ExpenseService expenseService;

	public static void main(String[] args) {
		SpringApplication.run(ExpenseTrackerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			String command = scanner.nextLine();
			String[] splitCommand = command.split(" ");

			if (splitCommand[0].equals("expense-tracker")) {
				handleCommands(splitCommand);
			} else {
				System.out.println("Unknown command. Try again.");
			}
		}
	}

	private void handleCommands(String[] args) {
		switch (args[1]) {
			case "add":
				handleAddExpense(args);
				break;
			case "list":
				handleListExpenses();
				break;
			case "delete":
				handleDeleteExpense(args);
				break;
			case "summary":
				if (args.length > 2 && args[2].equals("--month")) {
					handleSummaryByMonth(args);
				} else {
					handleTotalSummary();
				}
				break;
			default:
				System.out.println("Invalid command");
				break;
		}
	}

	private void handleAddExpense(String[] args) {
		try {
			String description = extractArgument(args, "--description");
			double amount = Double.parseDouble(extractArgument(args, "--amount"));

			Expense expense = new Expense();
			expense.setDescription(description);
			expense.setAmount(amount);
			expense.setDate(LocalDate.now());

			Expense savedExpense = expenseService.addExpense(expense);
			System.out.println("Expense added successfully (ID: " + savedExpense.getId() + ")");
		} catch (Exception e) {
			System.out.println("Error adding expense: " + e.getMessage());
		}
	}

	private void handleListExpenses() {
		List<Expense> expenses = expenseService.getAllExpenses();
		if (expenses.isEmpty()) {
			System.out.println("No expenses found.");
		} else {
			System.out.println("ID  Date       Description  Amount");
			expenses.forEach(expense ->
					System.out.printf("%d   %s  %s       ₹%.2f%n",
							expense.getId(),
							expense.getDate(),
							expense.getDescription(),
							expense.getAmount())
			);
		}
	}

	private void handleDeleteExpense(String[] args) {
		try {
			Long id = Long.parseLong(extractArgument(args, "--id"));
			expenseService.deleteExpense(id);
			System.out.println("Expense deleted successfully");
		} catch (Exception e) {
			System.out.println("Error deleting expense: " + e.getMessage());
		}
	}

	private void handleTotalSummary() {
		double total = expenseService.getTotalExpenses();
		System.out.println("Total expenses: ₹" + total);
	}

	private void handleSummaryByMonth(String[] args) {
		try {
			int month = Integer.parseInt(args[3]);
			double total = expenseService.getTotalExpensesForMonth(month);
			System.out.println("Total expenses for " + Month.of(month) + ": ₹" + total);
		} catch (Exception e) {
			System.out.println("Error generating monthly summary: " + e.getMessage());
		}
	}

	private String extractArgument(String[] args, String key) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals(key)) {
				return args[i + 1];
			}
		}
		throw new IllegalArgumentException(key + " not provided");
	}
}