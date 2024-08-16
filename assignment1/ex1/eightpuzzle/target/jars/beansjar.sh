#!/bin/sh
echo "!!! execute this from the folder inside beansjar.sh !!!"
CLASSES="unipi/eightpuzzle"

cd ../classes || { echo "Directory 'classes/unipi/eightpuzzle' not found. Exiting."; exit 1; }


jar cfv ../jars/Flip.jar $CLASSES/Flip.class $CLASSES/Flip'$'1.class
jar cfv ../jars/EightTile.jar $CLASSES/EightTile.class $CLASSES/utils/IntWrapper.class
jar cfv ../jars/EightController.jar $CLASSES/EightController.class $CLASSES/utils/IntWrapper.class
jar cfvm ../jars/EightBoard.jar ../jars/MANIFEST.MF \
 $CLASSES/EightBoard.class \
 $CLASSES/EightBoard'$'1.class \
 $CLASSES/EightBoard'$'2.class \
 $CLASSES/EightController.class \
 $CLASSES/utils/IntWrapper.class \
 $CLASSES/EightTile.class \
 $CLASSES/Flip.class \
 $CLASSES/Flip'$'1.class


