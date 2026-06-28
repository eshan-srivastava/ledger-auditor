#!/usr/bin/env bash
set -euo pipefail

wait_for_port() {
  local host=$1 port=$2 timeout=${3:-30}
  echo "Waiting for $host:$port..."
  for i in $(seq 1 "$timeout"); do
    if (exec 3<>"/dev/tcp/$host/$port") 2>/dev/null; then
      exec 3>&-
      echo "$host:$port is up."
      return 0
    fi
    sleep 1
  done
  echo "Timed out waiting for $host:$port" >&2
  return 1
}

# Repo root, regardless of where a script is invoked from
repo_root() {
  cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd
}