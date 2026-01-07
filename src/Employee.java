import java.time.format.DateTimeFormatter;
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
            System.out.println("3. Sales Record");
            System.out.println("4. Search Item");
            System.out.println("5. Morning Stock Count");
            System.out.println("6. Night Stock Count");
            System.out.println("7. Edit Stock Info");
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
                    EditInformation.showMenu(this);
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
            System.out.println("=== Attendance Clock Out ===");
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
            System.out.println("Clock in successful!");
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

        System.out.print("Customer Name: ");
        String customer = sc.nextLine();

        String model = "";
        double price = 0.0;
        String[] foundModel = null;
        int modelIndex = -1;

        while (foundModel == null) {
            System.out.print("Enter Model: ");
            model = sc.nextLine().trim();

            for (int i = 0; i < FileManager.models.size(); i++) {
                String[] modelData = FileManager.models.get(i);
                if (modelData.length > 0 && modelData[0].equalsIgnoreCase(model)) {
                    foundModel = modelData;
                    modelIndex = i;
                    price = Double.parseDouble(modelData[1]); // 自动从库存获取价格
                    System.out.println("Model found! Unit Price: RM " + price);
                    break;
                }
            }

            if (foundModel == null) {
                System.out.println("Error: Model \"" + model + "\" not found in stock records.");
                System.out.println("Please enter a valid model name or type 'cancel' to return to menu.");

                String response = sc.nextLine();
                if (response.equalsIgnoreCase("cancel")) {
                    System.out.println("Sale recording cancelled.");
                    return;
                }
            }
        }

        System.out.println("\n=== Stock Information for " + model + " ===");
        System.out.println("Unit Price: RM " + price);
        System.out.println("Stock by Outlet:");
        System.out.println("1. KLCC: " + foundModel[2] + " units");
        System.out.println("2. MidValley: " + foundModel[3] + " units");
        System.out.println("3. Lalaport: " + foundModel[4] + " units");
        System.out.println("4. Nu Sentral: " + foundModel[5] + " units");
        System.out.println("5. Pavilion KL: " + foundModel[6] + " units");
        System.out.println("6. MyTown: " + foundModel[7] + " units");
        System.out.println("7. KL East: " + foundModel[8] + " units");

        int outletChoice = 0;
        int outletStockIndex = 0;
        String selectedOutlet = "";

        while (outletChoice < 1 || outletChoice > 7) {
            System.out.print("\nSelect outlet (1-7): ");
            try {
                outletChoice = sc.nextInt();
                sc.nextLine();

                if (outletChoice < 1 || outletChoice > 7) {
                    System.out.println("Invalid selection. Please enter a number between 1 and 7.");
                } else {

                    switch(outletChoice) {
                        case 1:
                            outletStockIndex = 2;
                            selectedOutlet = "KLCC";
                            break;
                        case 2:
                            outletStockIndex = 3;
                            selectedOutlet = "MidValley";
                            break;
                        case 3:
                            outletStockIndex = 4;
                            selectedOutlet = "Lalaport";
                            break;
                        case 4:
                            outletStockIndex = 5;
                            selectedOutlet = "Nu Sentral";
                            break;
                        case 5:
                            outletStockIndex = 6;
                            selectedOutlet = "Pavilion KL";
                            break;
                        case 6:
                            outletStockIndex = 7;
                            selectedOutlet = "MyTown";
                            break;
                        case 7:
                            outletStockIndex = 8;
                            selectedOutlet = "KL East";
                            break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number between 1 and 7.");
                sc.nextLine();
            }
        }

        int currentStock = Integer.parseInt(foundModel[outletStockIndex]);
        System.out.println("Current stock at " + selectedOutlet + ": " + currentStock + " units");

        int qty = 0;
        while (qty <= 0 || qty > currentStock) {
            System.out.print("Enter Quantity: ");
            try {
                qty = sc.nextInt();
                sc.nextLine();

                if (qty <= 0) {
                    System.out.println("Quantity must be greater than 0.");
                } else if (qty > currentStock) {
                    System.out.println("Error: Insufficient stock! Only " + currentStock + " units available at " + selectedOutlet);
                    System.out.println("Please enter a smaller quantity or type '0' to cancel.");

                    if (qty == 0) {
                        System.out.println("Sale recording cancelled.");
                        return;
                    }
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.nextLine();
            }
        }

        int newStock = currentStock - qty;
        foundModel[outletStockIndex] = String.valueOf(newStock);

        FileManager.models.set(modelIndex, foundModel);

        FileManager.modelDataModified = true;

        System.out.print("Transaction Method (Cash/Card/E-wallet): ");
        String method = sc.nextLine();

        double total = qty * price;

        String[] sale = new String[10];
        sale[0] = date.toString();
        sale[1] = time;
        sale[2] = this.id;
        sale[3] = this.name;
        sale[4] = customer;
        sale[5] = model;
        sale[6] = String.valueOf(qty);
        sale[7] = String.valueOf(price);
        sale[8] = String.valueOf(total);
        sale[9] = method;

        FileManager.sales_history.add(sale);

        System.out.println("\n=== Sale Summary ===");
        System.out.println("Customer: " + customer);
        System.out.println("Model: " + model);
        System.out.println("Quantity: " + qty);
        System.out.println("Unit Price: RM " + price);
        System.out.println("Total: RM " + total);
        System.out.println("Payment Method: " + method);
        System.out.println("Outlet: " + selectedOutlet);
        System.out.println("Stock updated: " + currentStock + " -> " + newStock);
        System.out.println("Sale recorded successfully.");
    }
    public void search_item() {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Search Stock Information ===");
        System.out.print("Search Model Name: ");
        String searchModel = sc.nextLine().trim();

        System.out.println("Searching...");

        boolean found = false;

        for (String[] stock : FileManager.models) {
            if (stock.length > 0 && stock[0].equalsIgnoreCase(searchModel)) {
                System.out.println("Model: " + stock[0]);
                System.out.println("Unit Price: RM " + stock[1]);
                System.out.println("Stock by Outlet:");
                System.out.print("KLCC: " + stock[2] + " ");
                System.out.print("MidValley: " + stock[3] + " ");
                System.out.print("Lalaport: " + stock[4] + " ");
                System.out.print("Nu Sentral: " + stock[5] + " ");
                System.out.print("Pavilion KL: " + stock[6] + " ");
                System.out.print("MyTown: " + stock[7] + " ");
                System.out.print("KL East: " + stock[8]);
                System.out.println();
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Error: Model \"" + searchModel + "\" not found in stock records.");
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


        System.out.println("=== Night Stock Count ===");
        System.out.println("Date: " + now.format(dateFormatter));
        System.out.println("Time: " + now.format(timeFormatter).toLowerCase());
        runStockCount("Night");

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
            case "KLCC": stockColumn = 2; break;
            case "MidValley": stockColumn = 3; break;
            case "Lalaport": stockColumn = 4; break;
            case "Nu Sentral": stockColumn = 5; break;
            case "Pavilion KL": stockColumn = 6; break;
            case "MyTown": stockColumn = 7; break;
            case "KL East": stockColumn = 8; break;
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

    private void displaySummary(
            String shift,
            int totalModels,
            int correctTally,
            int mismatches
    ) {
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
