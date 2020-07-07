import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class GreedyDP {
    public static void main(String[] args) throws IOException {
        final int work_num;
        if (args.length>0){
            work_num = Integer.parseInt(args[0]);
        }else {
            work_num = 3000;
        }
        final double detour_factor;
        if (args.length>1){
            detour_factor = Double.parseDouble(args[1]);
        }else {
            detour_factor = 1.3;
        }
        final int work_cap;
        if (args.length>2){
            work_cap = Integer.parseInt(args[2]);
        }else {
            work_cap = 3;
        }
        final int penalty_weight = 30;
        final int len_time_span = 3600;  // check every hour

        Gson gson = new Gson();
        Random rand = new Random(666);
        InputStreamReader in;

        GridPrune Grid = new GridPrune();
        Grid.init();
        String data_file = "./NYC/ny";

        ShortestPathLRU SPC = new ShortestPathLRU();
        SPC.init(data_file);

        ArrayList<String> date_list = new ArrayList<>();
        date_list.add("30");
        int counter = 0;
        while (counter < date_list.size()) {
            in = new InputStreamReader(new FileInputStream(data_file+"Req" + date_list.get(counter) + ".json"));
            Request[] request_list = gson.fromJson(in,
                    new TypeToken<Request[]>() {
                    }.getType());
            in.close();

            // prepare workers
            ArrayList<Route> routes = new ArrayList<>(work_num);
            int start = rand.nextInt(request_list.length-work_num);
            for (int work_idx = start; work_idx < start+work_num; work_idx++) {
                Route temp = new Route();
                ArrayList<int[]> temp_route = new ArrayList<>();
                int[] temp_loc = {request_list[work_idx].ls, -10, -10, 0, 6666, 0};
                temp_route.add(temp_loc);
                temp.init(-10, work_cap, temp_route);
                routes.add(temp);
            }
            System.out.println("we have " + routes.size() + " routes");
            Insertion inserter = new Insertion();

            long penalty = 0;
            int time_idx = 0;
            int test_num = 800000;
            int current_num = 0;
            int served = 0;

            long starTime=System.currentTimeMillis();
            for (Request request: request_list) {
                if (current_num == test_num) break;

                request.fulfil(penalty_weight, detour_factor);
                int time_left = request.td - request.dist;

                if (current_num % 50000 == 0) System.out.print(current_num + " arrived, served " + served + ", penalty is " + penalty+"\n");
                if (request.tr >= len_time_span * time_idx) System.out.println("now time is " + len_time_span * time_idx++);

                int cost_final = Integer.MAX_VALUE;
                int route_idx = -1;
                int[] insert_idx = {-1, -1};
                int rou_idx;
                for (rou_idx = 0; rou_idx < routes.size(); rou_idx++) {
                    Route route_ = routes.get(rou_idx);
                    if (Grid.reachable(route_.route.get(0)[0], request.ls, time_left - route_.route.get(0)[2])) {
                        Pair<Pair<Integer,Integer>,Integer> info;
                        info = inserter.Leinsertion(route_, request, SPC, request.dist);

                        if (info != null) {
                            int cost = info.getValue();
                            if (cost < cost_final) {
                                cost_final = cost;
                                route_idx = rou_idx;
                                insert_idx[0] = info.getKey().getKey();
                                insert_idx[1] = info.getKey().getValue();
                            }
                        }
                    }
                }
                if (route_idx != -1) {
                    served += 1;
                    //System.out.println("new insertion:");
                    //routes.get(route_idx).print_tour();
                    int last_time = routes.get(route_idx).route.get(routes.get(route_idx).size - 1)[2];
                    routes.get(route_idx).rainsert(insert_idx[0], insert_idx[1], request, SPC, request.dist);
                    //routes.get(route_idx).print_tour();
                    //System.out.println("final cost = "+cost_final);
                    penalty += routes.get(route_idx).route.get(routes.get(route_idx).size - 1)[2] - last_time;
                    //System.out.println("penalty = "+(routes.get(route_idx).route.get(routes.get(route_idx).size-1)[2] - last_time));
                } else {
                    penalty += request.p;
                }
                current_num ++;
            }
            long endTime=System.currentTimeMillis();
            long Time=endTime-starTime;
            System.out.println(current_num + " arrived, " + served + " served, " + date_list.get(counter) + " penalty is " + penalty+", time cost = "+Time);
            counter += 1;
        }
    }
}
