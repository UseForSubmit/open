import json
import csv
from rtree import index
from operator import itemgetter

p = index.Property()
p.dimension = 2
p.dat_extension = 'data'
p.idx_extension = 'index'
id_nodes = index.Index("./ny_nodes", properties=p)

nodes = json.load(open("./NYC/ny_rtree_j.json", "r"))
print(len(nodes))
print(nodes[0])
num = 0
for node in nodes:
    id_nodes.insert(num, node)
    num += 1
output_name = "./NYC/nyTask30.json"
his = open("./NYC/ny_output_price_12", "w")
filename = "./NYC/yellow_tripdata_2013-12.csv"
tasks = []
task = []
with open(filename) as f:
    reader = csv.reader(f)
    num = 0
    for row in reader:
        if num > 1:
            if int(row[1].split(" ")[0].split("-")[2]) != 1:
                try:
                    his.write(str(int(row[1].split(" ")[0].split("-")[2])) + " " +
                              str(list(id_nodes.nearest((float(row[6]), float(row[5]))))[0]) + " " +
                              str(int(row[1].split(" ")[1].split(":")[0])*60 +
                                  int(row[1].split(" ")[1].split(":")[1])) + " " +
                              str(list(id_nodes.nearest((float(row[10]), float(row[9]))))[0]) + " " +
                              str(float(row[-1])) + "\n")
                except ValueError as e:
                    print(row)
                    continue
                if int(row[1].split(" ")[0].split("-")[2]) == 30:
                    try:
                        task = [int(row[1].split(" ")[1].split(":")[0]) * 3600 +
                                int(row[1].split(" ")[1].split(":")[1]) * 60 + int(row[1].split(" ")[1].split(":")[2]),
                                list(id_nodes.nearest((float(row[6]), float(row[5]))))[0],
                                list(id_nodes.nearest((float(row[10]), float(row[9]))))[0], int(row[3])]
                        tasks.append(task)
                    except ValueError as e:
                        print(e)
        num += 1
        if num % 1000000 == 0:
            print(num)
tasks = sorted(tasks, itemgetter(0))
json.dump(task, open(output_name, "w"))
his.close()
