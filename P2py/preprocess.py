import csv
import json
# First time only
# import nltk
# nltk.download('stopwords')
import numpy as np
from nltk.corpus import stopwords
from nltk.tokenize import RegexpTokenizer

stop_words = stopwords.words('english')

tokenizer = RegexpTokenizer(r'[A-Za-z]+')

word_index = {
    '<PAD>': 0,
    '<UNK>': 1
}

max_len = 0

def update_word_index(data):
    for word in no_stop:
        if word not in word_index:
            word_index[word] = len(word_index)

def update_max_len(data):
    global max_len
    max_len = max(max_len, len(data))

with open('data/train.csv', 'r') as fin:
    reader = csv.reader(fin, quotechar='"')
    for row in reader:
        data = row[2]
        tokens = [t.lower() for t in tokenizer.tokenize(data)]
        no_stop = [word for word in tokens if word not in stop_words]
        update_word_index(no_stop)
        update_max_len(no_stop)

print(f'max_len = {max_len}')

print('Dumping word index to JSON file')

with open('data/word_index.json', 'w') as fout:
    json.dump(word_index, fout)

print('Finished dumping to data/word_index.json')

def encode_review(review):
    tokens = [t.lower() for t in tokenizer.tokenize(review)]
    no_stop = [word for word in tokens if word not in stop_words]
    encoded = [word_index.get(word, word_index.get('<UNK>'))
               for word in no_stop]
    return encoded

def map_rating(rating):
    rating = float(rating)
    if rating > 5:
        return 1
    else:
        return 0

train_data = []
train_labels = []

with open('data/train.csv', 'r') as fin:
    reader = csv.reader(fin, quotechar='"')
    for row in reader:
        train_data.append(encode_review(row[2]))
        train_labels.append(map_rating(row[3]))

train = {'input': train_data, 'output': train_labels}

print('Dumping train data to JSON file')

with open('data/train.json', 'w') as fout:
    json.dump(train, fout)

print('Finished dumping to data/train.json')

test_data = []
test_labels = []

with open('data/test.csv', 'r') as fin:
    reader = csv.reader(fin, quotechar='"')
    for row in reader:
        test_data.append(encode_review(row[2]))
        test_labels.append(map_rating(row[3]))

test = {'input': test_data, 'output': test_labels}

print('Dumping test data to JSON file')

with open('data/test.json', 'w') as fout:
    json.dump(test, fout)

print('Finished dumping to data/test.json')
