# Milestone 01 Acceptance Criteria

## Purpose

Define the authoritative acceptance criteria for Milestone 01 (Engine Proof of Concept) and establish traceable links between PRD requirements and implementation stories.

Milestone 01 scope is limited to:

- JVM J-engine binding
- Simple expression evaluation
- Reset behavior
- Session/lifecycle and execution-model guardrails needed for reliable engine behavior

## PRD Traceability

| PRD Section | Requirement Focus | Covered AC IDs |
| --- | --- | --- |
| 3.1 Core Feature: Stateful REPL | Single interpreter per session; no persistence across restarts | M1-AC-002, M1-AC-003 |
| 3.5 Reset Behavior | Destroy/reinitialize interpreter; clear REPL state | M1-AC-011, M1-AC-012, M1-AC-013 |
| 3.6 Execution Model | Serialized eval; off-UI-thread execution | M1-AC-008, M1-AC-009 |
| 6.2 JEngine Interface | Shared engine contract (`eval/reset/shutdown`) | M1-AC-001 |
| 6.3 JResult Model | Result contract for output/error signaling | M1-AC-001, M1-AC-010 |
| 6.4 Threading Rules | Single-threaded engine access; non-blocking UI | M1-AC-008, M1-AC-009 |
| 10 `Milestone 1 — Engine Proof of Concept` | Bind JVM engine, evaluate simple expressions, reset working | M1-AC-004 through M1-AC-013 |
| 11 Definition of Done (v1, relevant subset) | Reliable reset, no interpreter leaks, stable error handling | M1-AC-010, M1-AC-012, M1-AC-014 |

## Milestone 01 AC List

- **M1-AC-001**: Shared engine contract for `eval/reset/shutdown` and result model is explicitly defined for common/shared layer.
- **M1-AC-002**: Exactly one interpreter instance is active per app session.
- **M1-AC-003**: Interpreter state is not persisted across app restarts.
- **M1-AC-004**: JVM engine binding initializes embedded runtime successfully.
- **M1-AC-005**: JVM init/binding failures are surfaced as controlled errors without app crash.
- **M1-AC-006**: JVM-specific implementation is isolated behind shared interface boundary.
- **M1-AC-007**: Simple J expressions evaluate and return output text correctly.
- **M1-AC-008**: Evaluations are serialized; no concurrent engine execution occurs.
- **M1-AC-009**: Evaluation is executed off the UI thread.
- **M1-AC-010**: J error text is preserved verbatim and interpreter stays usable after errors.
- **M1-AC-011**: Reset destroys current interpreter instance.
- **M1-AC-012**: Reset creates a fresh interpreter with cleared state.
- **M1-AC-013**: Reset clears input/output immediately at UI/feature level.
- **M1-AC-014**: Session lifecycle includes deterministic shutdown behavior.
- **M1-AC-015**: Milestone 01 test coverage includes eval success, eval error, serialization, and reset behavior.

## Verification Guidance

| AC ID | Primary Verification Type | Evidence Expectation |
| --- | --- | --- |
| M1-AC-001 | Design + unit tests | Shared contract and model in common code with compile-time usage from platform implementation |
| M1-AC-002 | Integration test | Session creates one engine instance and reuses it across commands |
| M1-AC-003 | Manual + integration test | App restart yields fresh interpreter state (no prior definitions available) |
| M1-AC-004 | Integration test | JVM engine can initialize and execute a known simple expression |
| M1-AC-005 | Integration test | Simulated binding failure returns controlled error path without process crash |
| M1-AC-006 | Code review + compile boundary | JVM implementation remains in `jvmMain`; common code depends on abstraction only |
| M1-AC-007 | Integration test | Known input expressions return expected output text |
| M1-AC-008 | Concurrency test | Parallel submit attempts execute one-at-a-time with deterministic ordering |
| M1-AC-009 | Concurrency/UI responsiveness test | Eval does not block UI thread / main event loop |
| M1-AC-010 | Integration test | Native J error message text is preserved exactly; next eval still works |
| M1-AC-011 | Integration test | Reset tears down active interpreter instance |
| M1-AC-012 | Integration test | Reset recreates fresh interpreter and state does not leak |
| M1-AC-013 | UI behavior test | Input/output buffers clear immediately when reset is triggered |
| M1-AC-014 | Lifecycle test | Shutdown path always runs and leaves no dangling engine session |
| M1-AC-015 | Coverage audit + release gate evidence | Audit case and release gate prove coverage for eval success, eval error, serialization, and reset behavior |

## Milestone-Level AC Ownership

- `M1-AC-015` is owned at milestone level, not by a single story.
- Evidence for `M1-AC-015` must come from:
  - `features/milestone-01-test-cases.md` milestone audit test case.
  - `features/milestone-01-test-plan.md` release-gate execution evidence.
- Story files may reference test cases that contribute evidence, but they do not claim ownership of `M1-AC-015` as a story acceptance criterion.

## Story Mapping

| Story File | Mapped AC IDs |
| --- | --- |
| `milestone-01-story-1-engine-contract-and-session-lifecycle.md` | M1-AC-001, M1-AC-002, M1-AC-003, M1-AC-014 |
| `milestone-01-story-2-jvm-engine-binding-bootstrap.md` | M1-AC-004, M1-AC-005, M1-AC-006 |
| `milestone-01-story-3-simple-expression-evaluation.md` | M1-AC-007, M1-AC-008, M1-AC-009, M1-AC-010 |
| `milestone-01-story-4-reset-reinitializes-engine.md` | M1-AC-011, M1-AC-012, M1-AC-013 |

## Source References

- PRD: `features/J Playground PRD.md` (Sections 3.1, 3.5, 3.6, 6.2, 6.3, 6.4, 10, 11)
- Kotlin Multiplatform source sets: <https://kotlinlang.org/docs/multiplatform/multiplatform-discover-project>
- Kotlin `expect/actual`: <https://kotlinlang.org/docs/multiplatform/multiplatform-expect-actual>
- Shared interfaces and platform implementations: <https://kotlinlang.org/docs/multiplatform/multiplatform-connect-to-apis>
- KMP concurrency FAQ: <https://kotlinlang.org/docs/multiplatform/faq>
