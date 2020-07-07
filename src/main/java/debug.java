import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.util.Pair;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class debug {
    public void sub_pre() throws IOException {

        HashMap<String, HashSet<String>> dir = new HashMap<>();
        HashSet<String> temp = new HashSet<>();
        temp.add("180");temp.add("240");temp.add("300");dir.put("dm", temp);
        temp = new HashSet<>();temp.add("25");temp.add("50");temp.add("100");temp.add("150");dir.put("nr", temp);
        temp = new HashSet<>();temp.add("5");temp.add("7");temp.add("10");dir.put("ncm", temp);
        temp = new HashSet<>();temp.add("0");temp.add("50");temp.add("100");temp.add("200");dir.put("thrcs", temp);
        temp = new HashSet<>();temp.add("40");temp.add("60");temp.add("80");dir.put("eps", temp);
        temp = new HashSet<>();temp.add("5");temp.add("8");temp.add("10");temp.add("15");dir.put("skip", temp);

        StringBuffer record = new StringBuffer();
        Gson gson = new Gson();

        for(String subtask: dir.keySet()) {
            record.append(subtask).append(" result:\n");
            for(String value: dir.get(subtask)){
                System.out.println(subtask+" "+value+" finished.");
                String data_set = "./NYC/"+subtask+"/" + value + "/";
                Check_SMD checker = new Check_SMD();
                InputStreamReader in = new InputStreamReader(new FileInputStream(data_set+"core.json"));
                checker.core = gson.fromJson(in,
                        new TypeToken<boolean[]>() {
                        }.getType());
                in.close();
                in = new InputStreamReader(new FileInputStream(data_set+"sub.json"));
                checker.sub = gson.fromJson(in,
                        new TypeToken<Boolean[]>() {
                        }.getType());
                in.close();
                in = new InputStreamReader(new FileInputStream(data_set+"alterf.json"));
                checker.alter = gson.fromJson(in,
                        new TypeToken<ArrayList<HashMap<Integer, Integer>>>() {
                        }.getType());
                in.close();
                in = new InputStreamReader(new FileInputStream(data_set+"sub2core.json"));
                checker.sub_core = gson.fromJson(in,
                        new TypeToken<HashMap<Integer, CHSP.Vertex>>() {
                        }.getType());
                in.close();
                in = new InputStreamReader(new FileInputStream(data_set+"raw_HSgraph.json"));
                checker.HSgraph = gson.fromJson(in,
                        new TypeToken<CHSP.Vertex[]>() {
                        }.getType());
                in.close();
                checker.n = checker.alter.size();
                checker.file = data_set;
                checker.SMD();
            }
        }
                /*BuildGraph Builder = new BuildGraph();

                InputStreamReader in = new InputStreamReader(new FileInputStream(data_set+"core.json"));
                Builder.core = gson.fromJson(in,
                        new TypeToken<boolean[]>() {
                        }.getType());
                in.close();
                in = new InputStreamReader(new FileInputStream(data_set+"sub.json"));
                Builder.sub = gson.fromJson(in,
                        new TypeToken<Boolean[]>() {
                        }.getType());
                in.close();
                in = new InputStreamReader(new FileInputStream(data_set+"defective.json"));
                ArrayList<Boolean> defective = gson.fromJson(in,
                        new TypeToken<ArrayList<Boolean>>() {
                        }.getType());
                in.close();

                in = new InputStreamReader(new FileInputStream(data_set+"sub2sub.json"));
                Builder.sub_nodes = gson.fromJson(in,
                        new TypeToken<HashMap<Integer, CHSP.Vertex>>() {
                        }.getType());
                in.close();

                in = new InputStreamReader(new FileInputStream(data_set+"HSgraph.json"));
                Builder.HSgraph = gson.fromJson(in,
                        new TypeToken<CHSP.Vertex[]>() {
                        }.getType());
                in.close();

                long starTime = System.currentTimeMillis();
                int i, j;
                HashMap<Integer, HashMap<Integer, Integer>> result = new HashMap<>();
                for (i = 0; i < 800; i++) {
                    HashMap<Integer, Integer> temp2 = new HashMap<>();
                    if (!defective.get(i)) {
                        for (j = 0; j < 800; j++) {
                            if (!defective.get(j)) temp2.put(j, Builder.BD.computeDist(i, j));
                        }
                        result.put(i, temp2);
                    }
                }
                long endTime = System.currentTimeMillis();
                long Time = endTime - starTime;
                record.append(value).append(": ").append(Time).append("\n");
                //String jsonObject = gson.toJson(result);
                //OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("." + Builder.file + "HS" + Time + ".json"));
                //out.write(jsonObject, 0, jsonObject.length());
                //out.close();*/
        /*String jsonObject = gson.toJson(record);
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("./NYC/SPC_result.json"));
        out.write(jsonObject, 0, jsonObject.length());
        out.close();*/
    }
    public void prune() throws IOException {
        Gson gson = new Gson();
        InputStreamReader in = new InputStreamReader(new FileInputStream("./debug/alterf.json"));
        ArrayList<HashMap<Integer, Integer>> alter = gson.fromJson(in,
                new TypeToken<ArrayList<HashMap<Integer, Integer>>>() {
                }.getType());
        in.close();
        in = new InputStreamReader(new FileInputStream("./NYC/ny_graph_o_j.json"));
        BiSP.Vertex[]graph = gson.fromJson(in,
                new TypeToken<BiSP.Vertex[]>() {
                }.getType());
        in.close();
        in = new InputStreamReader(new FileInputStream("./NYC/ny_graph_r_j.json"));
        BiSP.Vertex[]reverseGraph = gson.fromJson(in,
                new TypeToken<BiSP.Vertex[]>() {
                }.getType());
        in.close();
        in = new InputStreamReader(new FileInputStream("./debug/checker.json"));
        ArrayList<Integer> check = gson.fromJson(in,
                new TypeToken<ArrayList<Integer>>() {
                }.getType());
        in.close();
        in = new InputStreamReader(new FileInputStream("./debug/SMD.json"));
        ArrayList<Integer> SMD_ = gson.fromJson(in,
                new TypeToken<ArrayList<Integer>>() {
                }.getType());
        in.close();
        int i;
        int n = SMD_.size();
        ArrayList<HashMap<Integer, Integer>> MS = new ArrayList<>();
        for (i = 0; i < n; i++) MS.add(new HashMap<>());
        for (i = 0; i < n; i++) for (int ele : alter.get(i).keySet()) MS.get(ele).put(i, alter.get(i).get(ele));
        System.out.println(MS.get(32800));
        System.out.println(MS.get(911));
        in = new InputStreamReader(new FileInputStream("./debug/nyReq30.json"));
        Request[] request_list = gson.fromJson(in,
                new TypeToken<Request[]>() {
                }.getType());
        in.close();
        /*int idx = 0;
        for (Request reqs:request_list){
           if(reqs.ls==32800&&reqs.le==910&&reqs.a==2){System.out.println(idx);break;}
           idx++;
        }*/
        Request req = request_list[128991];
        int max_drive = (int) (req.dist * 1.3);
        req.td = req.tr + max_drive;
        req.p = 30 * req.dist;
        req.tp = req.td - req.dist;
        HashMap<Integer, Integer> MP = alter.get(req.ls);
        HashMap<Integer, Integer> DE = alter.get(req.le);
        for(int meet: MP.keySet()){
            int w_s = MP.get(meet);
            for(int dep: DE.keySet()){
                int dis = BiSP.computeDist(graph,reverseGraph,meet, dep);
                int w_e = DE.get(dep);
                int new_tp = req.td - dis - w_e;
                if(dis!=-1 && dis+w_s+w_e <= max_drive && new_tp > req.tp){
                    req.tp = new_tp;
                }
            }
        }
        System.out.println(gson.toJson(req));
        int r_size = 4;
        ArrayList<Integer[]> route = gson.fromJson("[[38451,54062,54062,0,6666,0,54062,771],[32799,55717,55613,-1,4,2,55613,771],[32799,55617,55613,-1,4,1,55613,771],[32800,55830,55669,-1,161,0,55669,771]]",
                new TypeToken<ArrayList<Integer[]>>() {
                }.getType());
        HashMap<Integer, Integer> DV = gson.fromJson("{\"5440\":52141,\"21890\":53693,\"4259\":53361,\"2550\":55696,\"8086\":51908,\"9672\":53363,\"45417\":52703,\"2444\":51607,\"40622\":54037,\"47054\":55696}",
                new TypeToken<HashMap<Integer, Integer>>() {
                }.getType());
        HashSet<Integer> Ne = gson.fromJson("[32800,32801,38829,32798]",
                new TypeToken<HashSet<Integer>>() {
                }.getType());
        int max_check = r_size;
        int checker = check.get(req.ls);
        int SMD = SMD_.get(req.ls);
        System.out.println(checker);
        System.out.println(SMD);
        System.out.println(BiSP.computeDist(graph, reverseGraph,38451, 32800));
        System.out.println(BiSP.computeDist(graph, reverseGraph, 38451, 32801));
        if(SMD!=1000000) {
            for (i = 0; i < r_size; i++) {
                if (Ne.contains(route.get(i)[0])) {
                    continue;
                }
                int shortest = BiSP.computeDist(graph, reverseGraph, route.get(i)[0], checker);
                System.out.println("shortest "+shortest);
                System.out.println("SMD "+SMD);
                if (shortest == -1 || route.get(i)[6] > req.tp-shortest+SMD) {
                    if (i == 0) {
                        DV.put(route.get(i)[0], req.tp-shortest+SMD);
                        return;
                    }
                    max_check = i;
                    break;
                } /*else if (r_size-1==i || route.get(i)[6] + shortest - SMD >= route.get(i + 1)[2] + route.get(i + 1)[4]) {
                    max_check = i+1;
                    break;
                }*/
            }
        }
        System.out.println(max_check);
    }
    public void smd() throws IOException {
        Gson gson = new Gson();
        InputStreamReader in = new InputStreamReader(new FileInputStream("./debug/alterf.json"));
        ArrayList<HashMap<Integer, Integer>> alter = gson.fromJson(in,
                new TypeToken<ArrayList<HashMap<Integer, Integer>>>() {
                }.getType());
        in.close();
        in = new InputStreamReader(new FileInputStream("./NYC/ny_graph_o_j.json"));
        BiSP.Vertex[]graph = gson.fromJson(in,
                new TypeToken<BiSP.Vertex[]>() {
                }.getType());
        in.close();
        in = new InputStreamReader(new FileInputStream("./NYC/ny_graph_r_j.json"));
        BiSP.Vertex[]reverseGraph = gson.fromJson(in,
                new TypeToken<BiSP.Vertex[]>() {
                }.getType());
        in.close();
        in = new InputStreamReader(new FileInputStream("./debug/core.json"));
        boolean[] core = gson.fromJson(in,
                new TypeToken<boolean[]>() {
                }.getType());
        in.close();
        in = new InputStreamReader(new FileInputStream("./debug/sub.json"));
        boolean[] sub = gson.fromJson(in,
                new TypeToken<boolean[]>() {
                }.getType());
        in.close();
        in = new InputStreamReader(new FileInputStream("./debug/HSgraph.json"));
        CHSP.Vertex[] HSgraph = gson.fromJson(in,
                new TypeToken<CHSP.Vertex[]>(){ }.getType());
        in.close();
        in = new InputStreamReader(new FileInputStream("./debug/sub2core.json"));
        HashMap<Integer, CHSP.Vertex> sub_core = gson.fromJson(in,
                new TypeToken<HashMap<Integer, CHSP.Vertex>>(){ }.getType());
        in.close();

        int n = HSgraph.length;
        HashSet<Integer> VC = new HashSet<>();
        HashMap<Integer, HashMap<Integer, Integer>> CC = new HashMap<>();
        HashSet<Integer> Can = new HashSet<>(alter.get(32800).keySet());
        for (int v : alter.get(32800).keySet()) {
            if (core[v]) {
                VC.addAll(HSgraph[v].inEdges);
            } else if (sub[v]) {
                VC.addAll(sub_core.get(v).inEdges);
            }
        }
        System.out.println(alter.get(32800));
        System.out.println(gson.toJson(VC));
        System.out.println(gson.toJson(Can));
        System.out.println(BiSP.computeDist(graph, reverseGraph, 33795, 32800));
        System.out.println(BiSP.computeDist(graph, reverseGraph, 33795, 32801));
        //System.exit(0);
        for (int v : VC) {
            CC.put(v, new HashMap<>());
        }
        for (int ca : Can) {
            HashSet<Integer> VC_ = (HashSet<Integer>) VC.clone();
            Comparator<Pair<Integer, Integer>> comp = new ShortestPathLRU.pair_com();
            //ArrayList<Integer> prevNode = new ArrayList<>(N);
            ArrayList<Integer> distanceFromSource = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                distanceFromSource.add(i, 1000000);
                //    prevNode.add(i, -1);
            }

            distanceFromSource.set(ca, 0);
            PriorityQueue<Pair<Integer, Integer>> dj = new PriorityQueue<>(n, comp);
            if (core[ca]) {
                for (int i = 0; i < HSgraph[ca].inEdges.size(); i++) {
                    int v = HSgraph[ca].inEdges.get(i);
                    distanceFromSource.set(v, HSgraph[ca].inECost.get(i));
                    dj.add(new Pair<>(-HSgraph[ca].inECost.get(i), v));
                }
            } else {
                for (int i = 0; i < sub_core.get(ca).inEdges.size(); i++) {
                    int v = sub_core.get(ca).inEdges.get(i);
                    distanceFromSource.set(v, sub_core.get(ca).inECost.get(i));
                    dj.add(new Pair<>(-sub_core.get(ca).inECost.get(i), v));
                }
            }

            Pair<Integer, Integer> x;
            int u, v;
            Integer alt;

            while( !dj.isEmpty() && !VC_.isEmpty())
            {
                x = dj.poll();
                u = x.getValue();
                if(VC_.contains(u)){
                    CC.get(u).put(ca, distanceFromSource.get( u ));
                    VC_.remove(u);
                }

                if( distanceFromSource.get( u ) >= 1000000 )
                    break;

                for(int i = 0; i < HSgraph[ u ].inEdges.size(); i++) {
                    v = HSgraph[ u ].inEdges.get( i );
                    alt = distanceFromSource.get( u ) + HSgraph[ u ].inECost.get( i );
                    if( alt < distanceFromSource.get( v ) ) {
                        distanceFromSource.set(v, alt);
                        dj.add( new Pair<>(-alt, v) );
                        //    prevNode.set(v, u);
                    }
                }
            }
        }
        System.out.println(gson.toJson(CC));
        HashMap<Integer, Integer> V2MD = new HashMap<>();
        for(int ca:Can){
            V2MD.put(ca, 0);
        }
        for (int vc:VC){
            if (CC.get(vc).size()!=Can.size()){
                //System.out.println(Can.size());
                for(int temp: Can){
                    System.out.println("checker "+temp);
                    System.out.println("SMD "+1000000);
                    System.exit(0);
                }
            }else {
                int low = Collections.min(CC.get(vc).values());
                for(int vp:Can){
                    if (V2MD.get(vp)<CC.get(vc).get(vp)-low){
                        V2MD.put(vp, CC.get(vc).get(vp)-low);
                    }
                }
            }
        }
        int ch = -1;
        int SMD_ = 1000000;
        for(int ca:Can){
            if (V2MD.get(ca)<SMD_){
                ch = ca;
                SMD_ = V2MD.get(ca);
            }
        }
        System.out.println("checker "+ch);
        System.out.println("SMD "+SMD_);
    }
    public static void main(String[] args) throws Throwable {
        debug bug = new debug();
        //bug.prune();
        //bug.smd();
        bug.sub_pre();
        /*for(int i=0;i<1000;i++){
            System.out.println(i+":"+queryID);
            for (int j=10000;j<11000;j++){
                if(def.get(i)||def.get(j)){
                    continue;
                }
                if (BiSP.computeDist(graph,reverseGraph,i,j)!=Builder.HS_dis(i, j)){
                    System.out.println(i);
                    System.out.println(j);
                    break;
                }
            }
        }*/

        /*for(int i=0;i<1000;i++){
            System.out.println(i+":"+counter);
            for (int j=10000;j<11000;j++){
                int co = 0;
                if(def.get(i)||def.get(j)){
                    continue;
                }
                ArrayList<Integer> path = BiSP.computePath(graph,reverseGraph,i,j);
                System.out.println(path);
                //System.out.println( BiSP.computeDist(graph,reverseGraph,i,j));
                //System.out.println(path);
                int last = i;
                if(path!=null) {
                    counter++;
                    for (int now : path) {
                        if (core.get(now)) {
                            System.out.println(last+" to "+now+" : "+BiSP.computeDist(graph,reverseGraph,last,now));
                            last = now;
                            co = 0;
                        } else {
                            co++;
                        }
                        if (co == skip) {
                            System.out.println("fuck");
                        }
                    }
                }
                break;
            }
            break;
        }

        in = new InputStreamReader(new FileInputStream("./NYC/ny_HSgraph_j.json"));
        CHSP.Vertex[] HSgraph = gson.fromJson(in,
                new TypeToken<CHSP.Vertex[]>(){ }.getType());
        in.close();

        System.out.println(HSgraph[46785].outEdges);
        System.out.println(HSgraph[46785].outECost);
        System.out.println(HSgraph[34977].inEdges);
        System.out.println(HSgraph[34977].inECost);

        in = new InputStreamReader(new FileInputStream("./NYC/ny_sub2sub_j.json"));
        HashMap<Integer, CHSP.Vertex> sub_nodes = gson.fromJson(in,
                new TypeToken<HashMap<Integer, CHSP.Vertex>>(){ }.getType());
        in.close();

        in = new InputStreamReader(new FileInputStream("./NYC/ny_sub2core_j.json"));
        HashMap<Integer, CHSP.Vertex> sub_core = gson.fromJson(in,
                new TypeToken<HashMap<Integer, CHSP.Vertex>>(){ }.getType());
        in.close();

        long queryID = 1;
        CHSP.BidirectionalDijkstra bd = new CHSP.BidirectionalDijkstra();
        System.out.println(BuildGraph.HS_dis(core, sub, HSgraph, sub_nodes, sub_core, bd, queryID++, 1, 30));
        System.out.println(BiSP.computeDist(graph,reverseGraph,0,5));
        System.out.println(HSgraph[34977].inEdges);
        System.out.println(BuildGraph.HS_dis(core, sub, HSgraph, sub_nodes, sub_core, bd, queryID++, 0, 5));
        System.out.println(BiSP.computeDist(graph,reverseGraph,0,1));

        System.out.println(BuildGraph.HS_dis(core, sub, HSgraph, sub_nodes, sub_core, bd, queryID++, 0, 1));
        System.out.println(BiSP.computeDist(graph,reverseGraph,1, 0));
        System.out.println(BuildGraph.HS_dis(core, sub, HSgraph, sub_nodes, sub_core, bd, queryID++, 1, 0));
        System.out.println(BiSP.computeDist(graph,reverseGraph,0,30));
        System.out.println(HSgraph[34977].inEdges);
        System.out.println(BuildGraph.HS_dis(core, sub, HSgraph, sub_nodes, sub_core, bd, queryID++, 0, 30));
        System.out.println(BiSP.computeDist(graph,reverseGraph,46785, 34977));
        System.out.println(BuildGraph.HS_dis(core, sub, HSgraph, sub_nodes, sub_core, bd, queryID++, 46785, 34977));
        System.out.println(bd.computeDist(HSgraph, 46785, 34977, queryID));
        System.out.println(graph[46785].adjList);
        System.out.println(graph[46785].costList);
        System.out.println(graph[56049].adjList);
        System.out.println(graph[56049].costList);
        System.out.println(graph[4374].adjList);
        System.out.println(graph[4374].costList);
        System.out.println(graph[4375].adjList);
        System.out.println(graph[4375].costList);
        System.out.println(graph[5711].adjList);
        System.out.println(graph[5711].costList);
        System.out.println(graph[34977].adjList);
        System.out.println(graph[34977].costList);
        System.out.println(HSgraph[46785].outEdges);
        System.out.println(HSgraph[46785].outECost);
        System.out.println(HSgraph[34977].inEdges);
        System.out.println(HSgraph[34977].inECost);*/
        /*Gson gson = new Gson();
        InputStreamReader in = new InputStreamReader(new FileInputStream("./NYC/ny_graph_o_j.json"));
        BiSP.Vertex[]graph = gson.fromJson(in,
                new TypeToken<BiSP.Vertex[]>() {
                }.getType());
        in.close();

        in = new InputStreamReader(new FileInputStream("./NYC/ny_graph_r_j.json"));
        BiSP.Vertex[]reverseGraph = gson.fromJson(in,
                new TypeToken<BiSP.Vertex[]>() {
                }.getType());
        in.close();
        System.out.println(BiSP.computeDist(graph,reverseGraph,1,23));

        in = new InputStreamReader(new FileInputStream("./NYC/ny_defective_j.json"));
        ArrayList<Boolean> def = gson.fromJson(in,
                new TypeToken<ArrayList<Boolean>>() {
                }.getType());
        in.close();

        in = new InputStreamReader(new FileInputStream("./NYC/ny_rtree_j.json"));
        ArrayList<ArrayList<Double>> lalo2id = gson.fromJson(in,
                new TypeToken<ArrayList<ArrayList<Double>>>(){ }.getType());
        in.close();

        in = new InputStreamReader(new FileInputStream("./NYC/ny_graph_j.json"));
        ArrayList<ArrayList<Integer>> Edges = gson.fromJson(in,
                new TypeToken<ArrayList<ArrayList<Integer>>>(){ }.getType());
        in.close();

        int n = lalo2id.size();	//number of vertices in the graph.
        System.out.println(n);

        CHSP.Vertex[] CHgraph = new CHSP.Vertex[n];

        //initialize the graph.
        for(int i=0;i<n;i++){
            CHgraph[i] = new CHSP.Vertex(i);
        }

        //get edges
        for (ArrayList<Integer> Edge:Edges) {
            int x, y, c;
            x = Edge.get(0);
            y = Edge.get(1);
            if(def.get(x) || def.get(y)){
                continue;
            }
            c = Edge.get(2);

            CHgraph[x].outEdges.add(y);
            CHgraph[x].outECost.add(c);
            CHgraph[y].inEdges.add(x);
            CHgraph[y].inECost.add(c);
        }
        CHSP.PreProcess process = new CHSP.PreProcess();
        process.processing(CHgraph);
        CHSP.BidirectionalDijkstra bd = new CHSP.BidirectionalDijkstra();
        long id = 0;
        for(int i=0;i<1000;i++){
            for (int j=10000;j<11000;j++){
                if(def.get(i)||def.get(j)){
                    continue;
                }
                if (BiSP.computeDist(graph,reverseGraph,i,j)!=bd.computeDist(CHgraph,i,j,id++)){
                    System.out.println(i);
                    System.out.println(j);
                    break;
                }
            }
        }*/
    }
}
