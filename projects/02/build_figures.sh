#!/bin/bash

set -e
set -o pipefail

TEST_FILES_DIR="test_files"

# Build executable
mvn clean
mvn package

# Move to test files directory
cd $TEST_FILES_DIR

for ds in $(find *.txt -type f); do
    printf "Reading file: %s" $ds
    java -jar ../target/dsdrawer.jar $ds > ${ds/txt/svg}
    printf "\n\tSVG generated successfully: %s\n\n" ${ds/txt/svg}
done

cd ..