#!/bin/bash
docker pull suneeshjoshi/binary
docker container stop `docker inspect binaryApplication | grep Id | awk -F'"' '{print $4}'`
docker container rm `docker inspect binaryApplication | grep Id | awk -F'"' '{print $4}'`
docker run  --name binaryApplication -t -d --restart always suneeshjoshi/binary
