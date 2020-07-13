import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.*;

public class gen_syn {
    public static void main(String[] args) throws IOException {
        String data_file = "./NYC/ny";
        Gson gson = new Gson();
        InputStreamReader in = new InputStreamReader(new FileInputStream(data_file+"_output_price_12"));
        ArrayList<int[]> requests = gson.fromJson(in,
                new TypeToken<ArrayList<int[]>>() {
                }.getType());
        in.close();

        ArrayList<HashMap<Integer, HashMap<Integer, Integer>>> end = new ArrayList<>();
        for (int i = 0; i < 24 * 60; i++) end.add(new HashMap<>());

        HashSet<Integer> days = new HashSet<>();
        ArrayList<Integer> cap = new ArrayList<>();
        for(int[] info: requests){
            int day = info[0];
            days.add(day);
            int time = info[2];
            int loc = info[1];
            int locd = info[3];
            cap.add(info[4]);
            if (!end.get(time).containsKey(loc)) {
                HashMap<Integer, Integer> temp = new HashMap<>();
                temp.put(locd, 1);
                end.get(time).put(loc, temp);
            } else if (!end.get(time).get(loc).containsKey(locd)) {
                end.get(time).get(loc).put(locd, 1);
            } else {
                end.get(time).get(loc).put(locd, end.get(time).get(loc).get(locd) + 1);
            }
        }

        System.out.println("start");
        HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> end_dis = new HashMap<>();
        HashMap<Integer, ArrayList<Integer>> start_dis = new HashMap<>();
        for (int time = 0; time < 60 * 24; time++) {
            end_dis.put(time, new HashMap<>());
            ArrayList<Integer> start = new ArrayList<>();
            ArrayList<Integer> fre_a = new ArrayList<>();
            for (int loc : end.get(time).keySet()) {
                start.add(loc);
                int num = 0;
                for (int temp : end.get(time).get(loc).values()) num += temp;
                fre_a.add(num);
                ArrayList<Integer> where = new ArrayList<>();
                for (int key : end.get(time).get(loc).keySet()) {
                    for(int n_=0;n_< end.get(time).get(loc).get(key);n_++){
                        where.add(key);
                    }
                }
                end_dis.get(time).put(loc, where);
            }
            ArrayList<Integer> where = new ArrayList<>();
            for (int n_=0;n_<start.size();n_++) {
                for(int n__=0;n__<fre_a.get(n_);n__++){
                    where.add(start.get(n_));
                }
            }
            start_dis.put(time, where);
        }
        System.out.println("collected");

        ArrayList<Integer> dis_t = new ArrayList<>();
        for (int time = 0; time < end.size(); time++){
            for (Integer freq : end.get(time).keySet()) {
                for (int mfre : end.get(time).get(freq).values()) {
                    for (int n_=0;n_<mfre;n_++)dis_t.add(time);
                }
            }
        }
        System.out.println("cap finished");

        Random rad = new Random();
        HashMap<Integer, ArrayList<int[]>> tasks = new HashMap<>();
        for(int i=0;i<60*24*60;i++) tasks.put(i, new ArrayList<>());
        int counter = 0;

        ShortestPathLRU SPC = new ShortestPathLRU();
        SPC.init(data_file);
        while(true){
            int time = dis_t.get(rad.nextInt(dis_t.size()));
            int sub_time = rad.nextInt(60);
            int loc = start_dis.get(time).get(rad.nextInt(start_dis.get(time).size()));
            int locd = end_dis.get(time).get(loc).get(rad.nextInt(end_dis.get(time).get(loc).size()));

            if (loc == locd) continue;
            int dist = SPC.dis(loc, locd);
            if (dist == -1) continue;

            int cap_n = cap.get(rad.nextInt(cap.size()));
            /*double cap_p = rad.nextDouble()-cap.get(cap_n);
            while (cap_p>0) {
                cap_n++;
                cap_p-=cap.get(cap_n);
            }*/
            counter++;
            int[] task = {time*60+sub_time, loc, locd, cap_n};
            tasks.get(time*60+sub_time).add(task);
            if(counter==100000){
                ArrayList<Request> Requests = new ArrayList<>(100000);
                for(int key=0;key<60*24*60;key++) {
                    for (int[] request_info : tasks.get(key)) {
                        Request request = new Request();
                        dist = SPC.dis(request_info[1], request_info[2]);
                        request.init(request_info[1], request_info[2], request_info[0],
                                request_info[3], dist);
                        Requests.add(request);
                    }
                }
                String jsonObject = gson.toJson(Requests);
                OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("./NYC/nyReq100f.json"));
                out.write(jsonObject, 0, jsonObject.length());
                out.close();
                System.out.println(100);
            }
            if(counter==200000){
                ArrayList<Request> Requests = new ArrayList<>(200000);
                for(int key=0;key<60*24*60;key++) {
                    for (int[] request_info : tasks.get(key)) {
                        Request request = new Request();
                        dist = SPC.dis(request_info[1], request_info[2]);
                        request.init(request_info[1], request_info[2], request_info[0],
                                request_info[3], dist);
                        Requests.add(request);
                    }
                }
                String jsonObject = gson.toJson(Requests);
                OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("./NYC/nyReq200f.json"));
                out.write(jsonObject, 0, jsonObject.length());
                out.close();
                System.out.println(200);
            }
            if(counter==400000){
                ArrayList<Request> Requests = new ArrayList<>(400000);
                for(int key=0;key<60*24*60;key++) {
                    for (int[] request_info : tasks.get(key)) {
                        Request request = new Request();
                        dist = SPC.dis(request_info[1], request_info[2]);
                        request.init(request_info[1], request_info[2], request_info[0],
                                request_info[3], dist);
                        Requests.add(request);
                    }
                }
                String jsonObject = gson.toJson(Requests);
                OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("./NYC/nyReq400f.json"));
                out.write(jsonObject, 0, jsonObject.length());
                out.close();
                System.out.println(400);
            }
            if(counter==800000){
                ArrayList<Request> Requests = new ArrayList<>(800000);
                for(int key=0;key<60*24*60;key++) {
                    for (int[] request_info : tasks.get(key)) {
                        Request request = new Request();
                        dist = SPC.dis(request_info[1], request_info[2]);
                        request.init(request_info[1], request_info[2], request_info[0],
                                request_info[3], dist);
                        Requests.add(request);
                    }
                }
                String jsonObject = gson.toJson(Requests);
                OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("./NYC/nyReq800f.json"));
                out.write(jsonObject, 0, jsonObject.length());
                out.close();
                System.out.println(800);
                break;
            }
        }
    }
