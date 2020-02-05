#!/bin/bash
CODE_LOCATION="/d/Development/JavaDevelopment/Trading"
DOCKER_BUILD_LOCATION="/d/Software/Docker_Build"
JAR_NAME="autoBinary-1.0-SNAPSHOT.jar"
DOCKER_TAG="autobinary"
DOCKER_REPOSITORY="suneeshjoshi/binary"
DOCKER_TAG_TO_DEPLOY="latest"

cd ${CODE_LOCATION}
mvn -U clean package -DskipTests
cp target/${JAR_NAME} ${DOCKER_BUILD_LOCATION}


# Build Docker and publish and test
cd ${DOCKER_BUILD_LOCATION}
docker build -t ${DOCKER_TAG} .
docker tag `docker images | grep ${DOCKER_TAG} | awk -F' ' '{print $3}'` ${DOCKER_REPOSITORY}
docker push ${DOCKER_REPOSITORY}:${DOCKER_TAG_TO_DEPLOY}
docker run ${DOCKER_REPOSITORY}

