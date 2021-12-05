import api.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.*;
import java.util.Locale;

import java.util.Iterator;
/**
 * This class is the main class for Ex2 - your implementation will be tested using this class.
 */
public class Ex2 {
    /**
     * This static function will be used to test your implementation
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraph getGraph(String json_file) throws IOException, ParseException {
        DirectedWeightedGraph ans = new MyDWG();
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(json_file));
        JSONObject jobj =(JSONObject) obj;
        JSONArray edges = (JSONArray) jobj.get("Edges");
        JSONArray nodes = (JSONArray) jobj.get("Nodes");
        for (Object o:nodes)
        {
            JSONObject temp = (JSONObject) o;
            Node n = new Node(Integer.parseInt(temp.get("id").toString()),temp.get("pos").toString());
            ans.addNode(n);
        }
        for (Object o:edges)
        {
            JSONObject temp = (JSONObject) o;
            if((temp.get("src")!=null) && temp.get("dest")!=null && temp.get("w")!=null)
            {
                int src = Integer.parseInt(temp.get("src").toString());
                int dst = Integer.parseInt(temp.get("dest").toString());
                double w =Double.parseDouble(temp.get("w").toString());
                ans.connect(src,dst,w);
            }
        }
        return ans;
    }

    /**
     * This static function will be used to test your implementation
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraphAlgorithms getGrapgAlgo(String json_file) {
        DirectedWeightedGraphAlgorithms ans = null;
        // ****** Add your code here ******
        File f = new File(json_file);
        if (f.exists()) {
            InputStream is = null;
            try {
                is = new FileInputStream("file.json");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        // ********************************
        return ans;
    }

    /**
     * This static function will run your GUI using the json fime.
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     */
    public static void runGUI(String json_file) {
        DirectedWeightedGraphAlgorithms alg = getGrapgAlgo(json_file);
        // ****** Add your code here ******
        //
        // ********************************
    }
}