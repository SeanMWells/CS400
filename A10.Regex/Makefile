p1: # occurrances of the word Comedy in this file
	grep -P "Comedy" movies.csv
p2: # prices that end with the digit 2
	grep -P "^[0-9]+2," cars.csv
p3: # songs with two word titles
	grep -P "^\w+\s+\w+," songs.csv
p4: # ingredients with 1000 or more calories per 100 grams
	grep -P "[1-9]\d\d\d\scal,\d+\skJ$\" ingredients.csv
p5: # meteorites with year 20XX where XX is any number
	grep -P "(\w+,){6}20\d\d," meteorites.csv
