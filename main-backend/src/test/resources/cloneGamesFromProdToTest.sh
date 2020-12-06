#!/usr/bin/env bash

PROD_CONNECTION_STRING=$1
SOURCE_DB="GameEye"
DESTINATION_DB="GameEyeTest"

export_import_collection() {
  collection=$1
  inputFile="${collection}_temp.json"
  mongoexport --collection=$collection --db=$SOURCE_DB --out=$inputFile --uri=$PROD_CONNECTION_STRING
  mongoimport --collection=$collection --db=$DESTINATION_DB --file=$inputFile --uri=$PROD_CONNECTION_STRING
  rm $inputFile
}

export_import_collection games