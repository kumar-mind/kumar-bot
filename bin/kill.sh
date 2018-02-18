#!/usr/bin/env sh
cd `dirname $0`/../data
kill -9 `cat kumar.pid` 2>/dev/null
rm -f kumar.pid 2>/dev/null


