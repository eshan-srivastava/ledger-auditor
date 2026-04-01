# Legacy-Bridge – Engineering Handover & Architectural Rationale

This document captures the **intent, reasoning, and trade-offs** behind the Legacy-Bridge project.
It exists to preserve architectural context for future maintainers and to clearly explain why certain
decisions were made—and why others were intentionally avoided.

This is not a product specification. It is an engineering rationale.

---

## 0. Executive Summary

Legacy-Bridge demonstrates a **safe-path modernization strategy** for a legacy financial ledger system.

It balances:

- the **non-negotiable correctness** of a Java/Spring Boot monolith,
- the **high-throughput and low-latency strengths** of Go-based sidecar services,
- and a **controlled, safety-first AI layer** used strictly for analysis and auditing.

The system is designed for environments where:

- zero data loss is mandatory,
- downtime is unacceptable,
- and modernization must happen incrementally rather than via rewrites.

---

## 1. Why This Project Exists

Most real-world systems are not greenfield.
They evolve under constraints such as:

- legacy codebases,
- business-critical correctness,
- and organizational risk aversion.

This project exists to demonstrate:

- how legacy systems can be extended safely,
- how new services can coexist without destabilizing the core,
- and how AI can be introduced without contaminating business logic.

The goal is not feature richness.
The goal is **trustworthy evolution**.

---

## 2. Why a Java Monolith Is the Source of Truth

Java + Spring Boot was deliberately chosen to represent a conservative, enterprise-grade system.

Reasons:

- Financial ledgers require strong ACID guarantees
- ORM-based modeling reflects real enterprise systems
- Spring Security mirrors real access-control patterns
- Such systems are often considered “too risky to rewrite”

The monolith is intentionally:

- slower,
- verbose,
- and stable.

This is not a weakness—it is the foundation.

---

## 3. Why Go Exists as a Sidecar, Not a Replacement

Go is introduced as a **read-only sidecar**, not a rewrite candidate.

Reasons:

- Real migrations rarely replace legacy systems upfront
- Sidecars minimize blast radius
- Go excels at stateless, high-throughput workloads

The Go service:

- reads data from the monolith,
- never mutates primary state,
- and shields the core system from analytical and exploratory workloads.

This reflects how modernization happens in regulated or high-risk domains.

---

## 4. Data Flow & CDC Strategy (Intentionally Simplified)

True Change Data Capture (CDC) frameworks add significant operational complexity.

For this project:

- Incremental polling
- Timestamp-based ingestion
- Explicit versioned APIs

are sufficient to demonstrate the **conceptual correctness** of CDC without infrastructure bloat.

The focus is on **data correctness and flow**, not tooling mastery.

---

## 5. AI as an Edge Consumer (Not an Authority)

The AI service is intentionally isolated from core business logic.

Principles:

- AI never writes data
- AI never makes autonomous decisions
- AI must call existing backend services
- AI outputs must be structured and auditable

This placement mirrors real-world adoption:
AI is introduced cautiously, at the edges, where mistakes are survivable.

---

## 6. AI Output Validation & Guardrails

To reduce the risk of hallucinated or misleading outputs, the AI service implements
a **source-of-truth alignment step**.

Workflow:

1. The AI generates a structured recommendation
2. It extracts the assumptions used (e.g. totals, categories)
3. These assumptions are validated against raw data fetched from the Go read-model
4. If validation fails, the result is marked as inconclusive

This ensures that insights are:

- explainable,
- verifiable,
- and never detached from actual data.

This is a safety mechanism, not a guarantee of correctness.

---

## 7. Reconciliation & Data Integrity Strategy

In real migrations, the greatest risk is not latency—it is silent data divergence.

To address this, the system includes a **Reconciliation Utility**:

- A background, read-only auditor
- Periodically compares a random sample of records
- Verifies parity between:
  - the Java source of truth
  - and the Go read-model

This process:

- is not on the critical path,
- does not block system operation,
- and exists purely to build trust.

The goal is not perfection—it is early detection.

---

## 8. Contract Safety & Downstream Stability

Downstream services are fragile when they depend directly on database schemas.

To mitigate this:

- The monolith exposes **versioned, read-only DTOs**
- The Go service depends only on these contracts
- Schema evolution is absorbed within the monolith

This prioritizes:

- team autonomy,
- backward compatibility,
- and safe parallel development.

Raw schema access is intentionally avoided.

---

## 9. Explicit Trade-offs

### Accepted

- Limited scalability
- Simplified authentication
- Synthetic data
- Single-pod deployment

### Avoided

- Feature bloat
- Regulatory complexity
- Distributed transactions
- Over-engineered infrastructure

These trade-offs are deliberate and documented.

---

## 10. Deployment Philosophy

The system is deployed as a **single Kubernetes pod** containing:

- Java Core Ledger container
- Go Mirror & Validator container
- AI Intelligence container

This reflects migration-phase deployments where:

- services are logically separate,
- but operationally coupled for simplicity.

The architecture remains modular even when co-located.

---

## 11. Performance Benchmarking & the Latency Narrative

Claims about performance must be measurable.

The project includes a `BENCHMARK.md` document that:

- compares a complex analytical query
- executed via the Java monolith
- versus the Go read-model

Metrics:

- median latency
- P95 latency
- identical dataset

This turns architectural decisions into data-backed outcomes.

---

## 12. Final Note

This project is designed to answer one question clearly:

> Can this engineer safely build, extend, and modernize real systems?

Every decision exists in service of that question.
