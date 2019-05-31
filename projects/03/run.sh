#/bin/bash

mvn clean
mvn package
java -jar target/proyecto3.jar -o output_dir ./test_files/*.txt ./test_files/hombres_necios
# java -jar target/proyecto3.jar -o output_dir ./test_files/hombres_necios
