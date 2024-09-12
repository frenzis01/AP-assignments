#!/bin/bash

# This script renames .raj to .jar and viceversa

# Rename .raj files to .tmp
for file in *.raj; do
    if [ -f "$file" ]; then
        mv "$file" "${file%.raj}.tmp"
    fi
done

# Rename .jar files to .raj
for file in *.jar; do
    if [ -f "$file" ]; then
        mv "$file" "${file%.jar}.raj"
    fi
done

# Rename .tmp files to .jar
for file in *.tmp; do
    if [ -f "$file" ]; then
        mv "$file" "${file%.tmp}.jar"
    fi
done

