#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
source "$ROOT/scripts/lib.sh"

# Usage: ./scripts/run-seeder.sh [--no-ledger]
# Seeder needs Postgres always; --no-ledger skips waiting on the Java API
# (useful if your seeder writes directly via PG_URL and only optionally
# calls the ledger API).
WAIT_LEDGER=true
[ "${1:-}" == "--no-ledger" ] && WAIT_LEDGER=false

docker compose up -d postgres
wait_for_port localhost 5432 30

if [ "$WAIT_LEDGER" = true ]; then
  wait_for_port localhost 8080 30
fi

cd "$ROOT/data-seeder"
export PG_URL="postgres://ledgeruser:ledgerpass@localhost:5432/ledgerdb?sslmode=disable"
export LEDGER_API_URL="http://localhost:8080/api"

# Whatever the seeder's native entrypoint is — go run, python main.py, etc.
# Adjust this line once you confirm the seeder's actual language/entrypoint.
go run ./cmd/seeder