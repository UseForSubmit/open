import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class RPWM {
    /**
     * Route Planning with MeetingPoint
     */
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
        String data_file = "./NYC/ny";
        Grid.init(data_file);
        String data_set = "./NYC/default/";

        ShortestPathLRU SPC = new ShortestPathLRU();
        SPC.init(data_file);

        in = new InputStreamReader(new FileInputStream(data_set+"alterf.json"));
        ArrayList<HashMap<Integer, Integer>> alter_node = gson.fromJson(in,
                new TypeToken<ArrayList<HashMap<Integer, Integer>>>() {
                }.getType());
        in.close();

        ArrayList<String> date_list = new ArrayList<>();
        date_list.add("30");
        int counter = 0;
        while (counter < date_list.size()) {
            in = new InputStreamReader(new FileInputStream("./NYC/nyReq" + date_list.get(counter) + ".json"));
            Request[] request_list = gson.fromJson(in,
                    new TypeToken<Request[]>() {
                    }.getType());
            in.close();

            ArrayList<Route> routes = new ArrayList<>(work_num);
            int start = rand.nextInt(request_list.length-work_num);
            for (int work_idx = start; work_idx < start+work_num; work_idx++) {
                Route temp = new Route();
                ArrayList<int[]> temp_route = new ArrayList<>();
                int[] temp_loc = {request_list[work_idx].ls, -10, -10, 0, 6666, 0, -10, 0};
                temp_route.add(temp_loc);
                temp.init(-10, work_cap, temp_route);

                routes.add(temp);
            }
            System.out.println("we have " + routes.size() + " routes");
            Insertion inserter = new Insertion();

            long penalty = 0;
            int time_idx = 0;
            int test_num = 500000;
            int current_num = 0;
            int served = 0;
            int served_me = 0;
            int served_dr = 0;

            long starTime=System.currentTimeMillis();
            for (Request request: request_list) {
                request.fulfil(penalty_weight, detour_factor);
                int time_left = request.tp;
                if (current_num == test_num) break;

                if (current_num % 50000 == 0) {
                    System.out.println(current_num + " arrived, served " + served + ", penalty is " + penalty);
                }
                if (request.tr >= len_time_span * time_idx) {
                    System.out.println("now time is " + request.tr);
                    time_idx += 1;
                }
                HashMap<Integer, Integer> meets = alter_node.get(request.ls);
                HashMap<Integer, Integer> deps = alter_node.get(request.le);
                int cost_final = Integer.MAX_VALUE;
                int route_idx = -1;
                int[] insert_idx = {-1, -1};
                int rou_idx;
                int meet = -1;
                int dep = -1;
                for (rou_idx = 0; rou_idx < routes.size(); rou_idx++) {
                    Route route_ = routes.get(rou_idx);
                    if (Grid.reachable(route_.route.get(0)[0], request.ls, time_left - route_.route.get(0)[6])) {
                        Pair<Pair<Integer,Integer>,Pair<Pair<Integer, Integer>, Integer>> info;
                        info = inserter.LeMDinsertion(route_, request, SPC, meets, deps);

                        if (info != null) {
                            int cost = info.getValue().getValue();
                            if (cost < cost_final) {
                                cost_final = cost;
                                route_idx = rou_idx;
                                meet = info.getValue().getKey().getKey();
                                dep = info.getValue().getKey().getValue();
                                insert_idx[0] = info.getKey().getKey();
                                insert_idx[1] = info.getKey().getValue();
                            }
                        }
                    }
                }
                if (route_idx != -1) {
                    served += 1;
                    //System.out.println("new insertion:");
                    //int last_time = routes.get(route_idx).route.get(routes.get(route_idx).size - 1)[2];
		            //routes.get(route_idx).print_tour_RM();
                    routes.get(route_idx).insert_RMD(insert_idx[0], insert_idx[1], request, SPC, SPC.dis(meet, dep),
                            request.tr+meets.get(meet), meet, dep, request.td-deps.get(dep));
                    //routes.get(route_idx).print_tour_RM();
                    //System.out.println("final cost = "+cost_final);
                    penalty += cost_final; //+(score[request.le]-score[dep])/2;
                    //System.out.println("penalty = "+(routes.get(route_idx).route.get(routes.get(route_idx).size-1)[2] - last_time));
                    if(meet!=request.ls){
                        served_me += 1;
                    }
                    if(dep!=request.le){
                        served_dr += 1;
                    }
                } else {
                    penalty += request.p;
                }
                current_num ++;
            }
            long endTime=System.currentTimeMillis();
            long Time=endTime-starTime;
            System.out.println(current_num + " arrived, " + served_me + "/" + served_dr +
                    " served by meeting points, " + served + " served, " + date_list.get(counter) + " penalty is " + penalty+", time cost = "+Time);
            counter += 1;
        }
    }
}
