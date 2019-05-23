import json
import csv
import tensorflow as tf
from tensorflow import keras
from tensorflow.keras import layers

with open('data/word_index.json', 'r') as fin:  
  word_index = json.load(fin)

with open('data/train.json') as fin:
	train = json.load(fin)

train_data = train['input']
train_labels = train['output']

max_len = 937

train_data = keras.preprocessing.sequence.pad_sequences(train_data,
													value=word_index["<PAD>"],
													padding='post',
													maxlen=max_len)

embedding_dim = 128

model = keras.Sequential([
  layers.Embedding(len(word_index), embedding_dim, input_length=max_len),
  #layers.Conv1D(20, 3, padding='valid', activation='relu', strides=1),
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
  callbacks=[keras.callbacks.ModelCheckpoint(filepath='saved_models/model-noconv1d-e128.h5', save_best_only=True)])

