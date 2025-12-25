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

    public void login_checker(String id, String password){
        boolean flag = false;
        String[] a = new String[6];
        for(int i =1; i < FileManager.employee_list.size(); i++){
           a = FileManager.employee_list.get(i);
            if(a[2].equals(id) && a[1].equals(password)){
                flag =true;
                break;
            }
        }
        if(flag){
            System.out.println("Login Successful!");
            System.out.println("Welcome, " + a[0] + " " + "(" + a[5] + ")");
            if(a[3].equals("Manager")){
                current_user = new Manager(a);
            }
            else{
               current_user = new Employee(a);
            }
            current_user.show_menu();
        }
        else{
            System.out.println("Login Failed: Invalid User ID or Password.");
        }
    }
}
