import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.*;
public class Manager extends Employee {
    // Creates a manager user from loaded employee data
    public Manager(String[] a){
        super(a);
    }
    // Main menu for manager actions
    public void show_menu() {
        while(true) {
            System.out.println("==== Employee Options ====");
            System.out.println("0. Log-Out");
            System.out.println("1. Clock-In.");
            System.out.println("2. Clock-Out");
            System.out.println("3. Sales Record");
            System.out.println("4. Search Item");
            System.out.println("5. Morning Stock Count");
            System.out.println("6. Night Stock Count");
            System.out.println("7. Stock In / Stock Out");
            System.out.println("8. Edit Stock Info");
            System.out.println("9. Filter & Sort Sales History");
            System.out.println("=== Manager Options ===");
            System.out.println("10. Register New Staff");
            System.out.println("11. View Performance Metrics");
            System.out.println("12. Data Analytics");
            Scanner sc = new Scanner(System.in);
            int option = sc.nextInt();
            sc.nextLine();
            switch (option) {
                case 0:
                    System.out.println("Logging Out.....");
                    FileManager.Data_Saver();
                    return;
                case 1:
                    clock_in();
                    break;
                case 2:
                    clock_out();
                    break;
                case 3:
                    sales_record();
                    break;
                case 4:
                    search_item();
                    break;
                case 5:
                    morning_stock_count();
                    break;
                case 6:
                    night_stock_count();
                    break;
                case 7:
                    stock_in_out_menu();
                    break;
                case 8:
                    EditInformation.showMenu(this);
                    break;
                case 9:
                    FilterandSortSalesHistory.showMenu();
                    break;
                case 10:
                    register_new_staff();
                    break;
                case 11:
                    view_performance();
                    break;
                case 12:
                    data_analytics();
                    break;
            }
        }
    }

    // Registers a new staff member with explicit outlet assignment
    public void register_new_staff(){
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Register New Employee ===");
        System.out.print("Enter Employee Name: ");
        String new_name = sc.nextLine();
        System.out.print("Enter Employee ID: ");
        String new_id = sc.nextLine();
        System.out.print("Set Password: ");
        String new_password = sc.nextLine();
        System.out.print("Set Role: ");
        String new_job_type = sc.nextLine();
        System.out.print("Enter Outlet ID: ");
        String new_outlet_id = sc.nextLine();
        String new_role = "Employee";
        String[] new_employee = {new_name, new_password, new_id, new_role, new_job_type, new_outlet_id};
        System.out.println("Employee Registered Successfully!");
        FileManager.employee_list.add(new_employee);
    }

