#!/bin/bash

rm -f dreimt-schema.sql dreimt-data.sql

sudo mysqldump --no-data dreimt | sed 's/ AUTO_INCREMENT=[0-9]*//g' > dreimt-schema.sql
sudo mysqldump --no-create-info dreimt > dreimt-data.sql
