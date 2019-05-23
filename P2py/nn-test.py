import argparse
import json
import tensorflow as tf
from tensorflow import keras
from tensorflow.keras import layers
import numpy as np
import seaborn as sns
import matplotlib.pyplot as plt

parser = argparse.ArgumentParser()
parser.add_argument('modelName', help='model name to load')
parser.add_argument('-c', '--confusion', help='model name to load', default=False, const=True, nargs='?')
args = parser.parse_args()
args = vars(args)

with open('data/word_index.json', 'r') as fin:  
  word_index = json.load(fin)

with open('data/test.json') as fin:
	test = json.load(fin)

test_data = test['input']
test_labels = test['output']

max_len = 937

test_data = keras.preprocessing.sequence.pad_sequences(test_data,
													value=word_index["<PAD>"],
													padding='post',
													maxlen=max_len)

modelPath = 'saved_models/' + args['modelName'] + '.h5';
model = keras.models.load_model(modelPath)
if (not args['confusion']):
	metrics = model.evaluate(test_data, test_labels)
	print(f'{model.metrics_names[0]}: {metrics[0]}')
	print(f'{model.metrics_names[1]}: {metrics[1]}')
else:
	predictions = model.predict(test_data)
	predictions = [ 1 if x >= 0.5 else 0 for x in predictions ]

	LABELS = [
		"Negative", "Positive"
	]

	# Create a confusion matrix on training data.
	with tf.Graph().as_default():
		cm = tf.confusion_matrix(test_labels, predictions)
		with tf.Session() as session:
			cm_out = session.run(cm)
	# Normalize the confusion matrix so that each row sums to 1.
	cm_out = cm_out.astype(float) / cm_out.sum(axis=1)[:, np.newaxis]

	sns.heatmap(cm_out, annot=True, xticklabels=LABELS, yticklabels=LABELS);
	plt.xlabel("Predicted");
	plt.ylabel("Actual");
	plt.show()