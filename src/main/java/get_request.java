import com.csvreader.CsvReader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.infomatiq.jsi.Point;
import com.infomatiq.jsi.Rectangle;
import com.infomatiq.jsi.rtree.RTree;
import gnu.trove.TIntProcedure;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

public class get_request {
    int near;
    String file;
    void try_tree(String req_file) throws IOException {
        // change your output name here
        String output_name = file+"Task30.json";
        File req =new File(file+"_output_price_12");

        Gson gson = new Gson();
        RTree tree = new RTree();
        tree.init(new Properties());
        tree.add(new Rectangle(1f, 0f, 1f, 0f), 0);
        tree.add(new Rectangle(3f, 2f, 3f, 2f), 1);
        tree.add(new Rectangle(0f, 3f, 0f, 3f), 2);
        tree.add(new Rectangle(2f, 4f, 2f, 4f), 3);
        tree.add(new Rectangle(4f, 7f, 4f, 7f), 4);
        tree.nearest(new Point(1f, 1f), (TIntProcedure) i -> {
            near = i;
            return true;
        }, Float.MAX_VALUE);
        System.out.println(near);
        InputStreamReader in = new InputStreamReader(new FileInputStream(file+"_rtree_j.json"));
        ArrayList<float[]> nodes = gson.fromJson(in,
                new TypeToken<ArrayList<float[]>>() {
                }.getType());
        in.close();
        System.out.println(nodes.size());

        int num = 0;
        for (float[] node:nodes) {
            tree.add(new Rectangle(node[0], node[1], node[0], node[1]), num);
            num += 1;
        }

        if(!req.exists())req.createNewFile();

        FileWriter reqWritter = new FileWriter(req.getName(),true);
        CsvReader f = new CsvReader(req_file);
        f.readHeaders();
        while (f.readRecord()) {
            if (f.get(3).equals("nan")) {
                continue;
            }
            if (Integer.parseInt(f.get(1).split(" ")[0].split("-")[2]) != 1) {
                tree.nearest(new Point(Float.parseFloat(f.get(6)), Float.parseFloat(f.get(5))), (TIntProcedure) i -> {
                    near = i;
                    return true;
                }, Float.MAX_VALUE);
                int node1 = this.near;
                tree.nearest(new Point(Float.parseFloat(f.get(10)), Float.parseFloat(f.get(9))), (TIntProcedure) i -> {
                    near = i;
                    return true;
                }, Float.MAX_VALUE);
                int node2 = this.near;
                reqWritter.write(Integer.parseInt(f.get(1).split(" ")[0].split("-")[2]) + " " +
                        node1 + " " +
                        Integer.parseInt(f.get(1).split(" ")[1].split(":")[0]) * 60 +
                        Integer.parseInt(f.get(1).split(" ")[1].split(":")[1]) + " " +
                        node2 + " " +
                        Float.parseFloat(f.get(-1)) + "\n");

                if (Integer.parseInt(f.get(1).split(" ")[0].split("-")[2]) != 1) == 30 {
                    task = [int(row[1].split(" ")[1].split(":")[0]) * 3600 +
                                int(row[1].split(" ")[1].split(":")[1]) * 60 + int(row[1].split(" ")[1].split(":")[2]),
                            list(id_nodes.nearest((float(row[6]), float(row[5]))))[0],
                    list(id_nodes.nearest((float(row[10]), float(row[9]))))[0], int(row[3])]
                    tasks.append(task)
                }
            }
        }

        tasks = []
        task = []

        if num % 1000000 == 0:
        print(num)
        tasks = sorted(tasks, itemgetter(0))
        json.dump(task, open(output_name, "w"))
        his.close()
        fileWritter.close();
    }

    public static void main(String[] args) throws IOException {
        get_request req = new get_request();

        String req_file = "./NYC/yellow_tripdata_2013-12.csv";
        String data_file = "./NYC/ny";
        req.file = data_file;
        req.try_tree(req_file);
    }
}

