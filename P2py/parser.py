import csv
import sys

if len(sys.argv) != 2:
	print('usage: python3 parser.py <file_name>')
else:
	filename = sys.argv[1]
	# read tab-delimited file
	with open(filename,'r') as fin:
		cr = csv.reader(fin, delimiter='\t', quotechar='"')
		filecontents = [line for line in cr]

	# write comma-delimited file (comma is the default delimiter)
	with open(filename.replace('.tsv', '.csv'),'w') as fou:
		cw = csv.writer(fou, quotechar='"', quoting=csv.QUOTE_NONNUMERIC, escapechar='\\')
		cw.writerows(filecontents)
