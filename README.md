# Legacy-Bridge: Financial Ledger Modernization & Intelligence Platform

Legacy-Bridge is a backend-focused project that demonstrates how a traditional,
transactional Java system can be safely extended using Go-based sidecars and a
controlled AI intelligence layer.

It models a **personal financial ledger** and showcases realistic migration,
verification, and analysis patterns used in enterprise environments.

This is **not a banking product**.

---

## Executive Summary

This project demonstrates a **safe-path modernization strategy** for a legacy
financial ledger.

It balances:

- the correctness and stability of Java/Spring Boot,
- the performance advantages of Go sidecar services,
- and a safety-first AI layer used for analysis and auditing.

The system is designed for scenarios where **zero data loss** and **incremental
evolution** are the primary constraints.

---

## Scope

### In Scope

- User-owned accounts
- Immutable financial transactions
- Deterministic balance calculation
- Read-only data mirroring
- Reconciliation and data integrity checks
- Performance benchmarking
- AI-driven, structured insights

### Outer Limit

The system ends at **analysis and recommendations**.

It does not:

- move money
- integrate with banks
- execute payments
- provide financial advice

---

## Non-Goals

- Payments, transfers, cards, UPI
- Interest, tax, or compliance logic
- Multi-currency support
- Distributed transactions
- Free-form AI chat interfaces
- Complex frontend applications
- Production-grade DevOps tooling

These are intentionally excluded.

---

## Architecture Overview

The system consists of three services with strict responsibilities.

### 1. Core Ledger (Java / Spring Boot)

**Source of Truth**

- Manages users, accounts, and transactions
- Guarantees data correctness and auditability
- Exposes versioned, read-only APIs

---

### 2. Mirror & Validator (Golang)

**High-Performance Sidecar**

- Incrementally ingests ledger data
- Maintains read-optimized models
- Performs fast validations
- Supports reconciliation and benchmarking

---

### 3. Intelligence Service (AI + Vector DB)

**Analysis & Insight Layer**

- Vectorizes transaction descriptions
- Identifies patterns and clusters
- Produces structured, verifiable insights
- Validates outputs against raw data

---

## Deployment Strategy

The system is designed to run as:

- **Single Kubernetes Pod**
  - Core Ledger container
  - Go Sidecar container
  - AI Intelligence container

or locally via Docker Compose.

This keeps operational complexity low while preserving architectural clarity.

---

## Design Philosophy

- Correctness over cleverness
- Explicit scope boundaries
- Measurable performance claims
- AI as a consumer, not an authority

---

## Disclaimer

All data is synthetic.  
This project does not provide financial advice and is intended solely for
demonstrating backend system design and modernization strategies.
