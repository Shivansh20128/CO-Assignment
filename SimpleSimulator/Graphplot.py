import pandas as pd
from matplotlib import pyplot as plt
plt.style.use('seaborn')
#x = [1,3,5,6]
#y = [7,8,3,1]
# plt.scatter(x,y,s=100)
file1 = open("ScatterPlot.txt", "r+")
L = file1.readlines()
#L[0] = L[0][:]
a_string = "1 2 3"
x_list = L[0].split()
y_list = L[1].split()
map_object_x = map(int, x_list)
map_object_y = map(int, y_list)
# applies int() to a_list elements

list_of_integers_x = list(map_object_x)
list_of_integers_y = list(map_object_y)
plt.scatter(list_of_integers_x, list_of_integers_y, s=2, color="blue")
plt.xlabel('Cycles')
plt.ylabel('Memory Address')
plt.tight_layout()
plt.show()
