# Story M1.3: Simple Expression Evaluation

Status: ready-for-dev

## Story

As a J Playground user,
I want simple J expressions to evaluate reliably with correct output/error behavior,
so that I can use the app as a basic interactive REPL in Milestone 01.

**Acceptance Criteria References:** M1-AC-007, M1-AC-008, M1-AC-009, M1-AC-010

## Acceptance Criteria

1. Simple J expressions evaluate and return correct textual output. (M1-AC-007)
2. Engine evaluations are serialized; concurrent evaluation requests are not executed simultaneously. (M1-AC-008)
3. Evaluation executes off the UI thread to keep interface responsive. (M1-AC-009)
4. Native J error text is preserved verbatim, and interpreter remains usable after errors. (M1-AC-010)
## Tasks / Subtasks

- [ ] Implement simple expression evaluation flow using shared engine contract. (AC: M1-AC-007)
  - [ ] Map raw engine output to shared result model.
  - [ ] Verify baseline expression examples produce expected output.
- [ ] Enforce serialized evaluation execution. (AC: M1-AC-008)
  - [ ] Add single-lane execution mechanism for eval operations.
  - [ ] Confirm deterministic ordering under rapid submissions.
- [ ] Ensure background execution model for eval. (AC: M1-AC-009)
  - [ ] Keep engine work off main/UI dispatcher.
  - [ ] Return results safely to UI state layer.
- [ ] Preserve J-native error messaging and runtime continuity. (AC: M1-AC-010)
  - [ ] Pass through original J error text unchanged.
  - [ ] Confirm next eval succeeds after error.
- [ ] Add tests for success/error/serialization scenarios.

## Test Cases

| Test Case ID | Title | Coverage | AC IDs |
| --- | --- | --- | --- |
| TC-M1-008 | Simple Expressions Return Correct Output | Core | M1-AC-007 |
| TC-M1-009 | Evaluations Are Serialized Under Concurrency | Edge | M1-AC-008 |
| TC-M1-010 | Evaluation Runs Off UI Thread | Core | M1-AC-009 |
| TC-M1-011 | J Error Text Preserved and Interpreter Recovers | Core | M1-AC-010 |
| TC-M1-012 | Mixed Valid/Invalid Sequence | Partial success | M1-AC-007, M1-AC-008, M1-AC-010 |

- Detailed steps and expected results: `features/milestone-01-test-cases.md`

## Dev Notes

- Milestone 01 focuses on simple expressions; multiline completeness belongs to Milestone 2.
- Concurrency model should align with serialized eval and no interrupt support in v1.
- Error behavior must preserve engine stability and exact error text.
- Milestone-level test coverage is governed by test artifacts and release-gate audit, not this story alone.

### Project Structure Notes

- Shared evaluation orchestration belongs in common code where possible.
- JVM adapter performs platform execution while conforming to common contract.
- Avoid introducing output truncation and styled error UI requirements in this story.

### References

- PRD source: `features/J Playground PRD.md` Sections 3.1, 3.4, 3.6, 5 (NFR-03, NFR-04), 11.
- KMP shared/platform boundaries: <https://kotlinlang.org/docs/multiplatform/multiplatform-connect-to-apis>
- KMP concurrency guidance (coroutines/async patterns): <https://kotlinlang.org/docs/multiplatform/faq>
- KMP source-set organization: <https://kotlinlang.org/docs/multiplatform/multiplatform-discover-project>

## Dev Agent Record

### Agent Model Used

GPT-5 Codex

### Debug Log References

- N/A (planning artifact)

### Completion Notes List

- Story drafted with explicit serialization and error-preservation scope for Milestone 01.

### File List

- `features/milestone-01-story-3-simple-expression-evaluation.md`
