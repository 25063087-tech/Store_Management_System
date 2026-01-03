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
    public Employee(String[] a) {
        this.name = a[0];
        this.password = a[1];
        this.id = a[2];
        this.role = a[3];
        this.job_type = a[4];
        this.outlet_id = a[5];
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
        System.out.println("msc");
    }

    public void night_stock_count() {
        System.out.println("nsc");
    }
}
