#!/bin/bash

pid=`ps x | grep -v grep | grep sbt | awk '{ print $1 }'`
kill $pid

sleep 10

nohup sbt run &
