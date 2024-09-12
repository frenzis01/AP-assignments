#!/bin/sh
echo "!!! execute this from the folder inside beansjar.sh !!!"
CLASSES="unipi/eightpuzzle"

cd ../target/classes || { echo "Directory 'classes/unipi/eightpuzzle' not found. Exiting."; exit 1; }


jar cfv ../../dist/Flip.jar $CLASSES/Flip.class $CLASSES/Flip'$'1.class
jar cfv ../../dist/EightTile.jar $CLASSES/EightTile.class $CLASSES/utils/IntWrapper.class
jar cfv ../../dist/EightController.jar $CLASSES/EightController.class $CLASSES/utils/IntWrapper.class
jar cfvm ../../dist/EightBoard.jar ../../dist/MANIFEST.MF \
 $CLASSES/EightBoard.class \
 $CLASSES/EightBoard'$'1.class \
 $CLASSES/EightBoard'$'2.class \
 $CLASSES/EightController.class \
 $CLASSES/utils/IntWrapper.class \
 $CLASSES/EightTile.class \
 $CLASSES/Flip.class \
 $CLASSES/Flip'$'1.class

chmod +x ../../dist/EightBoard.jar
