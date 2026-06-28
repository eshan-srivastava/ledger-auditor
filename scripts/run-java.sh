#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
source "$ROOT/scripts/lib.sh"

# Usage: ./scripts/run-java.sh [--no-deps]
DEPS="postgres"
[ "${1:-}" == "--no-deps" ] && DEPS=""

if [ -n "$DEPS" ]; then
  docker compose up -d $DEPS
  wait_for_port localhost 5432 30
else
  echo "Running without Postgres — expect connection failures on startup (this is useful for testing failure handling)."
fi

cd "$ROOT/ledger"
export SPRING_PROFILES_ACTIVE="local"
./mvnw spring-boot:run