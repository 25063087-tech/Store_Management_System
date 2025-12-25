import java.util.*;
import java.io.*;

public class FileManager {
    static ArrayList <String[]> employee_list = new ArrayList<>();
    static ArrayList <String[]> models = new ArrayList<>();
    static ArrayList <String[]> attendance = new ArrayList<>();
    static ArrayList <String[]> sales_history = new ArrayList<>();
    String attendance_path = "Files/Attendance.csv";
    String Employee_path = "Files/Employee_List - Sheet1.csv";
    String model_path = "Files/Model.csv";
    String sale_history = "Files/sales_history.csv";

    public FileManager(){
        try(BufferedReader br = new BufferedReader(new FileReader(Employee_path))){
            String line = "";
            while((line = br.readLine()) != null){
                String[] value = line.split(",");
                employee_list.add(value);
            }
        }
        catch(Exception e){
            System.out.println("There is something wrong with employee list");
        }
        try(BufferedReader br = new BufferedReader(new FileReader(model_path))){
            String line = "";
            while((line = br.readLine()) != null){
                String[] value = line.split(",");
                models.add(value);
            }
        }
        catch(Exception e){
            System.out.println("There is something wrong sales list");
        }
        try(BufferedReader br = new BufferedReader(new FileReader(attendance_path))){
            String line = "";
            while((line = br.readLine()) != null){
                String[] value = line.split(",");
                attendance.add(value);
            }
        }
        catch(Exception e){
            System.out.println("There is something wrong attendance");
        }
        try(BufferedReader br = new BufferedReader(new FileReader(sale_history))){
            String line = "";
            while((line = br.readLine()) != null){
                String[] value = line.split(",");
                sales_history.add(value);
            }
        }
        catch(Exception e){
            System.out.println("There is something wrong with sales history list");
        }
    }
}

