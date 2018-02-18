#!/usr/bin/env sh
cd `dirname $0`/..
echo "re-starting kumar"
bin/stop.sh
bin/start.sh
