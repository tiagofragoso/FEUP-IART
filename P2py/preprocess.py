import csv
import json

# First time only
# import nltk
# nltk.download('stopwords')

from nltk.corpus import stopwords
from nltk.tokenize import RegexpTokenizer, sent_tokenize

stop_words = stopwords.words('english')

tokenizer = RegexpTokenizer(r'[A-Za-z]+')

word_index = {
    '<PAD>': 0,
    '<START>': 1,
    '<UNK>': 2,  # unknown
    '<UNUSED>': 3
}

max_len = 0


def update_word_index(data):
    for word in no_stop:
        if word not in word_index:
            word_index[word] = len(word_index)


def pad_sentence(data):
    global max_len
    max_len = max(max_len, len(data))


with open('data/train.csv', 'r') as fin:
    reader = csv.reader(fin, quotechar='"')
    for row in reader:
        data = row[2]
        tokens = [t.lower() for t in tokenizer.tokenize(data)]
        no_stop = [word for word in tokens if word not in stop_words]
        update_word_index(no_stop)
        pad_sentence(no_stop)
        # s_tokens = [tokenizer.tokenize(s) for s in sent_tokenize(data)]
        # for s in s_tokens:
        #     lower_words = [word.lower() for word in s]
        #     no_stop = [word for word in lower_words if word not in stop_words]
        #     update_word_index(no_stop)
        #     pad_sentence(no_stop)

print(max_len)

print('Dumping result to JSON file')

with open('data/word_index.json', 'w') as fout:
    json.dump(word_index, fout)

print('Finished dumping to data/word_index.json')


# if word in word_index:
# 	word_index[word] = word_index[word] + 1
# else:
# 	word_index[word] = 0

# print(f'{len(word_index)} different words')
# best_word = ""
# best_count = 0

# for w, c in word_index.items():
# 	if c > best_count:
# 		best_count = c
# 		best_word = w

# print(f'Most frequent word is {best_word} with {best_count} occurences')
