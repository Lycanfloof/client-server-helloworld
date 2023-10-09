#!/bin/bash
for i in {1..50}
do
    java -jar ./client/build/libs/client.jar rff rff.txt &
done
