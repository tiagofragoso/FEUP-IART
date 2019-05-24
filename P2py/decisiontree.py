import argparse
import json
import time
import graphviz
from sklearn import tree
import tensorflow as tf
from tensorflow import keras
import seaborn as sns
import matplotlib.pyplot as plt
import numpy as np

parser = argparse.ArgumentParser()
parser.add_argument('-c', '--confusion', help='model name to load', default=False, const=True, nargs='?')
args = parser.parse_args()
args = vars(args)

with open('data/word_index.json', 'r') as fin:  
  word_index = json.load(fin)

with open('data/train.json') as fin:
	train = json.load(fin)

with open('data/test.json') as fin:
	test = json.load(fin)

max_len = 937

train_data = keras.preprocessing.sequence.pad_sequences(train['input'],
													value=word_index["<PAD>"],
													padding='post',
													maxlen=max_len)

test_data = keras.preprocessing.sequence.pad_sequences(test['input'],
													value=word_index["<PAD>"],
													padding='post',
													maxlen=max_len)

print('Starting training')
t = time.time()
clf = tree.DecisionTreeClassifier()
clf = clf.fit(train_data, train['output'])
runtime = time.time() - t
node_count = clf.tree_.node_count
print(f'{node_count} nodes created')
print("{}{:.2f}s".format('Runtime(train): ', runtime))
if not args['confusion']:
	t = time.time()
	acc = clf.score(test_data, test['output'])
	runtime = time.time() - t
	print("{}{:.2f}s".format('Runtime(test): ', runtime))
	print(f'Accuracy: {acc}')
else:
	t = time.time()
	pred = clf.predict(test_data)

	LABELS = [
		"Negative", "Positive"
	]

	# Create a confusion matrix on training data.
	with tf.Graph().as_default():
		cm = tf.confusion_matrix(test['output'], pred)
		with tf.Session() as session:
			cm_out = session.run(cm)
	# Normalize the confusion matrix so that each row sums to 1.
	cm_out = cm_out.astype(float) / cm_out.sum(axis=1)[:, np.newaxis]

	sns.heatmap(cm_out, annot=True, xticklabels=LABELS, yticklabels=LABELS);
	plt.xlabel("Predicted");
	plt.ylabel("Actual");
	plt.show()
