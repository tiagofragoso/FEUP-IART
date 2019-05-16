import json
import csv
import tensorflow as tf
from tensorflow import keras
from tensorflow.keras import layers
from nltk.corpus import stopwords
from nltk.tokenize import RegexpTokenizer, sent_tokenize
from sklearn.preprocessing import MultiLabelBinarizer

stop_words = stopwords.words('english')

tokenizer = RegexpTokenizer(r'[A-Za-z]+')

with open('data/word_index.json', 'r') as fin:  
    word_index = json.load(fin)

def encode_review(review):
	tokens = [t.lower() for t in tokenizer.tokenize(review)]
	no_stop = [word for word in tokens if word not in stop_words]
	encoded = [word_index.get(word) for word in no_stop]
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

train_data = []
train_labels = []

with open('data/train.csv', 'r') as fin:
	cr = csv.reader(fin)
	for row in cr:
		train_data.append(encode_review(row[2]))
		train_labels.append(map_rating(row[3]))
		#train_labels.append(row[2])

# multilabel_binarizer = MultiLabelBinarizer()
# multilabel_binarizer.fit(train_labels)
# train_labels = multilabel_binarizer.classes_


max_len = 937

train_data = keras.preprocessing.sequence.pad_sequences(train_data,
                                                        value=word_index["<PAD>"],
                                                        padding='post',
                                                        maxlen=max_len)


embedding_dim=16

model = keras.Sequential([
  layers.Embedding(len(word_index), embedding_dim, input_length=max_len),
  layers.Conv1D(20, 3, padding='valid', activation='relu', strides=1),
  layers.GlobalAveragePooling1D(),
  layers.Dense(16, activation='relu'),
  layers.Dense(1, activation='sigmoid')
])

model.compile(optimizer='adam',
              loss='binary_crossentropy',
              metrics=['accuracy'])

history = model.fit(
    train_data,
    train_labels,
    epochs=30,
    batch_size=512,
    validation_split=0.1,
	callbacks=[keras.callbacks.ModelCheckpoint(filepath='saved_models/model-ml-conv1d.h5', save_best_only=True)])


# tf.enable_eager_execution()

# dataset = tf.data.experimental.CsvDataset(
# 	['data/traincpy.csv'], 
# 	[tf.string, tf.string, tf.string, tf.float32, tf.string, tf.int32],
# 	header=True,
# )
	