#!/usr/bin/env sh
cd `dirname $0`/..
echo "loading latest code changes"
git pull origin master
echo "clean up"
ant clean
echo "building kumar"
ant
bin/restart.sh
