import java.util.*;
import java.io.*;

public class FileManager {
    public static String[][] stocks;
    static ArrayList <String[]> employee_list = new ArrayList<>();
    static ArrayList <String[]> models = new ArrayList<>();
    static ArrayList <String[]> attendance = new ArrayList<>();
    static ArrayList <String[]> sales_history = new ArrayList<>();
    static String attendance_path = "Files/Attendance.csv";
    static String Employee_path = "Files/Employee_List - Sheet1.csv";
    static String model_path = "Files/Model.csv";
    static String sale_history = "Files/sales_history.csv";

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
    public static void Data_Saver() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(attendance_path))) {
            for (String[] row : attendance) {
                String line = String.join(",", row);
                pw.println(line);
            }
            System.out.println("Attendance data saved successfully.");
        } catch (Exception e) {
            System.out.println("Error: Attendance file did not save. " + e.getMessage());
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(model_path))) {
            for (String[] row : models) {
                String line = String.join(",", row);
                pw.println(line);
            }
            System.out.println("model data saved successfully.");
        } catch (Exception e) {
            System.out.println("Error: model file did not save. " + e.getMessage());
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(Employee_path))) {
            for (String[] row : employee_list) {
                String line = String.join(",", row);
                pw.println(line);
            }
            System.out.println("Employee data saved successfully.");
        } catch (Exception e) {
            System.out.println("Error: employee file did not save. " + e.getMessage());
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(sale_history))) {
            for (String[] row : sales_history) {
                String line = String.join(",", row);
                pw.println(line);
            }
            System.out.println("Sales data saved successfully.");
        } catch (Exception e) {
            System.out.println("Error: Sales file did not save. " + e.getMessage());
        }
    }
}

