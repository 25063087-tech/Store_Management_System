import java.util.Scanner;
import java.util.Locale;

public class EditInformation {

    // Displays edit options menu for stock and sales records
    public static void showMenu(Employee user) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("=== Edit Information ===");
            System.out.println("1. Edit Stock Information");
            System.out.println("2. Edit Sales Information");
            System.out.println("0. Back");
            System.out.print("> ");
            String choice = sc.nextLine().trim();
            if (choice.equals("0")) return;
            if (choice.equals("1")) {
                editStockInfo(user);
            } else if (choice.equals("2")) {
                editSalesInfo();
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
    }

    // Edits stock information for a selected model
    // Automatically detects outlet-specific stock column
    public static void editStockInfo(Employee user) {
        Scanner sc = new Scanner(System.in);
        if (FileManager.models.size() <= 1) {
            System.out.println("No models available to edit.");
            return;
        }
        System.out.print("Enter Model Name: ");
        String modelName = sc.nextLine();
        int foundIdx = -1;
        for (int i = 1; i < FileManager.models.size(); i++) {
            String[] row = FileManager.models.get(i);
            if (row.length > 0 && row[0].equalsIgnoreCase(modelName)) {
                foundIdx = i;
                break;
            }
        }
        if (foundIdx == -1) {
            System.out.println("Model not found. Available models:");
            for (int i = 1; i < FileManager.models.size(); i++) {
                String[] row = FileManager.models.get(i);
                if (row.length > 0) System.out.println("- " + row[0]);
            }
            return;
        }
        String[] header = FileManager.models.get(0);
        String[] row = FileManager.models.get(foundIdx);


        int outletCol = -1;
        if (user != null && user.outlet_id != null && header != null) {
            for (int i = 0; i < header.length; i++) {
                if (header[i].equalsIgnoreCase(user.outlet_id)) {
                    outletCol = i;
                    break;
                }
            }
        }

        if (outletCol != -1) {
            String current = (row.length > outletCol) ? row[outletCol] : "";
            System.out.println("=== Edit Stock Information ===");
            System.out.println("Model: " + row[0]);
            System.out.println("Current Stock: " + current);
            System.out.print("Enter New Stock Value: ");
            String newVal = sc.nextLine();
            row = ensureRowLength(row, outletCol + 1);
            row[outletCol] = newVal;
            FileManager.models.set(foundIdx, row);
            FileManager.Data_Saver();
            System.out.println("Stock information updated successfully.");
            return;
        }

        System.out.println("Editing model: " + row[0]);
        for (int i = 0; i < row.length; i++) {
            String colName = (header != null && header.length > i) ? header[i] : ("col" + i);
            String value = (row.length > i) ? row[i] : "";
            System.out.println(i + ". " + colName + " : " + value);
        }
        System.out.print("Enter outlet number to edit (or -1 to cancel): ");
        int col;
        try {
            col = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid input. Cancelled.");
            return;
        }
        if (col < 0 || col >= Math.max(row.length, (header != null ? header.length : 0))) {
            System.out.println("Cancelled or invalid column.");
            return;
        }
        System.out.print("Enter new value: ");
        String newVal = sc.nextLine();
        row = ensureRowLength(row, col + 1);
        row[col] = newVal;
        FileManager.models.set(foundIdx, row);
        FileManager.Data_Saver();
        System.out.println("Stock information updated successfully.");
    }

