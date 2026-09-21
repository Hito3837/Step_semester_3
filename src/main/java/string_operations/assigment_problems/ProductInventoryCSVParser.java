import java.util.Scanner;
public class ProductInventoryCSVParser
{
    public static void parseInventoryRecord(String csvLine)
    {
        String[] fields = csvLine.split(",");
        if (fields.length != 3)
        {
            System.out.println("Invalid Record");
        }
        else
        {
            String productName = fields[0];
            String sku = fields[1];
            String quantity = fields[2];
            System.out.println("Product: " + productName + " | SKU: " + sku + " | Qty: " + quantity);
        }
    }
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter CSV line (ProductName,SKU,Quantity): ");
        String csvLine = scanner.nextLine();
        parseInventoryRecord(csvLine);
        scanner.close();
    }
}