/*    end = {}

for time in end:
    end_dis[time] = {}
    start = []
    fre_a = []
    num_a = 0
            for loc in end[time]:
            start.append(loc)
    num = sum(end[time][loc].values())
    num_a += num
        fre_a.append(sum(end[time][loc].values()))
    where, fre = [], []
            for key in end[time][loc]:
            where.append(key)
            fre.append(end[time][loc][key]/num)
    end_dis[time][loc] = (where, fre)
    start_dis[time] = (start, [fre/num_a for fre in fre_a])

    prInteger.parseInt("first")

    num_day = len(days)

    cap = {1: 0, 2: 0, 3: 0, 4: 0, 5: 0, 6: 0, 7: 0, 8: 0, 9: 0, 10: 0}
    request = open("NYC/yellow_tripdata_2013-12.csv")
    line = request.readline()
    req_num = 0
            while line:
    line = request.readline()
            try:
    cap[max(1, Integer.parseInt(line.split(",")[3]))] += 1
    req_num += 1
    except:
    prInteger.parseInt(line)
for key in cap:
    cap[key] /= req_num
    prInteger.parseInt(cap)

    tasks = []
    dis_t = [0 for _ in range(24*60)]
            for time in end:
    dis_t[time] += sum([sum(end[time][fre].values()) for fre in end[time]])
    total = sum(dis_t)
for time in end:
    dis_t[time] /= total
            times = np.random.choice(list(range(24*60)), size=100000, p=dis_t)
for time in times:
    sub_time = np.random.choice(list(range(60)), p=[1/60 for _ in range(60)])
    loc = np.random.choice(start_dis[time][0], p=start_dis[time][1])
    locd = np.random.choice(end_dis[time][loc][0], p=end_dis[time][loc][1])
            tasks.append([Integer.parseInt(time*60+sub_time), Integer.parseInt(loc), Integer.parseInt(locd),
    Integer.parseInt(np.random.choice(list(cap.keys()), p=list(cap.values())))])
            json.dump(sorted(tasks, key=itemgetter(0, 1, 2)), open("./NYC/nyTask100k.json", "w"))
    prInteger.parseInt("100")
    times = np.random.choice(list(range(24*60)), size=100000, p=dis_t)
            for time in times:
    sub_time = np.random.choice(list(range(60)), p=[1/60 for _ in range(60)])
    loc = np.random.choice(start_dis[time][0], p=start_dis[time][1])
    locd = np.random.choice(end_dis[time][loc][0], p=end_dis[time][loc][1])
            tasks.append([Integer.parseInt(time*60+sub_time), Integer.parseInt(loc), Integer.parseInt(locd),
    Integer.parseInt(np.random.choice(list(cap.keys()), p=list(cap.values())))])
            json.dump(sorted(tasks, key=itemgetter(0, 1, 2)), open("./NYC/nyTask200k.json", "w"))
    prInteger.parseInt("200")
    times = np.random.choice(list(range(24*60)), size=200000, p=dis_t)
            for time in times:
    sub_time = np.random.choice(list(range(60)), p=[1/60 for _ in range(60)])
    loc = np.random.choice(start_dis[time][0], p=start_dis[time][1])
    locd = np.random.choice(end_dis[time][loc][0], p=end_dis[time][loc][1])
            tasks.append([Integer.parseInt(time*60+sub_time), Integer.parseInt(loc), Integer.parseInt(locd),
    Integer.parseInt(np.random.choice(list(cap.keys()), p=list(cap.values())))])
            json.dump(sorted(tasks, key=itemgetter(0, 1, 2)), open("./NYC/nyTask400k.json", "w"))
    times = np.random.choice(list(range(24*60)), size=400000, p=dis_t)
    prInteger.parseInt("400")
for time in times:
    sub_time = np.random.choice(list(range(60)), p=[1/60 for _ in range(60)])
    loc = np.random.choice(start_dis[time][0], p=start_dis[time][1])
    locd = np.random.choice(end_dis[time][loc][0], p=end_dis[time][loc][1])
            tasks.append([Integer.parseInt(time*60+sub_time), Integer.parseInt(loc), Integer.parseInt(locd),
    Integer.parseInt(np.random.choice(list(cap.keys()), p=list(cap.values())))])
            json.dump(sorted(tasks, key=itemgetter(0, 1, 2)), open("./NYC/nyTask800k.json", "w"))
    prInteger.parseInt("800")*/
}
