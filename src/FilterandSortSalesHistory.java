import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Locale;

public class FilterandSortSalesHistory {

    // Filters sales records by date range and applies sorting
    // Displays results in a formatted table with cumulative totals
    public static void showMenu() {
        Scanner sc = new Scanner(System.in);
        if (FileManager.sales_history == null || FileManager.sales_history.size() <= 1) {
            System.out.println("No sales records available.");
            return;
        }

        String[] header = FileManager.sales_history.get(0);

        int dateCol = findColumnIndex(header, new String[]{"date", "transaction_date", "transactiondate"});
        int timeCol = findColumnIndex(header, new String[]{"time"});
        int empCol = findColumnIndex(header, new String[]{"employee_name", "employee", "employee_id", "employeeid"});
        int custCol = findColumnIndex(header, new String[]{"customer_name", "customer", "cust_name"});
        int modelCol = findColumnIndex(header, new String[]{"model", "model_name"});
        int qtyCol = findColumnIndex(header, new String[]{"quantity", "qty"});
        int totalCol = findColumnIndex(header, new String[]{"total", "amount", "total_price", "totalprice"});
        int methodCol = findColumnIndex(header, new String[]{"transaction_method", "method", "payment_method"});

        final int DATE_COL = (dateCol == -1) ? 0 : dateCol;
        final int TIME_COL = (timeCol == -1) ? 1 : timeCol;
        final int EMP_COL = (empCol == -1) ? 3 : empCol;
        final int CUST_COL = (custCol == -1) ? 4 : custCol;
        final int MODEL_COL = (modelCol == -1) ? 5 : modelCol;
        final int QTY_COL = (qtyCol == -1) ? 6 : qtyCol;
        final int TOTAL_COL = (totalCol == -1) ? 8 : totalCol;
        final int METHOD_COL = (methodCol == -1) ? 9 : methodCol; 

        System.out.println("=== Filter & Sort Sales History ===");
        System.out.print("Enter start date (YYYY-MM-DD): ");
        String startInput = sc.nextLine().trim();
        System.out.print("Enter end date (YYYY-MM-DD): ");
        String endInput = sc.nextLine().trim();

        LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = LocalDate.parse(startInput, DateTimeFormatter.ISO_LOCAL_DATE);
            endDate = LocalDate.parse(endInput, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            System.out.println("Invalid date format. Use YYYY-MM-DD.");
            return;
        }

        if (startDate.isAfter(endDate)) {
            LocalDate tmp = startDate; startDate = endDate; endDate = tmp;
        }

        List<String[]> filtered = new ArrayList<>();
        double totalSum = 0.0;

        for (int i = 1; i < FileManager.sales_history.size(); i++) {
            String[] row = FileManager.sales_history.get(i);
            if (row == null) continue;
            String dateStr = (row.length > DATE_COL) ? row[DATE_COL] : "";
            LocalDate rowDate;
            try {
                rowDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (Exception e) {
                continue; 
            }
            if ((rowDate.isEqual(startDate) || rowDate.isAfter(startDate)) &&
                    (rowDate.isEqual(endDate) || rowDate.isBefore(endDate))) {
                filtered.add(row);
                String tot = (row.length > TOTAL_COL) ? row[TOTAL_COL] : "0";
                totalSum += parseDoubleSafe(tot);
            }
        }

        if (filtered.isEmpty()) {
            System.out.println("No transactions found in the specified date range.");
            return;
        }

        System.out.println("Sort by:\n1. Date (asc) 2. Total Price (asc) 3. Total Price (desc) 4. Quantity (asc) 5. Quantity (desc) 6. Customer (A-Z) 7. No sort");
        System.out.print("> ");
        int sel = 9;
        try { sel = Integer.parseInt(sc.nextLine()); } catch (Exception ignored) {}

        Comparator<String[]> comparator = null;
        switch (sel) {
            case 1: comparator = Comparator.comparing((String[] r) -> parseDateOrMin(get(r, DATE_COL))); break;
            case 2: comparator = Comparator.comparingDouble((String[] r) -> parseDoubleSafe(get(r, TOTAL_COL))); break;
            case 3: comparator = Comparator.comparingDouble((String[] r) -> parseDoubleSafe(get(r, TOTAL_COL))).reversed(); break;
            case 4: comparator = Comparator.comparingDouble((String[] r) -> parseDoubleSafe(get(r, QTY_COL))); break;
            case 5: comparator = Comparator.comparingDouble((String[] r) -> parseDoubleSafe(get(r, QTY_COL))).reversed(); break;
            case 6: comparator = Comparator.comparing((String[] r) -> get(r, CUST_COL).toLowerCase(Locale.ROOT)); break;
            default: comparator = null; break;
        }

        if (comparator != null) Collections.sort(filtered, comparator);

        System.out.printf("%-12s %-10s %-20s %-20s %-15s %-6s %-12s %-15s%n",
                "Date", "Time", "Employee", "Customer", "Model", "Qty", "Total Price", "Method");
        System.out.println("---------------------------------------------------------------------------------------------");

        for (String[] row : filtered) {
            String d = get(row, DATE_COL);
            String t = get(row, TIME_COL);
            String emp = get(row, EMP_COL);
            String cust = get(row, CUST_COL);
            String model = get(row, MODEL_COL);
            String qty = get(row, QTY_COL);
            String tot = get(row, TOTAL_COL);
            String method = get(row, METHOD_COL);

            System.out.printf("%-12s %-10s %-20s %-20s %-15s %-6s %-12s %-15s%n",
                    d, t, emp, cust, model, qty, tot, method);
        }

        System.out.println();
        System.out.printf("Total cumulative sales for range %s to %s: RM %.2f%n", startDate.toString(), endDate.toString(), totalSum);
        System.out.println("Press Enter to continue...");
        sc.nextLine();
    }

    //Safely retrieves data from a row
    private static String get(String[] row, int idx) {
        if (row == null || idx < 0) return "";
        return (row.length > idx && row[idx] != null) ? row[idx] : "";
    }

    //Dynamically finds column index based on header name
    private static int findColumnIndex(String[] header, String[] candidates) {
        if (header == null) return -1;
        for (int i = 0; i < header.length; i++) {
            String h = header[i].toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
            for (String c : candidates) {
                String cc = c.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
                if (h.equals(cc) || h.contains(cc)) return i;
            }
        }
        return -1;
    }

    //Safely parses numeric values
    private static double parseDoubleSafe(String s) {
        if (s == null) return 0.0;
        try { return Double.parseDouble(s); } catch (Exception e) {
            String cleaned = s.replaceAll("[^0-9.-]", "");
            try { return Double.parseDouble(cleaned); } catch (Exception ex) { return 0.0; }
        }
    }

    private static LocalDate parseDateOrMin(String s) {
        try { return LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE); } catch (Exception e) { return LocalDate.MIN; }
    }
} 


