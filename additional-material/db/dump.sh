#!/bin/bash

rm -f dreimt-schema.sql dreimt-data.sql

sudo mysqldump --no-data dreimt > dreimt-schema.sql
sudo mysqldump --no-create-info dreimt > dreimt-data.sql
