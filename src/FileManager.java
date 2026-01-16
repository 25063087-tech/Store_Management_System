import java.util.*;
import java.io.*;
import java.time.LocalDate;
public class FileManager {
    static ArrayList<String[]> employee_list = new ArrayList<>();
    static ArrayList<String[]> models = new ArrayList<>();
    static ArrayList<String[]> attendance = new ArrayList<>();
    static ArrayList<String[]> sales_history = new ArrayList<>();
    static String attendance_path = "Files/Attendance.csv";
    static String Employee_path = "Files/Employee_List - Sheet1.csv";
    static String model_path = "Files/Model.csv";
    static String sale_history = "Files/sales_history.csv";
    static String sales_receipt = "Files/Sales_Receipts";
    public static boolean modelDataModified = false;

    // Loads employee, model, attendance, and sales data at system startup
    public FileManager() {
        try (BufferedReader br = new BufferedReader(new FileReader(Employee_path))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] value = line.split(",");
                employee_list.add(value);
            }
        } catch (Exception e) {
            System.out.println("There is something wrong with employee list");
        }
        try (BufferedReader br = new BufferedReader(new FileReader(model_path))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] value = line.split(",");
                models.add(value);
            }
        } catch (Exception e) {
            System.out.println("There is something wrong sales list");
        }
        try (BufferedReader br = new BufferedReader(new FileReader(attendance_path))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] value = line.split(",");
                attendance.add(value);
            }
        } catch (Exception e) {
            System.out.println("There is something wrong attendance");
        }
        try (BufferedReader br = new BufferedReader(new FileReader(sale_history))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] value = line.split(",");
                sales_history.add(value);
            }
        } catch (Exception e) {
            System.out.println("There is something wrong with sales history list");
        }
    }

    // Saves all in-memory data back to CSV files
    // Prevents data loss during logout or system updates
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

    // Method to create the daily report text file
    public static String createDailyReportFile(String date) {
        //Define the specific folder path
        String folderPath = "Files/Daily_Report";

        //Ensure the directory exists (Create it if it's missing)
        File directory = new File(folderPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        //Combine folder path and filename
        String fileName = folderPath + "/Daily_Report_" + date + ".txt";

        double totalSales = 0.0;
        boolean hasSales = false;

        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            //Write the Header
            pw.println("=========================================");
            pw.println("      GOLDENHOUR HEADQUARTERS REPORT     ");
            pw.println("=========================================");
            pw.println("Date: " + date);
            pw.println("Generated by: Auto-System");
            pw.println("-------------------------------------------------------");
            pw.printf("%-10s %-20s %-15s %-5s %-10s%n", "TIME", "ITEM", "CUSTOMER", "QTY", "TOTAL");
            pw.println("-------------------------------------------------------");

            //Loop through the sales_history list
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

    //Update CSV Stock (From & To)
    public static void updateStockMovement(
            List<String[]> movements,
            String from,
            String to
    ) {
        if (models.isEmpty()) {
            System.out.println("Model list is empty.");
            return;
        }

        String[] headers = models.get(0);

        int fromIndex = getOutletColumnIndexByName(headers, from);
        int toIndex = getOutletColumnIndexByName(headers, to);

        if (fromIndex == -1 || toIndex == -1) {
            System.out.println("Error: Invalid outlet column.");
            return;
        }

        for (int i = 1; i < models.size(); i++) {
            String[] row = models.get(i);

            for (String[] m : movements) {
                if (row[0].equalsIgnoreCase(m[0])) {

                    int qty = Integer.parseInt(m[1]);

                    int fromStock = Integer.parseInt(row[fromIndex]);
                    int toStock = Integer.parseInt(row[toIndex]);

                    row[fromIndex] = String.valueOf(fromStock - qty);
                    row[toIndex] = String.valueOf(toStock + qty);
                }
            }
        }

        modelDataModified = true;


        Data_Saver();
    }

    // Get outlet column index by outlet name
    public static int getOutletColumnIndexByName(String[] columns, String outlet) {

        String columnName = outlet + "_Stock";

        for (int i = 0; i < columns.length; i++) {
            if (columns[i].equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1;
    }


    public static void saveLatestSaleReceipt() {

        if (sales_history == null || sales_history.size() <= 1) return;

        int index = sales_history.size() - 1;
        String[] sale = sales_history.get(index);

        String date      = sale[0];
        String time      = sale[1];
        String empName   = sale[3];
        String custName  = sale[4];
        String item      = sale[5];
        String qty       = sale[6];
        String total     = sale[8];
        String method    = sale[9];

        // CREATE FILENAME (DATE+TIME)
        String safeDate = date.replace("/", "-");
        String safeTime = time.replace(":", "").replace(" ", "");
        String receiptName = "sales_" + safeDate + "_" + safeTime + ".txt";


        if (sale.length < 11) {
            sale = Arrays.copyOf(sale, 11);
            sales_history.set(index, sale);
        }
        sale[10] = receiptName;

        File dir = new File(sales_receipt);
        if (!dir.exists()) dir.mkdirs();

        String path = sales_receipt + "/" + receiptName;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, false))) {

            bw.write("=========================================\n");
            bw.write("             OFFICIAL RECEIPT            \n");
            bw.write("=========================================\n");
            bw.write("Date:      " + date + " " + time + "\n");
            bw.write("Served By: " + empName + "\n");
            bw.write("Customer:  " + custName + "\n");
            bw.write("-----------------------------------------\n");
            bw.write("Item:      " + item + "\n");
            bw.write("Qty:       " + qty + "\n");
            bw.write("Payment:   " + method + "\n");
            bw.write("-----------------------------------------\n");
            bw.write("TOTAL:     RM " + total + "\n");
            bw.write("=========================================\n");

        } catch (IOException e) {
            System.out.println("Error saving receipt.");
        }
    }

    // Updates receipt when a sales record is edited
    public static void updateReceiptForSale(int index) {

        String[] sale = sales_history.get(index);

        if (sale.length < 11 || sale[10] == null || sale[10].isEmpty()) {
            System.out.println("No receipt linked to this sale.");
            return;
        }

        String path = sales_receipt + "/" + sale[10];

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, false))) {

            bw.write("=========================================\n");
            bw.write("             OFFICIAL RECEIPT            \n");
            bw.write("=========================================\n");
            bw.write("Date:      " + sale[0] + " " + sale[1] + "\n");
            bw.write("Served By: " + sale[3] + "\n");
            bw.write("Customer:  " + sale[4] + "\n");
            bw.write("-----------------------------------------\n");
            bw.write("Item:      " + sale[5] + "\n");
            bw.write("Qty:       " + sale[6] + "\n");
            bw.write("Payment:   " + sale[9] + "\n");
            bw.write("-----------------------------------------\n");
            bw.write("TOTAL:     RM " + sale[8] + "\n");
            bw.write("=========================================\n");

            System.out.println("Receipt updated: " + sale[10]);

        } catch (IOException e) {
            System.out.println("Failed to update receipt.");
        }
    }
}










