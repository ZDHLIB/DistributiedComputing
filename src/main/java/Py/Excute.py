import os
import numpy as np
import pandas as pd 
import matplotlib.pyplot as plt

#for i in range(1):
	#os.system('java -jar ./BlackVirus-1.0-SNAPSHOT-jar-with-dependencies.jar ' + str(50) + ' ./res.csv')

data = pd.read_csv("res.csv",index_col=False)
print data.columns


# Number of Nodes versus # of others
#ids = np.arange(1,21,1)
ids = np.linspace(1, 20, 20,endpoint=True)

# nodes versus number of EDGES
plt.subplot(221)
d10 = data["EDGES"].values
k = 0
for i in range(10, 55, 5):
	plt.plot(ids,d10[k*20:k*20+20],label='Node #: '+str(i))
	k += 1
plt.xlabel('Node ID')
plt.ylabel('Number of Edges')
plt.legend(fontsize='x-small')


# nodes versus number of TOT_MOVES
plt.subplot(222)
d10 = data["TOT_MOVES"].values
k = 0
for i in range(10, 55, 5):
	plt.plot(ids,d10[k*20:k*20+20],label='Node #: '+str(i))
	k += 1
plt.xlabel('Node ID')
plt.ylabel('Number of TOT_MOVES')
plt.legend(fontsize='x-small')



# nodes versus number of LEADER_MOVES
plt.subplot(223)
d10 = data["LEADER_MOVES"].values
k = 0
for i in range(10, 55, 5):
	plt.plot(ids,d10[k*20:k*20+20],label='Node #: '+str(i))
	k += 1
plt.xlabel('Node ID')
plt.ylabel('Number of LEADER_MOVES')
plt.legend(fontsize='x-small')


# nodes versus number of shadows
plt.subplot(224)
d10 = data["SHADOWS"].values
k = 0
for i in range(10, 55, 5):
	plt.plot(ids,d10[k*20:k*20+20],label='Node #: '+str(i))
	k += 1
plt.xlabel('Node ID')
plt.ylabel('Number of LEADER_MOVES')
plt.legend(fontsize='x-small')


#============================================================================
#                  Start average data ploting
#============================================================================
#============================================================================
#                  Start density ploting
#============================================================================
#graph density = 2|E| / |V| * (|V| - 1)
density = 2 * data['EDGES'] / data['NODES'] * (data['NODES'] - 1)
densityDF = pd.concat([data,density],axis=1)
densityDF.columns = [u'ID', u'NODES', u'EDGES', u'TOT_MOVES', u'LEADER_MOVES', u'SHADOWS',u'DENSITY']

ids = np.linspace(1, 20, 20,endpoint=True)
d10 = densityDF["DENSITY"].values
k = 0
fig, bx = plt.subplots()
for i in range(10, 55, 5):
	bx.plot(ids,d10[k*20:k*20+20],label='Node #: '+str(i))
	k += 1
plt.xlabel('Number of Nodes')
plt.ylabel('Graph Density')
legend = bx.legend(shadow=True, fontsize='x-small')

#================================================================
#plot average density of graph
density_sum = densityDF.groupby(by=['NODES']).sum()
density_mean = density_sum/20

x = density_mean.index.tolist()
y_edges = density_mean['EDGES']
y_totM = density_mean['TOT_MOVES']
y_leaderM = density_mean['LEADER_MOVES']
y_shadow = density_mean['SHADOWS']
y_density = density_mean['DENSITY']

fig, ax = plt.subplots()
ax.plot(x, y_edges, label='Ave. EDGES')
ax.plot(x, y_totM, label='Ave. TOT_MOVES')
ax.plot(x, y_leaderM, label='Ave. LEADER_MOVES')
ax.plot(x, y_shadow, label='Ave. SHADOWS')
ax.plot(x, y_density, label='Ave. DENSITY')

legend = ax.legend(shadow=True, fontsize='x-small')


plt.show()


