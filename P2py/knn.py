import argparse
import json
import time
import editdistance
import operator
import random
import numpy as np
import seaborn as sns
import matplotlib.pyplot as plt
import tensorflow as tf
from tensorflow import keras

parser = argparse.ArgumentParser()
parser.add_argument('-i', '--iterations', help='number of iterations', default=500, type=int)
parser.add_argument('-k', '--kneighbors', help='number of neighbors', default=5, type=int)
parser.add_argument('-c', '--confusion', help='model name to load', default=False, const=True, nargs='?')
args = parser.parse_args()
args = vars(args)

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
acc = 0 
l = len(test['input'])
if (args['iterations'] < l):
	l = args['iterations']

k = args['kneighbors']

#shuffles keys
random_keys = random.choices(np.arange(len(test['input'])), k=l)

if (args['confusion']):
	predictions = []
	labels = []

for i in range(l):
	index = random_keys[i]
	t = time.time()
	prediction = knn(train, test['input'][index], k)[0]
	label = test['output'][index]
	if (args['confusion']):
		predictions.append(prediction)
		labels.append(label)
	if prediction == label:
		correct += 1
		acc = correct/(i+1)

	print(f'Iter {i}/{l}:')
	print('Accuracy(avg): {:.2f}'.format(acc));
	total_runtime += time.time() - t
	avg = total_runtime / (i+1)
	print("{}{:.2f}s".format('Runtime(avg): ', avg))

if (args['confusion']):
	LABELS = [
		"Negative", "Positive"
	]

	# Create a confusion matrix on training data.
	with tf.Graph().as_default():
		cm = tf.confusion_matrix(labels, predictions)
		with tf.Session() as session:
			cm_out = session.run(cm)
	# Normalize the confusion matrix so that each row sums to 1.
	cm_out = cm_out.astype(float) / cm_out.sum(axis=1)[:, np.newaxis]

	sns.heatmap(cm_out, annot=True, xticklabels=LABELS, yticklabels=LABELS);
	plt.xlabel("Predicted");
	plt.ylabel("Actual");
	plt.show()