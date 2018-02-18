#!/usr/bin/env sh
cd `dirname $0`/../data
echo "shut down of kumar"
# If you don't want to wait, just run this concurrently
kill $(cat kumar.pid 2>/dev/null) 2>/dev/null
if [ $? -eq 0 ]; then while [ -f "kumar.pid" ]; do sleep 1; done; fi;
rm -f kumar.pid 2>/dev/null
echo "kumar has been terminated"
