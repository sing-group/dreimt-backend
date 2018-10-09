#!/bin/bash

sudo mysqldump --no-data dreimt > dreimt-schema.sql
sudo mysqldump --no-create-info dreimt > dreimt-data.sql
