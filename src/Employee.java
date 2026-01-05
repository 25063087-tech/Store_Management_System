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
            Scanner sc = new Scanner(System.in);
            int option = sc.nextInt();
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
        System.out.println("record sales");
    }

    public void search_item() {
        System.out.println("search item");
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