    // Edits existing sales records and updates corresponding receipt
    public static void editSalesInfo() {
        Scanner sc = new Scanner(System.in);
        if (FileManager.sales_history.size() <= 1) {
            System.out.println("No sales records available to edit.");
            return;
        }
        System.out.print("Enter Transaction Date: ");
        String date = sc.nextLine().trim();
        System.out.print("Enter Customer Name: ");
        String customer = sc.nextLine().trim();

        String[] header = FileManager.sales_history.get(0);

        int dateCol = findColumnIndex(header, new String[]{"date", "transaction_date", "transactiondate"});
        int custCol = findColumnIndex(header, new String[]{"customer_name", "customer", "cust_name"});
        int modelCol = findColumnIndex(header, new String[]{"model", "model_name"});
        int qtyCol = findColumnIndex(header, new String[]{"quantity", "qty"});
        int totalCol = findColumnIndex(header, new String[]{"total", "amount", "total_price"});
        int methodCol = findColumnIndex(header, new String[]{"transaction_method", "method", "payment_method"});

        if (dateCol == -1) dateCol = 0;
        if (custCol == -1) custCol = 1;
        if (modelCol == -1) modelCol = 2;
        if (qtyCol == -1) qtyCol = 3;
        if (totalCol == -1) totalCol = 4;
        if (methodCol == -1) methodCol = 5;

        int foundIdx = -1;
        for (int i = 1; i < FileManager.sales_history.size(); i++) {
            String[] row = FileManager.sales_history.get(i);
            String rDate = (row.length > dateCol) ? row[dateCol] : "";
            String rCust = (row.length > custCol) ? row[custCol] : "";
            if (rDate.equalsIgnoreCase(date) && rCust.equalsIgnoreCase(customer)) {
                foundIdx = i;
                break;
            }
        }
        if (foundIdx == -1) {
            System.out.println("Sales record not found.");
            return;
        }

        String[] row = FileManager.sales_history.get(foundIdx);
        String rModel = (row.length > modelCol) ? row[modelCol] : "";
        String rQty = (row.length > qtyCol) ? row[qtyCol] : "";
        String rTotal = (row.length > totalCol) ? row[totalCol] : "";
        String rMethod = (row.length > methodCol) ? row[methodCol] : "";

        System.out.println("=== Edit Sales Information ===");
        System.out.println("Sales Record Found:");
        System.out.println("Model: " + rModel + " Quantity: " + rQty);
        System.out.println("Total: " + rTotal);
        System.out.println("Transaction Method: " + rMethod);
        System.out.println();
        System.out.println("Select number to edit:");
        System.out.println("1. Name  2. Model  3. Quantity  4. Total  ");
        System.out.println("5. Transaction Method");
        System.out.print("> ");
        int sel;
        try {
            sel = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid selection. Cancelled.");
            return;
        }
        int targetCol = -1;
        switch (sel) {
            case 1: targetCol = custCol; break;
            case 2: targetCol = modelCol; break;
            case 3: targetCol = qtyCol; break;
            case 4: targetCol = totalCol; break;
            case 5: targetCol = methodCol; break;
            default:
                System.out.println("Invalid selection.");
                return;
        }
        System.out.print("Enter New ");
        switch (sel) {
            case 1: System.out.print("Name: "); break;
            case 2: System.out.print("Model: "); break;
            case 3: System.out.print("Quantity: "); break;
            case 4: System.out.print("Total: "); break;
            case 5: System.out.print("Transaction Method: "); break;
        }
        String newVal = sc.nextLine();
        System.out.print("Confirm Update? (Y/N): ");
        String confirm = sc.nextLine().trim().toUpperCase(Locale.ROOT);
        if (!confirm.equals("Y")) {
            System.out.println("Cancelled.");
            return;
        }
        row = ensureRowLength(row, targetCol + 1);
        row[targetCol] = newVal;
        FileManager.sales_history.set(foundIdx, row);

        FileManager.updateReceiptForSale(foundIdx);

        FileManager.Data_Saver();

        System.out.println("Sales information and receipt updated successfully.");

    }

    private static int findColumnIndex(String[] header, String[] candidates) {
        if (header == null) return -1;
        for (int i = 0; i < header.length; i++) {
            String h = header[i].toLowerCase(Locale.ROOT);
            for (String c : candidates) {
                if (h.contains(c)) return i;
            }
        }
        return -1;
    }

    private static String[] ensureRowLength(String[] row, int minLen) {
        if (row.length >= minLen) return row;
        String[] newRow = new String[minLen];
        for (int i = 0; i < newRow.length; i++) {
            if (i < row.length) newRow[i] = row[i]; else newRow[i] = "";
        }
        return newRow;
    }
}