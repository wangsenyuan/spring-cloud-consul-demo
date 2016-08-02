#!/bin/bash

set -e

wait_single_host() {
  local host=$1
  shift
  local port=$1
  shift

  echo "waiting for TCP connection to $host:$port..."

  while ! nc ${host} ${port} > /dev/null 2>&1 < /dev/null
  do
     echo "TCP connection  [$host] not ready, will try again..."
     sleep 1
  done

  echo "TCP connection ready. Executing command [$host] now..."
}

wait_all_hosts() {
  if [ ! -z "$WAIT_FOR_HOSTS" ]; then
    local separator=':'
    for _HOST in $WAIT_FOR_HOSTS ; do
        IFS="${separator}" read -ra _HOST_PARTS <<< "$_HOST"
        wait_single_host "${_HOST_PARTS[0]}" "${_HOST_PARTS[1]}"
    done
  else
    echo "IMPORTANT : Waiting for nothing because no $WAIT_FOR_HOSTS env var defined !!!"
  fi
}

wait_all_hosts

exec $1
