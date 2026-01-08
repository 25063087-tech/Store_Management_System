import java.util.*;
import java.io.*;

public class FileManager {
    static ArrayList <String[]> employee_list = new ArrayList<>();
    static ArrayList <String[]> models = new ArrayList<>();
    static ArrayList <String[]> attendance = new ArrayList<>();
    static ArrayList <String[]> sales_history = new ArrayList<>();
    static String attendance_path = "Files/Attendance.csv";
    static String Employee_path = "Files/Employee_List - Sheet1.csv";
    static String model_path = "Files/Model.csv";
    static String sale_history = "Files/sales_history.csv";

    public static boolean modelDataModified = false;

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
    // Add these imports to the top of FileManager.java
// ... inside the FileManager class ...

    // Method to create the daily report text file
    public static String createDailyReportFile(String date) {
        // 1. Define the specific folder path
        String folderPath = "Files/Sales_Receipts";

        // 2. Ensure the directory exists (Create it if it's missing)
        File directory = new File(folderPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 3. Combine folder path and filename
        String fileName = folderPath + "/Daily_Report_" + date + ".txt";

        double totalSales = 0.0;
        boolean hasSales = false;

        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            // 4. Write the Header
            pw.println("=========================================");
            pw.println("      GOLDENHOUR HEADQUARTERS REPORT     ");
            pw.println("=========================================");
            pw.println("Date: " + date);
            pw.println("Generated by: Auto-System");
            pw.println("-------------------------------------------------------");
            pw.printf("%-10s %-20s %-15s %-5s %-10s%n", "TIME", "ITEM", "CUSTOMER", "QTY", "TOTAL");
            pw.println("-------------------------------------------------------");

            // 5. Loop through the sales_history list
            for (String[] sale : sales_history) {
                // Check if row is valid and matches the requested date
                if (sale.length > 8 && sale[0].equals(date)) {
                    pw.printf("%-10s %-20s %-15s %-5s %-10s%n",
                            sale[1],   // Time
                            sale[5],   // Model Name
                            sale[4],   // Customer Name
                            sale[6],   // Quantity
                            sale[8]);  // Total Price

                    try {
                        totalSales += Double.parseDouble(sale[8]);
                    } catch (NumberFormatException e) {
                        // Ignore bad numbers
                    }
                    hasSales = true;
                }
            }

            if (!hasSales) {
                pw.println("       NO SALES RECORDED TODAY       ");
            }

            // 6. Write the Footer/Totals
            pw.println("-------------------------------------------------------");
            pw.printf("TOTAL SALES ACCUMULATED: RM %.2f%n", totalSales);
            pw.println("=========================================");
            pw.println("End of Report");

            System.out.println(">> Report file generated successfully: " + fileName);

        } catch (IOException e) {
            System.out.println("Error writing report file: " + e.getMessage());
            return null; // Return null if failed
        }

        return fileName; // Return the path so EmailService knows what to attach
    }
}