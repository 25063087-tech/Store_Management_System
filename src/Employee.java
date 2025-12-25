import java.util.Scanner;

public class Employee {
    String name = "";
    String id = "";
    String password = "";
    String outlet_id = "";
    String role = "";
    String job_type = "";

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
        System.out.println("clock in");
    }

    public void clock_out() {
        System.out.println("clock out");
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
