cd "$(dirname "$0")"
pwd
sh ./es7/installes7.sh

cd "$(dirname "$0")"
pwd
cd flinketl
mvn clean package install
cd ..

cd flinketljob
mvn clean package
cd ..

