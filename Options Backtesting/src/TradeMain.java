import java.io.*;
import java.util.ArrayList;

public class TradeMain {
    // Returns list of orders from input file
    public static ArrayList<Order> ordersFromFile(String filename, String orderType)
            throws IOException {
        ArrayList<Order> data = new ArrayList<Order>();
        FileReader fr = new FileReader(filename);
        BufferedReader br = new BufferedReader(fr);
        String line = "";
        while ((line = br.readLine()) != null) {
            try {
                Order order = Order.parseLine(line, orderType);
                data.add(order);
            }
            catch (Exception e){
                System.out.println(e);
                System.out.println(line);
            }
        }
        br.close();
        return data;
    }

    public static void main(String[] args) {
        ArrayList<Order> calls = new ArrayList<>();
        ArrayList<Order> puts = new ArrayList<>();

        try{
            puts = ordersFromFile("C:\\Users\\User\\Downloads\\Options Data\\Put_Data_CSV.csv", "put");
        }
        catch (IOException e){ //Ignore lines that don't parse correctly
            System.out.println(e);
        }



   



    }

}


