#!/bin/bash
for i in {1..4}
do
    java -jar client.jar rff rff.txt 24000
done

exit 0
