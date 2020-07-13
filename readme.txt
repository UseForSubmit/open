We have 3 algorithms

-----------for Algorithm SMDB:
Run:    mvn compile exec:java -Dexec.mainClass="HSRP" -Dexec.args="|W|  1+e_r  a_i"
-----------for Algorithm BasicMP:
Run:    mvn compile exec:java -Dexec.mainClass="RPWM" -Dexec.args="|W|  1+e_r  a_i"
-----------for Algorithm GreedyDP:
Run:    mvn compile exec:java -Dexec.mainClass="GreedyDP" -Dexec.args="|W|  1+e_r  a_i"

-----------where |W| is #_of_worker,   e_r is detour_factor,   a_i is worker's capacity
-----------for example, 10000 worker with capacity 3, allowing detour 30% for SMDB algorithm:
Run:    mvn compile exec:java -Dexec.mainClass="HSRP" -Dexec.args="10000 1.3 3"

But before running the above algorithms:


Many big raw files cannot be uploaded. After processing, the files for default parameters are uploaded.
To fully setup it, you need to:

-----------1. download the NYC taxi data first:
wget -P ./NYC/ https://s3.amazonaws.com/nyc-tlc/trip+data/yellow_tripdata_2013-12.csv

-----------2. run maven command for request generation and initial LRU of normal graph 
Run:    mvn compile exec:java -Dexec.mainClass="get_request"

-----------3. setup LRU for HMPO graph
Run:    mvn compile exec:java -Dexec.mainClass="ShortestPathHSG"

Then you can run the 3 algorithms.



Below are the steps to set up with new graphs. Remember to change the data name in each java file.

1.Put 3 files in your own directory
locations of nodes                   with format of "NYC/ny_node2loc.json"
edges from OSM                     with format of "new-york-latest_edges.txt.csv"  (not uploaded yet)
request information                 with format of "yellow_tripdata_2013-12.csv"    (not uploaded yet)

----------for basic graph generation-----------------------------
2.Run:       mvn compile exec:java -Dexec.mainClass="Setup"

----------for NYC request generation and LRU initial-------------
3.Run:       mvn compile exec:java -Dexec.mainClass="get_request"

----------for grid prune------------------------------------------
4.Run:       mvn compile exec:java -Dexec.mainClass="grid_dis"

----------operations for meeting point based solutions----------
5.Run:       mvn compile exec:java -Dexec.mainClass="ProcessWM"

----------generate synthetic data---------------------------------
6.Run:       mvn compile exec:java -Dexec.mainClass="gen_syn"
