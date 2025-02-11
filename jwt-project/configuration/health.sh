#!/bin/bash

curl --noproxy '*' -s http://localhost:8080/metrics/81CkCBotp2sRb48-IHzMaHXpUGYwHWOjQrFkmzfeyIKEGf6ajw7Cgwxyvayr9FKZ/healthcheck |grep false

if [ $? == 1 ]; then
  exit 0
fi

exit 1
