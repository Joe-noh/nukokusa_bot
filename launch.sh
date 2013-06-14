#!/bin/bash

pid=`ps x | grep -v grep | grep sbt | awk '{ print $1 }'`
kill $pid

nohup sbt run &
