import java.util.*;
public class EmployeeManager{

    Employee current_user;
    Scanner sc = new Scanner(System.in);

    public EmployeeManager(){
        while(true) {
            System.out.println("=== Employee Login ===");
            System.out.print("Enter User ID: ");
            String id = sc.nextLine();
            System.out.print("Enter Password: ");
            String password = sc.nextLine();
            login_checker(id, password);
        }
    }

    // Validates employee ID and password against stored records
    // Creates Employee or Manager object based on role
    public void login_checker(String id, String password){
        if (FileManager.employee_list == null || FileManager.employee_list.size() <= 1) {
            System.out.println("Login Failed: No employee records found.");
            return;
        }
        boolean flag = false;
        String[] found = null;
        for (int i = 1; i < FileManager.employee_list.size(); i++) {
            String[] row = FileManager.employee_list.get(i);
            if (row == null || row.length < 3) continue; // need at least id and password
            String rowId = (row.length > 2 && row[2] != null) ? row[2].trim() : "";
            String rowPass = (row.length > 1 && row[1] != null) ? row[1].trim() : "";
            if (rowId.equals(id.trim()) && rowPass.equals(password.trim())) {
                flag = true;
                // normalize to 6 columns to avoid index issues
                found = new String[6];
                for (int j = 0; j < 6; j++) {
                    found[j] = (j < row.length && row[j] != null) ? row[j].trim() : "";
                }
                break;
            }
        }

        if (flag && found != null) {
            System.out.println("Login Successful!");
            System.out.println("Welcome, " + found[0] + " (" + found[5] + ")");
            if ("Manager".equals(found[3])) {
                current_user = new Manager(found);
            } else {
                current_user = new Employee(found);
            }
            current_user.show_menu();
        } else {
            System.out.println("Login Failed: Invalid User ID or Password.");
        }
    }
}
