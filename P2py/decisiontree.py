import json
import time
import graphviz
from sklearn import tree
from tensorflow import keras

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
print("{}{:.2f}s".format('Runtime(train): ', runtime))
t = time.time()
acc = clf.score(test_data, test['output'])
runtime = time.time() - t
print("{}{:.2f}s".format('Runtime(test): ', runtime))
print(f'Accuracy: {acc}')