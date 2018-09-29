import java.io.*;
import java.util.ArrayList;

public class TradeMain {
    // Returns list of DataPoint objects (labelled or unlabelled) from web page input
    public static ArrayList<DataPoint> dataFromFile(String filename)
            throws IOException {
        ArrayList<DataPoint> data = new ArrayList<DataPoint>();
        FileReader fr = new FileReader(filename);
        BufferedReader br = new BufferedReader(fr);
        String line = "";
        while ((line = br.readLine()) != null) {
            DataPoint d = DataPoint.parseLine(line);
            data.add(d);
        }
        br.close();
        return data;
    }

    public static void main(String[] args) {


    }

}


