import json
import time
import editdistance
import operator
import numpy as np
from tensorflow import keras

with open('data/word_index.json', 'r') as fin:  
  word_index = json.load(fin)

with open('data/train.json') as fin:
	train = json.load(fin)

with open('data/test.json') as fin:
	test = json.load(fin)


def knn(train, test, k):
	
	distances = {}
	sort = {}

	for i in range(len(train['input'])):
		dist = editdistance.eval(train['input'][i], test)
		distances[i] = dist
	
	sorted_d = sorted(distances.items(), key=operator.itemgetter(1))

	neighbors = []

	for x in range(k):
		neighbors.append(sorted_d[x][0])
	
	classVotes = {}

	for x in range(len(neighbors)):
		response = train['output'][neighbors[x]]
 
		if response in classVotes:
			classVotes[response] += 1
		else:
			classVotes[response] = 1
	
	sortedVotes = sorted(classVotes.items(), key=operator.itemgetter(1), reverse=True)
	return(sortedVotes[0][0], neighbors)

correct = 0
total_runtime = 0
l = len(test['input'])
for i in range(l):
	t = time.time()

	if knn(train, test['input'][i], 5)[0] == test['output'][i]:
		correct += 1
		acc = correct/(i+1)

	print(f'Iter {i}/{l}:')
	print('Accuracy(avg): {:.2f}'.format(acc));
	total_runtime += time.time() - t
	avg = total_runtime / (i+1)
	print("{}{:.2f}s".format('Runtime(avg): ', avg))