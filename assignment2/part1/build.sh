#!/bin/bash
# Stop on any command failure
set -e

javac -d out strategyjobscheduler/*.java
javac -d out anagramcounter/*.java
jar cfm anagramcounter.jar manifest.mf -C out .
cp anagramcounter.jar anagramcounter.arj 