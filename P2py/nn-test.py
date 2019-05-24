import argparse
import json
import tensorflow as tf
from tensorflow import keras
from tensorflow.keras import layers
import numpy as np
import seaborn as sns
import matplotlib.pyplot as plt
from nltk.corpus import stopwords
from nltk.tokenize import RegexpTokenizer

stop_words = stopwords.words('english')
tokenizer = RegexpTokenizer(r'[A-Za-z]+')

parser = argparse.ArgumentParser()
parser.add_argument('-m', '--modelName', help='model name to load', default='model-conv1d-e128')
parser.add_argument('-p', '--predict', help='make a prediction', required=False)
parser.add_argument('-c', '--confusion', help='confusion matrix', default=False, const=True, nargs='?')
args = parser.parse_args()
args = vars(args)

def encode_review(review):
    tokens = [t.lower() for t in tokenizer.tokenize(review)]
    no_stop = [word for word in tokens if word not in stop_words]
    encoded = [word_index.get(word, word_index.get('<UNK>'))
               for word in no_stop]
    return encoded

with open('data/word_index.json', 'r') as fin:  
  word_index = json.load(fin)

if (not args['predict']):
	with open('data/test.json') as fin:
		test = json.load(fin)

	test_data = test['input']
	test_labels = test['output']
else:
	test_data = [encode_review(args['predict'])]

LABELS = [
	"Negative", "Positive"
]

max_len = 937

test_data = keras.preprocessing.sequence.pad_sequences(test_data,
													value=word_index["<PAD>"],
													padding='post',
													maxlen=max_len)

modelPath = 'saved_models/' + args['modelName'] + '.h5';
model = keras.models.load_model(modelPath)
if (args['predict']):
	review = args['predict']
	pred = model.predict(test_data)
	label = LABELS[1 if pred >= 0.5 else 0]
	print(f'Prediction for review "{review}": {label}')
elif (not args['confusion']):
	metrics = model.evaluate(test_data, test_labels)
	print(f'{model.metrics_names[0]}: {metrics[0]}')
	print(f'{model.metrics_names[1]}: {metrics[1]}')
else:
	predictions = model.predict(test_data)
	predictions = [ 1 if x >= 0.5 else 0 for x in predictions ]

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