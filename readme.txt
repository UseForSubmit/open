Many big raw files cannot be uploaded. After processing, the files for default parameters are uploaded.

1. You need to download the NYC taxi data first:
wget https://s3.amazonaws.com/nyc-tlc/trip+data/yellow_tripdata_2013-12.csv

2. Then run maven command for request generation
Run:    python3 get_request.py
Run:    mvn compile exec:java -Dexec.mainClass="Request"

3. Then setup LRU
Run:    mvn compile exec:java -Dexec.mainClass="ShortestPathHSG"
Run:    mvn compile exec:java -Dexec.mainClass="ShortestPathLRU"

4. Finally we have three algorithm
for Algorithm SMDB:
mvn compile exec:java -Dexec.mainClass="HSRP" -Dexec.args="#_of_worker detour_factor capacity"
for Algorithm BasicMP:
mvn compile exec:java -Dexec.mainClass="RPWM" -Dexec.args="#_of_worker detour_factor capacity"
for Algorithm GreedyDP:
mvn compile exec:java -Dexec.mainClass="GreedyDP" -Dexec.args="#_of_worker detour_factor capacity"

For example, 10000 worker with capacity 3, allowing detour 30%:
mvn compile exec:java -Dexec.mainClass="HSRP" -Dexec.args="10000 1.3 3"



Below are the steps to set up with new graphs. Remember to change the data name in each java file.
1.Change the path of pom.xml into your own desk's

2.Put the files for nodes and edges of map from OSM in the project file.

----------for basic graph generation-----------------------------
3.Run:       mvn compile exec:java -Dexec.mainClass="Setup"

----------for grid prune------------------------------------------
4.Run:       mvn compile exec:java -Dexec.mainClass="grid_dis"

----------operations for meeting point based solutions----------
5.Run:       mvn compile exec:java -Dexec.mainClass="ProcessWM"

----------for NYC request generation----------------------------------
6.1.Run:    python3 get_request.py
6.2.Run:    mvn compile exec:java -Dexec.mainClass="Request"

----------generate synthetic data---------------------------------
7.Run:       mvn compile exec:java -Dexec.mainClass="get_syn"

----------operations for LRU construction----------
8.1.Run:    mvn compile exec:java -Dexec.mainClass="ShortestPathHSG"
8.2.Run:    mvn compile exec:java -Dexec.mainClass="ShortestPathLRU"
