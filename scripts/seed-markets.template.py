#!/usr/bin/env python3
"""
PrediX market-schema — batch create OPEN markets (local demo template)

Usage:
  python3 scripts/seed-markets.template.py

Edit the MARKETS list below, then run. Requires market-schema on :8081.
"""

from __future__ import annotations

import json
import sys
import urllib.error
import urllib.request

BASE = "http://localhost:8081"

# ── 在这里添加/修改市场（复制一行改 title / description / category 即可）──
MARKETS = [
    {
        "title": "Will ETH flip BTC market cap in 2026?",
        "description": "Layer-1 dominance bet",
        "category": "crypto",
    },
    {
        "title": "Will Jakarta MRT Line 3 open before Dec 2026?",
        "description": "Indonesia infra milestone",
        "category": "macro",
    },
    {
        "title": "Will Thailand win AFF Cup 2026?",
        "description": "ASEAN football championship",
        "category": "sports",
    },
    # 复制上面块继续加 ↓
    # {
    #     "title": "你的问题?",
    #     "description": "简短说明",
    #     "category": "politics",  # crypto | macro | sports | politics | environment
    # },
]

# ── 全局默认值（一般不用改）────────────────────────────────────────────────
DEFAULTS = {
    "marketType": "BINARY",
    "chainId": 137,
    "collateralTokenSymbol": "USDC",
    "openTime": "2026-05-23T00:00:00Z",
    "closeTime": "2026-12-31T23:59:59Z",
    "resolveDeadline": "2027-01-31T23:59:59Z",
    "createdBy": "ops@predix.io",
}
ACTOR = "ops@predix.io"


def call(method: str, path: str, body: dict | None = None) -> dict:
    data = json.dumps(body).encode() if body is not None else None
    req = urllib.request.Request(
        f"{BASE}{path}",
        data=data,
        method=method,
        headers={"Content-Type": "application/json"},
    )
    with urllib.request.urlopen(req, timeout=15) as resp:
        return json.loads(resp.read())


def create_open_market(entry: dict) -> str:
    payload = {**DEFAULTS, **entry}
    res = call("POST", "/api/v1/markets", payload)
    market_id = res["data"]["id"]

    call(
        "POST",
        f"/api/v1/markets/{market_id}/outcomes",
        {"outcomeCode": "YES", "outcomeLabel": "Yes"},
    )
    call(
        "POST",
        f"/api/v1/markets/{market_id}/outcomes",
        {"outcomeCode": "NO", "outcomeLabel": "No"},
    )
    call("POST", f"/api/v1/markets/{market_id}/open", {"actor": ACTOR})
    return market_id


def main() -> int:
    try:
        health = call("GET", "/actuator/health")
    except (urllib.error.URLError, urllib.error.HTTPError) as e:
        print(f"ERROR: market-schema not reachable at {BASE}: {e}", file=sys.stderr)
        return 1

    if health.get("status") != "UP":
        print(f"ERROR: health check failed: {health}", file=sys.stderr)
        return 1

    if not MARKETS:
        print("MARKETS list is empty — add entries in the script first.")
        return 1

    for m in MARKETS:
        title = m["title"]
        print(f"→ {title}")
        market_id = create_open_market(m)
        print(f"  ✓ OPEN  id={market_id}")

    total = call("GET", "/api/v1/markets")["data"]["totalElements"]
    print(f"\nDone. Total markets in DB: {total}")
    print("Refresh http://localhost:3001")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
