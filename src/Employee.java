import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.*;

public class Employee {

    String[] attendance = new String[7];
    String name = "";
    String id = "";
    String password = "";
    String outlet_id = "";
    String role = "";
    String job_type = "";
    Instant start;
    Instant end;
    Duration duration;
    Boolean clock_in = false;
    String branch = "";

    public Employee(String[] a) {
        this.name = a[0];
        this.password = a[1];
        this.id = a[2];
        this.role = a[3];
        this.job_type = a[4];
        this.outlet_id = a[5];
        this.branch = a[5];
    }

    public void show_menu() {
        while (true) {
            System.out.println("==== Employee Options ====");
            System.out.println("0. Log-Out");
            System.out.println("1. Clock-In.");
            System.out.println("2. Clock-Out");
            System.out.println("3. Sales Record System");
            System.out.println("4. Search Information");
            System.out.println("5. Morning Stock Count");
            System.out.println("6. Night Stock Count");
            System.out.println("7. Stock In / Stock Out");
            System.out.println("8. Edit Information");
            System.out.println("9. Filter & Sort Sales History");
            Scanner sc = new Scanner(System.in);
            int option = sc.nextInt();
            sc.nextLine(); // consume newline
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
            }
        }

    }

    public void clock_in() {
        start = Instant.now();
        LocalDate date = LocalDate.now();
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
        String formattedTime = now.format(formatter);
        System.out.println("=== Attendance Clock In ===");
        System.out.println("Employee ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Outlet: " + outlet_id);
        System.out.println("");
        System.out.println("Clock in successful!");
        System.out.println("Date: " + date);
        System.out.println("Time: " + formattedTime);
        attendance[0] = this.id;
        attendance[1] = this.name;
        attendance[2] = String.valueOf(date);
        attendance[3] = formattedTime;
        attendance[5] = this.outlet_id;
        clock_in = true;
    }

    public void clock_out() {
        if(clock_in) {

            end = Instant.now();
            duration = Duration.between(start, end);
            long dur = duration.getSeconds();
            double hours = dur / 3600.0;
            String formattedHours = String.format("%.1f", hours);
            LocalTime now = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
            String formattedTime = now.format(formatter);
            attendance[4] = formattedTime;
            attendance[6] = formattedHours;
            System.out.println("=== Attendance Clock Out ===");
            System.out.println("Employee ID: " + id);
            System.out.println("Name: " + name);
            System.out.println("Outlet: " + outlet_id);
            System.out.println("");
            System.out.println("Clock out successful!");
            System.out.println("Date: " + attendance[2]);
            System.out.println("Time: " + formattedTime);
            System.out.println("Total Hours Worked: " + formattedHours);
            clock_in = false;
            FileManager.attendance.add(attendance);
        }
        else{
            System.out.println("You have not clocked in yet");
        }
    }


    public void sales_record() {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Record New Sale ===");

        LocalDate date = LocalDate.now();
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("hh:mm a");
        String time = LocalTime.now().format(tf);

        System.out.print("Customer Name (X to cancel): ");
        String customer = sc.nextLine().trim();

        if (customer.equalsIgnoreCase("X")) {
            System.out.println("Sale recording cancelled.");
            return;
        }

        String model;
        double price = 0.0;
        String[] foundModel = null;
        int modelIndex = -1;

        while (foundModel == null) {
            System.out.print("Enter Model (X to cancel): ");
            model = sc.nextLine().trim();

            if (model.equalsIgnoreCase("X")) {
                System.out.println("Sale recording cancelled.");
                return;
            }

            for (int i = 0; i < FileManager.models.size(); i++) {
                String[] modelData = FileManager.models.get(i);
                if (modelData.length > 0 && modelData[0].equalsIgnoreCase(model)) {
                    foundModel = modelData;
                    modelIndex = i;
                    price = Double.parseDouble(modelData[1]);
                    System.out.println("Model found! Unit Price: RM " + price);
                    break;
                }
            }

            if (foundModel == null) {
                System.out.println("Error: Model \"" + model + "\" not found.");
            }
        }


        System.out.println("\n=== Stock Information for " + foundModel[0] + " ===");
        System.out.println("Unit Price: RM " + price);
        System.out.println("1. KLCC: " + foundModel[3] + " units");
        System.out.println("2. MidValley: " + foundModel[4] + " units");
        System.out.println("3. Lalaport: " + foundModel[5] + " units");
        System.out.println("4. Nu Sentral: " + foundModel[6] + " units");
        System.out.println("5. Pavilion KL: " + foundModel[7] + " units");
        System.out.println("6. MyTown: " + foundModel[8] + " units");
        System.out.println("7. KL East: " + foundModel[9] + " units");

        int outletChoice = 0;
        int outletStockIndex = 0;
        String selectedOutlet = "";


        while (outletChoice < 1 || outletChoice > 7) {
            System.out.print("Select outlet (1-7) or X to cancel: ");
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("X")) {
                System.out.println("Sale recording cancelled.");
                return;
            }

            try {
                outletChoice = Integer.parseInt(input);

                switch (outletChoice) {
                    case 1: outletStockIndex = 3; selectedOutlet = "KLCC"; break;
                    case 2: outletStockIndex = 4; selectedOutlet = "MidValley"; break;
                    case 3: outletStockIndex = 5; selectedOutlet = "Lalaport"; break;
                    case 4: outletStockIndex = 6; selectedOutlet = "Nu Sentral"; break;
                    case 5: outletStockIndex = 7; selectedOutlet = "Pavilion KL"; break;
                    case 6: outletStockIndex = 8; selectedOutlet = "MyTown"; break;
                    case 7: outletStockIndex = 9; selectedOutlet = "KL East"; break;
                    default:
                        System.out.println("Invalid outlet number.");
                        outletChoice = 0;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        int currentStock = Integer.parseInt(foundModel[outletStockIndex]);
        System.out.println("Current stock at " + selectedOutlet + ": " + currentStock);

        int qty;
        while (true) {
            System.out.print("Enter Quantity (X to cancel): ");
            String qtyInput = sc.nextLine().trim();

            if (qtyInput.equalsIgnoreCase("X")) {
                System.out.println("Sale recording cancelled.");
                return;
            }

            try {
                qty = Integer.parseInt(qtyInput);

                if (qty <= 0) {
                    System.out.println("Quantity must be greater than 0.");
                } else if (qty > currentStock) {
                    System.out.println("Insufficient stock. Available: " + currentStock);
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        int newStock = currentStock - qty;
        foundModel[outletStockIndex] = String.valueOf(newStock);
        FileManager.models.set(modelIndex, foundModel);
        FileManager.modelDataModified = true;

        System.out.print("Transaction Method (Cash/Card/E-wallet) (X to cancel): ");
        String method = sc.nextLine().trim();

        if (method.equalsIgnoreCase("X")) {
            System.out.println("Sale recording cancelled.");
            return;
        }

        double total = qty * price;

        String[] sale = new String[10];
        sale[0] = date.toString();
        sale[1] = time;
        sale[2] = this.id;
        sale[3] = this.name;
        sale[4] = customer;
        sale[5] = foundModel[0];
        sale[6] = String.valueOf(qty);
        sale[7] = String.valueOf(price);
        sale[8] = String.valueOf(total);
        sale[9] = method;

        FileManager.sales_history.add(sale);
        FileManager.Data_Saver();
        FileManager.saveLatestSaleReceipt();

        // =========================
        // SUMMARY
        // =========================
        System.out.println("\n=== Sale Summary ===");
        System.out.println("Customer: " + customer);
        System.out.println("Model: " + foundModel[0]);
        System.out.println("Quantity: " + qty);
        System.out.println("Unit Price: RM " + price);
        System.out.println("Total: RM " + total);
        System.out.println("Payment Method: " + method);
        System.out.println("Outlet: " + selectedOutlet);
        System.out.println("Stock updated: " + currentStock + " → " + newStock);
        System.out.println("Sale recorded successfully.");
    }

    public void search_item() {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Search Options ===");
        System.out.println("1. Search Stock Information");
        System.out.println("2. Search Sales Information");
        System.out.print("Choose option (1 or 2): ");

        int option;
        try {
            option = sc.nextInt();
            sc.nextLine();
        } catch (Exception e) {
            System.out.println("Invalid input.");
            sc.nextLine();
            return;
        }


        if (option == 1) {
            System.out.print("Search Model Name: ");
            String searchModel = sc.nextLine().trim();

            System.out.println("Searching...");

            boolean found = false;

            for (String[] stock : FileManager.models) {
                if (stock.length >= 9 && stock[0].equalsIgnoreCase(searchModel)) {

                    System.out.println("\n=== Stock Information ===");
                    System.out.println("Model: " + stock[0]);
                    System.out.println("Unit Price: RM " + stock[1]);

                    System.out.println("\nStock by Outlet:");
                    System.out.println("KLCC: " + stock[3] + " units");
                    System.out.println("MidValley: " + stock[4] + " units");
                    System.out.println("Lalaport: " + stock[5] + " units");
                    System.out.println("Nu Sentral: " + stock[6] + " units");
                    System.out.println("Pavilion KL: " + stock[7] + " units");
                    System.out.println("MyTown: " + stock[8] + " units");
                    System.out.println("KL East: " + stock[9] + " units");

                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("Error: Model \"" + searchModel + "\" not found in stock records.");
            }
        }

        else if (option == 2) {
            System.out.println("=== Search Sales Information ===");
            System.out.print("Search keyword (date, customer name, or model name): ");
            String keyword = sc.nextLine().trim();

            System.out.println("Searching...");

            int recordsFound = 0;

            for (String[] sale : FileManager.sales_history) {
                if (sale.length < 10 || sale[0].equalsIgnoreCase("Date")) {
                    continue;
                }

                String date = sale[0].trim();
                String time = sale[1].trim();
                String customerName = sale[4].trim();
                String modelName = sale[5].trim();
                String quantity = sale[6].trim();
                String total = sale[8].trim();
                String transactionMethod = sale[9].trim();
                String employeeName = sale[3].trim();

                if (date.contains(keyword) ||
                        customerName.toLowerCase().contains(keyword.toLowerCase()) ||
                        modelName.toLowerCase().contains(keyword.toLowerCase())) {

                    System.out.println("\nSales Record Found:");
                    System.out.println("Date: " + date + " Time: " + time);
                    System.out.println("Customer: " + customerName);
                    System.out.println("Item: " + modelName + " | Quantity: " + quantity);
                    System.out.println("Total: RM " + total);
                    System.out.println("Transaction Method: " + transactionMethod);
                    System.out.println("Employee: " + employeeName);
                    System.out.println("Status: Transaction verified.");

                    recordsFound++;
                }
            }

            if (recordsFound > 0) {
                System.out.println("\nTotal records found: " + recordsFound);
            } else {
                System.out.println("No sales record found for keyword: " + keyword);
            }
        }
        else {
            System.out.println("Invalid option!");
        }
    }

    public void morning_stock_count() {
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd");

        DateTimeFormatter timeFormatter =
                DateTimeFormatter.ofPattern("hh:mm a");


        System.out.println("=== Morning Stock Count ===");
        System.out.println("Date: " + now.format(dateFormatter));
        System.out.println("Time: " + now.format(timeFormatter).toLowerCase());
        runStockCount("Morning");
    }

    public void night_stock_count() {
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd");

        DateTimeFormatter timeFormatter =
                DateTimeFormatter.ofPattern("hh:mm a");
        String todayDate = now.format(dateFormatter);// new
        System.out.println("=== Night Stock Count ===");
        System.out.println("Date: " + now.format(dateFormatter));
        System.out.println("Time: " + now.format(timeFormatter).toLowerCase());
        runStockCount("Night");
        System.out.println("\n[SYSTEM] Stock count finished. Initiating Auto-Email to HQ..."); // new
        String reportFileName = FileManager.createDailyReportFile(todayDate);

        if (reportFileName != null) { // new
            String hqEmail = "25063087@siswa.um.edu.my";
            EmailService.sendDailyReport(hqEmail, reportFileName, todayDate);

            System.out.println("[SYSTEM] Closing sequence complete. You may log out.");
        } else {
            System.out.println("[ERROR] Could not generate report. Email was not sent.");
        }

    }

    private void runStockCount(String shift) {


        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd");

        DateTimeFormatter timeFormatter =
                DateTimeFormatter.ofPattern("hh:mm a");




        Scanner sc = new Scanner(System.in);

        if (this.branch == null) {
            System.out.println("Error: Employee branch not set.");
            return;
        }

        String branchName = this.outlet_id;

        int totalModels = 0;
        int correctTally = 0;
        int mismatches = 0;

        int stockColumn;

        switch (branchName) {
            case "KLCC": stockColumn = 3; break;
            case "MidValley": stockColumn = 4; break;
            case "Lalaport": stockColumn = 5; break;
            case "Nu_Sentral": stockColumn = 6; break;
            case "PavilionKL": stockColumn = 7; break;
            case "MyTown": stockColumn = 8; break;
            case "KLEast": stockColumn = 9; break;
            default:
                System.out.println("Invalid branch.");
                return;
        }

        for (int i = 1; i < FileManager.models.size(); i++) {

            String[] item = FileManager.models.get(i);

            String modelName = item[0];
            int storeRecord = Integer.parseInt(item[stockColumn]);

            System.out.print("Model: " + modelName + " - Counted: ");
            int counted = sc.nextInt();

            System.out.println("Store Record: " + storeRecord);

            totalModels++;

            if (counted == storeRecord) {
                System.out.println("Stock tally correct.");
                correctTally++;
            } else {
                int diff = Math.abs(counted - storeRecord);
                System.out.println("! Mismatch detected (" + diff + " unit difference)");
                mismatches++;
            }

            System.out.println();
        }

        displaySummary(shift, totalModels, correctTally, mismatches);
    }

    //Stock in / Stock out menu
    public void stock_in_out_menu() {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Stock Movement ===");
        System.out.println("1. Stock In");
        System.out.println("2. Stock Out");
        System.out.print("Choose option: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {
            processStockMovement(true);
        } else if (choice == 2) {
            processStockMovement(false);
        } else {
            System.out.println("Invalid option.");
        }
    }

    //Process stock Movement
    private void processStockMovement(boolean isStockIn) {
        Scanner sc = new Scanner(System.in);

        String from = selectOutlet(sc, "Select FROM outlet:");
        String to = selectOutlet(sc, "Select TO outlet:");

        System.out.print("How many different models? ");
        int modelCount = sc.nextInt();
        sc.nextLine();

        List<String[]> movements = new ArrayList<>();

        for (int i = 0; i < modelCount; i++) {

            String model = selectModel(sc);

            System.out.print("Quantity: ");
            int qty = sc.nextInt();
            sc.nextLine();

            movements.add(new String[]{model, String.valueOf(qty)});
        }

        FileManager.updateStockMovement(movements, from, to);

        writeReceipt(isStockIn ? "Stock In" : "Stock Out", from, to, movements);

        System.out.println("Stock movement recorded successfully.");
    }


    //write stockin&out receipt (one receipt per transaction)
    private void writeReceipt(String type, String from, String to,
                              List<String[]> movements) {

        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        String timestamp = time.format(DateTimeFormatter.ofPattern("HHmmss"));
        String safeType = type.replace(" ", "");

        String fileName =
                "Files/StockIn_StockOut_Receipts/receipts_" + date + "_" + timestamp + "_" + safeType + ".txt";

        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {

            pw.println("=== " + type + " ===");
            pw.println("Date: " + date);
            pw.println("Time: " + time.format(DateTimeFormatter.ofPattern("hh:mm a")));
            pw.println("From: " + from);
            pw.println("To: " + to);
            pw.println(role +": " + name);
            pw.println("Models:");

            int total = 0;
            for (String[] m : movements) {
                pw.println("- " + m[0] + " (Quantity: " + m[1] + ")");
                total += Integer.parseInt(m[1]);
            }

            pw.println("Total Quantity: " + total);
            pw.println("--------------------------------");

            System.out.println("Receipt generated: " + fileName);

        } catch (Exception e) {
            System.out.println("Error writing receipt.");
        }
    }

    //Select model by index
    private String selectModel(Scanner sc) {

        System.out.println("Select Model:");

        // models list already loaded in FileManager
        for (int i = 1; i < FileManager.models.size(); i++) {
            System.out.println(i + ". " + FileManager.models.get(i)[0]);
        }

        System.out.print("Enter choice: ");
        int choice = sc.nextInt();
        sc.nextLine();

        return FileManager.models.get(choice)[0];
    }

    //Select outlet by index
    private String selectOutlet(Scanner sc, String title) {

        String[] outlets = {
                "HQ",
                "KLCC",
                "MidValley",
                "Lalaport",
                "Nu_Sentral",
                "PavilionKL",
                "MyTown",
                "KLEast"
        };

        System.out.println(title);
        for (int i = 0; i < outlets.length; i++) {
            System.out.println((i + 1) + ". " + outlets[i]);
        }

        System.out.print("Enter choice: ");
        int choice = sc.nextInt();
        sc.nextLine();

        return outlets[choice - 1];
    }

    //Get CSV column index
    private int getOutletColumnIndex(String[] columns) {

        for (int i = 0; i < columns.length; i++) {
            if (columns[i].equalsIgnoreCase(outlet_id + "_Stock")) {
                return i;
            }
        }

        // fallback to safety
        return 2;
    }

    private void displaySummary(
            String shift,
            int totalModels,
            int correctTally,
            int mismatches
    )
    {
        System.out.println("=== " + shift + " Stock Summary ===");
        System.out.println("Total Models Checked: " + totalModels);
        System.out.println("Tally Correct: " + correctTally);
        System.out.println("Mismatches: " + mismatches);

        if (mismatches > 0) {
            if (shift.equalsIgnoreCase("Night")) {
                System.out.println("Warning: Please verify stock before closing.");
            } else {
                System.out.println("Warning: Please verify stock.");
            }
        }

        System.out.println(shift + " stock count completed.");
    }


    public Employee(String name, String branch) {
        this.name = name;
        this.branch = branch;
    }


}


