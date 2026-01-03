import java.sql.SQLOutput;
import java.util.*;
public class Manager extends Employee {
    public Manager(String[] a){
        super(a);
    }
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
            System.out.println("=== Manager Options ===");
            System.out.println("7. Register New Staff");
            System.out.println("8. View Performance Metrics");
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
                case 7:
                    register_new_staff();
                    break;
                case 8:
                    view_performance();
                    break;
            }
        }
    }

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
        String new_role = "Employee";
        String new_outlet_id = this.outlet_id;
        String[] new_employee = {new_name, new_password, new_id, new_role, new_job_type, new_outlet_id};
        System.out.println("Employee Registered Successfully!");
        FileManager.employee_list.add(new_employee);
    }

    public void view_performance(){
        System.out.println("view performance");
    }

}
