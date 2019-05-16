import json
import csv
import tensorflow as tf
from tensorflow import keras
from tensorflow.keras import layers
from nltk.corpus import stopwords
from nltk.tokenize import RegexpTokenizer, sent_tokenize

stop_words = stopwords.words('english')

tokenizer = RegexpTokenizer(r'[A-Za-z]+')

with open('data/word_index.json', 'r') as fin:  
    word_index = json.load(fin)

def encode_review(review):
	tokens = [t.lower() for t in tokenizer.tokenize(review)]
	no_stop = [word for word in tokens if word not in stop_words]
	encoded = [word_index.get(word, word_index.get('<UNK>')) for word in no_stop]
	return encoded

def map_rating(rating):
	rating = float(rating)
	if rating >= 5:
		return 1
	else:
		return 0
	# if rating <= 4:
	# 	return -1
	# elif rating < 7:
	# 	return 0
	# else:
	# 	return 1

test_data = []
test_labels = []

with open('data/test.csv', 'r') as fin:
	cr = csv.reader(fin)
	for row in cr:
		test_data.append(encode_review(row[2]))
		test_labels.append(map_rating(row[3]))

print(test_data[6])

max_len = 937

# for i in range(1, len(test_data)):
# 	print(i)
# 	data = test_data[:i]
# 	keras.preprocessing.sequence.pad_sequences(data,
#                                                         value=word_index["<PAD>"],
#                                                         padding='post',
#                                                         maxlen=max_len)

test_data = keras.preprocessing.sequence.pad_sequences(test_data,
													value=word_index["<PAD>"],
													padding='post',
													maxlen=max_len)

model = keras.models.load_model('saved_models/model-noconv1d.h5')
metrics = model.evaluate(test_data, test_labels)
print(f'{model.metrics_names[0]}: {metrics[0]}')
print(f'{model.metrics_names[1]}: {metrics[1]}')