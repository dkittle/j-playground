# Story M1.4: Reset Reinitializes Engine

Status: ready-for-dev

## Story

As a J Playground user,
I want Reset to reliably recreate interpreter state and clear active REPL context,
so that I can immediately return to a clean working session.

**Acceptance Criteria References:** M1-AC-011, M1-AC-012, M1-AC-013

## Acceptance Criteria

1. Reset destroys the currently active interpreter instance. (M1-AC-011)
2. Reset creates a fresh interpreter with cleared runtime state. (M1-AC-012)
3. Reset clears input/output state immediately at the feature/UI layer. (M1-AC-013)

## Tasks / Subtasks

- [ ] Implement reset lifecycle path that tears down active engine instance. (AC: M1-AC-011)
  - [ ] Ensure teardown is deterministic and idempotent.
  - [ ] Prevent stale handles from being reused.
- [ ] Reinitialize fresh engine/session state after reset. (AC: M1-AC-012)
  - [ ] Verify prior definitions/variables are not available post-reset.
  - [ ] Validate reset can be invoked repeatedly without leaks.
- [ ] Ensure immediate feature/UI buffer clearing on reset trigger. (AC: M1-AC-013)
  - [ ] Clear input buffer immediately.
  - [ ] Clear output history immediately for visible clean state.
- [ ] Add tests covering teardown, reinit, and immediate clear behavior. (AC: M1-AC-011, M1-AC-012, M1-AC-013)

## Test Cases

| Test Case ID | Title | Coverage | AC IDs |
| --- | --- | --- | --- |
| TC-M1-013 | Reset Destroys Active Interpreter | Core | M1-AC-011 |
| TC-M1-014 | Reset Reinitializes with Fresh State | Core | M1-AC-012 |
| TC-M1-015 | Reset Clears Input/Output Immediately | Core | M1-AC-013 |
| TC-M1-016 | Repeated Rapid Reset Requests | Edge | M1-AC-011, M1-AC-012 |
| TC-M1-017 | Reset Requested During In-Flight Eval | Partial success | M1-AC-012, M1-AC-013 |

- Detailed steps and expected results: `features/milestone-01-test-cases.md`

## Dev Notes

- Reset is a critical behavioral contract from the PRD and must be always available.
- Reinitialization should happen through the same lifecycle path used for first startup.
- For in-flight evaluation, reset behavior is deferred for engine finalization: clear UI immediately, then finalize reset after current eval completes (no interrupt in v1).
- Do not add persistence or undo semantics in Milestone 01.

### Project Structure Notes

- Session reset orchestration should be centralized in shared/session layer.
- JVM engine implementation handles platform teardown/reinit specifics.
- Keep UI clearing semantics in feature state layer; do not couple engine adapter to UI widgets.

### References

- PRD source: `features/J Playground PRD.md` Sections 3.1, 3.5, 5 (NFR-05), 11, 12.
- KMP interface isolation: <https://kotlinlang.org/docs/multiplatform/multiplatform-connect-to-apis>
- KMP source-set organization: <https://kotlinlang.org/docs/multiplatform/multiplatform-discover-project>
- KMP concurrency/runtime FAQ: <https://kotlinlang.org/docs/multiplatform/faq>

## Dev Agent Record

### Agent Model Used

GPT-5 Codex

### Debug Log References

- N/A (planning artifact)

### Completion Notes List

- Story drafted to enforce reset teardown/reinit and immediate context clearing.

### File List

- `features/milestone-01-story-4-reset-reinitializes-engine.md`
