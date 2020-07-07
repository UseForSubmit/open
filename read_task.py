import numpy as np
from operator import itemgetter
from Tran import Tran
import time
from rtree import index
train_x_list = []
# get_list()


def save_points(data):
    p = index.Property()
    p.dimension = 2
    p.dat_extension = 'data'
    p.idx_extension = 'index'
    id_nodes = index.Index("./final_nodes", properties=p)
    id_nodes.insert()

    pro = np.array([0.80, 0.11, 0.08, 0.01])

    sorted_data = sorted(data, key=itemgetter(0, 1, 2))
    task = []
    worker = []
    driver = sorted_data[0][0]
    x_l, y_l = Tran(float(sorted_data[0][3]), float(sorted_data[0][4])).gcj02_to_wgs84()
    idx = list(id_nodes.nearest((x_l, y_l), 1))[0]
    # timeStamp = int(sorted_data[0][2])
    # timeArray = time.localtime(timeStamp)
    # otherStyleTime = time.strftime("%Y-%m-%d %H:%M:%S", timeArray)
    # otherStyleTime = otherStyleTime[:11] + '00:00:00'
    # timeArray = time.strptime(otherStyleTime, "%Y-%m-%d %H:%M:%S")
    # start_time = int(time.mktime(timeArray))
    timeArray = time.strptime("2016-10-"+str(101+num)[-2:]+' 00:00:00', "%Y-%m-%d %H:%M:%S")
    start_time = int(time.mktime(timeArray))

    worker.append([int(sorted_data[0][2])-start_time, idx])
    order = sorted_data[0][1]
    task.append([int(sorted_data[0][2])-start_time, idx])
    for point in sorted_data:
        if driver == point[0]:
            if order == point[1]:
                pre = point[2: 5]
            else:
                x_l, y_l = Tran(float(pre[1]), float(pre[2])).gcj02_to_wgs84()
                idx = list(id_nodes.nearest((x_l, y_l), 1))[0]
                task[-1].extend([idx, np.random.choice([1, 2, 3, 4], p=pro.ravel())])
                
                x_l, y_l = Tran(float(point[3]), float(point[4])).gcj02_to_wgs84()
                idx = list(id_nodes.nearest((x_l, y_l), 1))[0]
                task.append([int(point[2])-start_time, idx])
                order = point[1]
        else:
            x_l, y_l = Tran(float(pre[1]), float(pre[2])).gcj02_to_wgs84()
            idx = list(id_nodes.nearest((x_l, y_l), 1))[0]
            task[-1].extend([idx, np.random.choice([1, 2, 3, 4], p=pro.ravel())])
            worker[-1].append(int(pre[0])-start_time)
            driver = point[0]
            order = point[1]
            
            x_l, y_l = Tran(float(point[3]), float(point[4])).gcj02_to_wgs84()
            idx = list(id_nodes.nearest((x_l, y_l), 1))[0]
            worker.append([int(point[2])-start_time, idx])
            task.append([int(point[2])-start_time, idx])
    x_l, y_l = Tran(float(pre[1]), float(pre[2])).gcj02_to_wgs84()
    idx = list(id_nodes.nearest((x_l, y_l), 1))[0]
    task[-1].extend([idx, np.random.choice([1, 2, 3, 4], p = pro.ravel())])
    worker[-1].append(int(pre[0])-start_time)
    worker = np.array(worker).astype(np.int64)
    task = np.array(task).astype(np.int64)
    np.save("driver/driver"+str(101+num)[-2:], worker[worker[:,0].argsort()])
    np.save("task/task"+str(101+num)[-2:], task[task[:,0].argsort()])
    print('finished', num)


def get_list():
    per_list = []
    filename = "./train.csv"
    file = open(filename, 'r')
    line = file.readline()
    while line:
        per_list.append(line.strip('\n').split(','))
        line = file.readline()
    print("done")
    print(len(per_list))
    file.close()
    save_points(per_list)


if __name__ == '__main__':
    # file_num = list(range(31))
    get_list()
    # train_x_list = pool.map(get_list, file_num, chunksize=2)