    // Aggregates sales totals per employee and prints a ranking
    public void view_performance() {
        System.out.println("\n=== Employee Performance Metrics ===");


        Map<String, Double> totalSales = new HashMap<>();
        Map<String, Integer> transactions = new HashMap<>();


        for (int i = 1; i < FileManager.employee_list.size(); i++) {

            String[] emp = FileManager.employee_list.get(i);
            String employeeName = emp[0];

            totalSales.put(employeeName, 0.0);
            transactions.put(employeeName, 0);
        }


        for (int i = 1; i < FileManager.sales_history.size(); i++) {

            String[] sale = FileManager.sales_history.get(i);

            String employeeName = sale[3];
            double totalPrice = Double.parseDouble(sale[8]);

            totalSales.put(
                    employeeName,
                    totalSales.getOrDefault(employeeName, 0.0) + totalPrice
            );

            transactions.put(
                    employeeName,
                    transactions.getOrDefault(employeeName, 0) + 1
            );
        }


        List<Map.Entry<String, Double>> ranking =
                new ArrayList<>(totalSales.entrySet());

        ranking.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));


        System.out.printf("%-25s %-15s %-15s%n",
                "Employee Name", "Total Sales", "Transactions");
        System.out.println("-------------------------------------------------------");

        for (Map.Entry<String, Double> entry : ranking) {

            String name = entry.getKey();
            double sales = entry.getValue();
            int count = transactions.get(name);

            System.out.printf("%-25s %-15.2f %-15d%n",
                    name, sales, count);
        }
    }
    // Dispatches analytics routines based on user selection
    public void data_analytics() {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Data Analytics ===");
        System.out.println("Select analysis type:");
        System.out.println("1. Daily Sales Summary");
        System.out.println("2. Weekly Sales Summary");
        System.out.println("3. Monthly Sales Summary");
        System.out.println("4. Most Sold Product Model");
        System.out.println("5. Average Daily Revenue");
        System.out.println("6. Return to Main Menu");

        int option = sc.nextInt();
        sc.nextLine();

        switch (option) {
            case 1:
                daily_sales_summary();
                break;
            case 2:
                weekly_sales_summary();
                break;
            case 3:
                monthly_sales_summary();
                break;
            case 4:
                most_sold_product();
                break;
            case 5:
                average_daily_revenue();
                break;
            case 6:
                System.out.println("Returning to main menu...");
                break;
            default:
                System.out.println("Invalid option!");
        }
    }

    // Summarizes sales by day across the history list
    public void daily_sales_summary() {
        System.out.println("=== Daily Sales Summary ===");

        Map<String, Double> dailySales = new HashMap<>();
        Map<String, Integer> dailyCount = new HashMap<>();

        // 检查是否有销售数据
        if (FileManager.sales_history.isEmpty()) {
            System.out.println("No sales data available.");
            return;
        }

        for (String[] sale : FileManager.sales_history) {
            if (sale.length < 10 || sale[0] == null || sale[0].isEmpty()) {
                continue;
            }

            String date = sale[0].trim();

            double total = 0.0;
            try {
                total = Double.parseDouble(sale[8].trim());
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                continue;
            }

            dailySales.put(date, dailySales.getOrDefault(date, 0.0) + total);
            dailyCount.put(date, dailyCount.getOrDefault(date, 0) + 1);
        }

        if (dailySales.isEmpty()) {
            System.out.println("No valid sales data available.");
            return;
        }

        System.out.println("\n=== Daily Sales Report ===");
        System.out.println("Date\t\t\tTotal Sales (RM)\tTransactions");
        System.out.println("--------------------------------------------------------");

        List<String> dates = new ArrayList<>(dailySales.keySet());
        Collections.sort(dates);

        double grandTotal = 0.0;
        int totalTransactions = 0;

        for (String date : dates) {
            double dailyTotal = dailySales.get(date);
            int dailyTransactions = dailyCount.get(date);

            System.out.printf("%-15s\t\tRM%10.2f\t\t%d%n", date, dailyTotal, dailyTransactions);

            grandTotal += dailyTotal;
            totalTransactions += dailyTransactions;
        }

        System.out.println("--------------------------------------------------------");
        System.out.printf("GRAND TOTAL\t\t\tRM%10.2f\t\t%d%n", grandTotal, totalTransactions);
        System.out.printf("AVERAGE PER DAY\t\t\tRM%10.2f\t\t%.1f%n",
                grandTotal / dates.size(),
                (double) totalTransactions / dates.size());
    }

    // Groups sales by ISO week and can drill into a selected week
    public void weekly_sales_summary() {
        System.out.println("=== Weekly Sales Summary ===");

        Map<String, Double> weeklySales = new TreeMap<>();
        Map<String, Set<String>> weeklyDays = new HashMap<>();

        for (String[] sale : FileManager.sales_history) {
            if (sale.length < 10 || sale[0] == null || sale[0].isEmpty()) {
                continue;
            }
            String dateStr = sale[0].trim();
            LocalDate date;
            try {
                date = LocalDate.parse(dateStr);
            } catch (Exception e) {
                continue;
            }
            int year = date.getYear();
            int week = date.get(java.time.temporal.WeekFields.ISO.weekOfWeekBasedYear());
            String weekKey = String.format("%d-W%02d", year, week);

            double total = 0.0;
            try {
                total = Double.parseDouble(sale[8].trim());
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                continue;
            }

            weeklySales.put(weekKey, weeklySales.getOrDefault(weekKey, 0.0) + total);

            weeklyDays.putIfAbsent(weekKey, new HashSet<>());
            weeklyDays.get(weekKey).add(dateStr);
        }

        if (weeklySales.isEmpty()) {
            System.out.println("No sales data available for weekly summary.");
            return;
        }

        System.out.println("Week\t\t\tTotal Sales (RM)\tSales Days\tAverage Daily");
        System.out.println("--------------------------------------------------------------------");

        double totalWeeklySales = 0.0;
        int totalWeeks = weeklySales.size();

        for (Map.Entry<String, Double> entry : weeklySales.entrySet()) {
            String week = entry.getKey();
            double weekTotal = entry.getValue();
            int salesDays = weeklyDays.get(week).size();
            double avgDaily = weekTotal / salesDays;

            System.out.printf("%-10s\t\tRM%10.2f\t\t%d\t\tRM%10.2f%n",
                    week, weekTotal, salesDays, avgDaily);
            totalWeeklySales += weekTotal;
        }

        double avgWeeklySales = totalWeeklySales / totalWeeks;
        System.out.println("--------------------------------------------------------------------");
        System.out.printf("Total Weeks: %d%n", totalWeeks);
        System.out.printf("Total Weekly Sales: RM%.2f%n", totalWeeklySales);
        System.out.printf("Average Weekly Sales: RM%.2f%n", avgWeeklySales);

        System.out.println("\n=== Detailed Weekly Breakdown ===");
        Scanner sc = new Scanner(System.in);
        System.out.print("Would you like to see detailed breakdown for a specific week? (yes/no): ");
        String response = sc.nextLine().toLowerCase();

        if (response.equals("yes") || response.equals("y")) {
            System.out.print("Enter week (format: YYYY-WW, e.g., 2026-W01): ");
            String selectedWeek = sc.nextLine();

            if (weeklyDays.containsKey(selectedWeek)) {
                System.out.println("\n=== Sales for Week " + selectedWeek + " ===");
                System.out.println("Date\t\t\tSales (RM)");
                System.out.println("----------------------------------");

                List<String> dates = new ArrayList<>(weeklyDays.get(selectedWeek));
                Collections.sort(dates);

                double weekTotal = weeklySales.get(selectedWeek);
                for (String date : dates) {

                    double dailyTotal = 0.0;
                    for (String[] sale : FileManager.sales_history) {
                        if (sale.length >= 10 && sale[0].trim().equals(date)) {
                            try {
                                dailyTotal += Double.parseDouble(sale[8].trim());
                            } catch (Exception e) {

                            }
                        }
                    }
                    System.out.printf("%-15s\tRM%10.2f%n", date, dailyTotal);
                }
                System.out.println("----------------------------------");
                System.out.printf("Week Total: RM%.2f%n", weekTotal);
            } else {
                System.out.println("Week " + selectedWeek + " not found in records.");
            }
        }
    }

    // Rolls up sales totals per calendar month
    public void monthly_sales_summary() {
        System.out.println("=== Monthly Sales Summary ===");

        Map<String, Double> monthlySales = new HashMap<>();
        Map<String, Set<String>> monthDays = new HashMap<>();

        for (String[] sale : FileManager.sales_history) {
            if (sale.length < 10 || sale[0] == null || sale[0].isEmpty()) {
                continue;
            }

            String dateStr = sale[0].trim();
            if (dateStr.length() < 7) {
                continue;
            }

            String month = dateStr.substring(0, 7);

            double total = 0.0;
            try {
                total = Double.parseDouble(sale[8].trim());
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                continue;
            }

            monthlySales.put(month, monthlySales.getOrDefault(month, 0.0) + total);

            monthDays.putIfAbsent(month, new HashSet<>());
            monthDays.get(month).add(dateStr);
        }

        if (monthlySales.isEmpty()) {
            System.out.println("No sales data available.");
            return;
        }

        System.out.println("\n=== Monthly Sales Report ===");
        System.out.println("Month\t\t\tTotal Sales (RM)\tSales Days");
        System.out.println("------------------------------------------------");

        List<String> months = new ArrayList<>(monthlySales.keySet());
        Collections.sort(months);

        for (String month : months) {
            double monthlyTotal = monthlySales.get(month);
            int daysCount = monthDays.getOrDefault(month, Collections.emptySet()).size();
            System.out.printf("%-15s\t\tRM%10.2f\t\t%d%n", month, monthlyTotal, daysCount);
        }
    }

    // Finds top selling models by quantity and revenue
    public void most_sold_product() {
        System.out.println("=== Most Sold Product Model ===");

        Map<String, Integer> productSales = new HashMap<>();
        Map<String, Double> productRevenue = new HashMap<>();

        for (String[] sale : FileManager.sales_history) {
            if (sale.length < 10 || sale[5] == null || sale[5].isEmpty()) {
                continue;
            }

            String model = sale[5].trim();

            int quantity = 0;
            double total = 0.0;

            try {
                quantity = Integer.parseInt(sale[6].trim());
                total = Double.parseDouble(sale[8].trim());
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                continue;
            }

            productSales.put(model, productSales.getOrDefault(model, 0) + quantity);
            productRevenue.put(model, productRevenue.getOrDefault(model, 0.0) + total);
        }

        if (productSales.isEmpty()) {
            System.out.println("No product sales data available.");
            return;
        }

        List<Map.Entry<String, Integer>> sortedProducts = new ArrayList<>(productSales.entrySet());
        sortedProducts.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        System.out.println("\n=== Top Selling Products ===");
        System.out.println("Rank\tModel\t\t\t\tQuantity Sold\tRevenue (RM)");
        System.out.println("---------------------------------------------------------------");

        int rank = 1;
        for (Map.Entry<String, Integer> entry : sortedProducts) {
            if (rank > 10) break;

            String model = entry.getKey();
            int quantity = entry.getValue();
            double revenue = productRevenue.getOrDefault(model, 0.0);

            String displayModel = model.length() > 20 ? model.substring(0, 17) + "..." : model;
            System.out.printf("%d\t%-20s\t%d\t\tRM%10.2f%n", rank, displayModel, quantity, revenue);
            rank++;
        }

        if (!sortedProducts.isEmpty()) {
            Map.Entry<String, Integer> bestSeller = sortedProducts.get(0);
            String bestModel = bestSeller.getKey();
            int bestQuantity = bestSeller.getValue();
            double bestRevenue = productRevenue.get(bestModel);

            System.out.println("\n=== TOP SELLER ===");
            System.out.println("Model: " + bestModel);
            System.out.println("Total Quantity Sold: " + bestQuantity);
            System.out.printf("Total Revenue: RM%.2f%n", bestRevenue);
        }
    }

    // Calculates average daily revenue and basic variability stats
    public void average_daily_revenue() {
        System.out.println("=== Average Daily Revenue ===");

        Map<String, Double> dailyRevenue = new HashMap<>();

        for (String[] sale : FileManager.sales_history) {
            if (sale.length < 10 || sale[0] == null || sale[0].isEmpty()) {
                continue;
            }

            String date = sale[0].trim();
            double total = 0.0;

            try {
                total = Double.parseDouble(sale[8].trim());
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                continue;
            }

            dailyRevenue.put(date, dailyRevenue.getOrDefault(date, 0.0) + total);
        }

        if (dailyRevenue.isEmpty()) {
            System.out.println("No sales data available.");
            return;
        }

        int totalDays = dailyRevenue.size();
        double totalRevenue = dailyRevenue.values().stream().mapToDouble(Double::doubleValue).sum();
        double averageDailyRevenue = totalRevenue / totalDays;

        double maxDaily = Collections.max(dailyRevenue.values());
        double minDaily = Collections.min(dailyRevenue.values());

        String maxDate = dailyRevenue.entrySet().stream()
                .filter(entry -> Math.abs(entry.getValue() - maxDaily) < 0.001)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("N/A");

        String minDate = dailyRevenue.entrySet().stream()
                .filter(entry -> Math.abs(entry.getValue() - minDaily) < 0.001)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("N/A");

        double variance = 0.0;
        for (double daily : dailyRevenue.values()) {
            variance += Math.pow(daily - averageDailyRevenue, 2);
        }
        double stdDev = Math.sqrt(variance / totalDays);

        System.out.println("\n=== Revenue Analysis ===");
        System.out.printf("Analysis Period: %d days%n", totalDays);
        System.out.printf("Total Revenue: RM%.2f%n", totalRevenue);
        System.out.printf("Average Daily Revenue: RM%.2f%n", averageDailyRevenue);
        System.out.println("----------------------------------");
        System.out.printf("Highest Daily Revenue: RM%.2f (on %s)%n", maxDaily, maxDate);
        System.out.printf("Lowest Daily Revenue: RM%.2f (on %s)%n", minDaily, minDate);
        System.out.printf("Standard Deviation: RM%.2f%n", stdDev);

        System.out.println("\n=== Performance Analysis ===");
        double revenueVariability = stdDev / averageDailyRevenue;
        if (revenueVariability > 0.5) {
            System.out.println("⚠️  Revenue fluctuates significantly day-to-day.");
            System.out.println("    Consider analyzing peak sales days.");
        } else {
            System.out.println("✅  Revenue is relatively stable.");
        }

        if (averageDailyRevenue > 0) {
            System.out.printf("Projected Monthly Revenue (30 days): RM%.2f%n", averageDailyRevenue * 30);
        }
    }
}
