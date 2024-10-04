SHELL := /bin/env bash

p1:
	cat midwestConsumerPriceIndex.csv | tail -n+2 | cut -d',' -f5 | sort -n -r | tail -n1 | xargs printf "grep %s midwestConsumerPriceIndex.csv" | bash

p2:
	cat midwestConsumerPriceIndex.csv | tail -n+2 | head | cut -d',' -f3 | paste -sd+ | xargs printf "scale=2; ( %s )/10\n" | bc
