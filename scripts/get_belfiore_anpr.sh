#!/bin/sh

CSVURL=https://raw.githubusercontent.com/italia/anpr/master/src/archivi/ANPR_archivio_comuni.csv
TEMP_FILE=/tmp/anpr.csv

curl $CSVURL -o $TEMP_FILE

head -n1 $TEMP_FILE && (tail -n+2 $TEMP_FILE | sort -t"," -k5,5 -k3r,3 | awk -F, '!seen[$5]++' | awk -F, '!seen[$6, $15]++' | awk -F, 'BEGIN{OFS = ","}{print $5,$6,$15}')



