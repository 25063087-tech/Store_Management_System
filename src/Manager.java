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
            System.out.println("7. Register New Staff");
            System.out.println("8. View Performance Metrics");
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
        System.out.println("rns");
    }

    public void view_performance(){
        System.out.println("view performance");
    }

}
