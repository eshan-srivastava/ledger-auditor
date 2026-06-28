#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
source "$ROOT/scripts/lib.sh"

# Usage: ./scripts/run-python.sh [--no-deps]
DEPS="postgres"
[ "${1:-}" == "--no-deps" ] && DEPS=""

if [ -n "$DEPS" ]; then
  docker compose up -d $DEPS
  wait_for_port localhost 5432 30
else
  echo "Running without Postgres — expect connection failures on startup."
fi

cd "$ROOT/ai"

export DATABASE_URL="postgresql+asyncpg://ledgeruser:ledgerpass@localhost:5432/ledger_ai"
# TODO: CHANGE THIS TO GO INGESTOR URL
export LEDGER_API_URL="http://localhost:8080/api"

uv run uvicorn app.main:app --reload --port 8000