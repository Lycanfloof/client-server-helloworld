#!/bin/bash
for i in {1..300}
do
    java -jar ./client/build/libs/client.jar rff rff.txt &
done
