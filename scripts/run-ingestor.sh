#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
source "$ROOT/scripts/lib.sh"

# Usage: ./scripts/run-ingestor.sh [--with-ledger]
# By default, runs standalone with no Java backend reachable — useful for
# exercising poller retry/backoff against a dead upstream. Pass --with-ledger
# to bring up the full chain (postgres + ledger) for an end-to-end run.
WITH_LEDGER=false
[ "${1:-}" == "--with-ledger" ] && WITH_LEDGER=true

if [ "$WITH_LEDGER" = true ]; then
  docker compose up -d postgres
  wait_for_port localhost 5432 30
  docker compose up -d ledger
  wait_for_port localhost 8080 60   # ledger has a slower healthcheck start_period
else
  echo "Running without ledger backend — expect connection refused / retry behavior on startup."
fi

cd "$ROOT/ingestor-go"

export LEDGER_API_URL="http://localhost:8080/api"
export SQLITE_PATH="./local-data/mirror.db"

mkdir -p local-data
go run ./cmd/ingestor