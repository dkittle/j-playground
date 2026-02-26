# Milestone 01 Test Plan

## Objective

Define how to execute Milestone 01 test cases and determine release readiness for the Engine Proof of Concept.

Primary artifacts:

- `features/milestone-01-acceptance-criteria.md`
- `features/milestone-01-test-cases.md`
- `features/milestone-01-story-1-engine-contract-and-session-lifecycle.md`
- `features/milestone-01-story-2-jvm-engine-binding-bootstrap.md`
- `features/milestone-01-story-3-simple-expression-evaluation.md`
- `features/milestone-01-story-4-reset-reinitializes-engine.md`

## In Scope

- Validation of AC IDs `M1-AC-001` through `M1-AC-015`
- JVM desktop runtime behavior for Milestone 01
- Core, edge, and partial success scenarios listed in `milestone-01-test-cases.md`

## Out of Scope

- Milestone 2+ scope (multiline completion behavior, truncation rendering policy, Android/iOS tablet release validation)
- Cross-platform packaging validation beyond JVM desktop Milestone 01 target

## Test Environments

| Environment | Purpose |
| --- | --- |
| Local JVM desktop run (`./gradlew :composeApp:run`) | Manual behavior validation and UI responsiveness checks |
| Automated verification (`./gradlew :composeApp:check`) | Unit/integration regression baseline |
| CI JVM pipeline (if configured) | Reproducible release-candidate validation |

## Entry Criteria

1. Milestone 01 stories are marked ready for implementation or done.
2. JVM engine binding code paths compile.
3. Test harness supports fault injection for bootstrap failure and concurrent eval submissions.
4. No blocking unresolved defects in test infrastructure.

## Execution Order

### Phase 1: Contract and Lifecycle Baseline

Execute: `TC-M1-001`, `TC-M1-002`, `TC-M1-003`, `TC-M1-004`

Pass condition:

- Shared contract boundary is valid.
- Session lifecycle and restart expectations hold.

### Phase 2: JVM Binding Validation

Execute: `TC-M1-005`, `TC-M1-006`, `TC-M1-007`

Pass condition:

- Bootstrap success path works.
- Failure path is controlled and non-crashing.
- Platform boundaries remain clean.

### Phase 3: Evaluation Behavior Validation

Execute: `TC-M1-008`, `TC-M1-009`, `TC-M1-010`, `TC-M1-011`, `TC-M1-012`

Pass condition:

- Expression evaluation, serialization, and error-preservation behavior meet AC requirements.
- Partial success sequence behaves as expected and interpreter remains stable.

### Phase 4: Reset and Recovery Validation

Execute: `TC-M1-013`, `TC-M1-014`, `TC-M1-015`, `TC-M1-016`, `TC-M1-017`

Pass condition:

- Reset teardown/reinit works reliably.
- Immediate UI clearing is observed.
- Edge and partial success reset behavior remain stable.

### Phase 5: Coverage Audit

Execute: `TC-M1-018`

Pass condition:

- Milestone-level coverage audit confirms required categories for `M1-AC-015`.
- Release report includes traceable evidence links for audit categories.

## Test Execution Rules

- Run test cases in phase order.
- If a core test fails, stop release sign-off and triage immediately.
- If an edge or partial success test fails, triage severity before deciding on release hold.

## Failure Evidence Capture

Capture all of the following for each failed test case:

- Test case ID
- Build/test run identifier
- Logs/screenshots
- Repro steps
- Suspected AC impact

## Partial Success Policy

A partial success test passes when the system degrades in an allowed way while preserving safety and core milestone behavior.

Examples:

- Mixed valid/invalid eval sequence returns both success and controlled error while session remains usable.
- Reset during in-flight eval clears UI immediately, then reset finalization occurs after in-flight eval completes because interrupt is out of scope in v1.

A partial success test fails when degradation causes crash, deadlock, corrupted session state, or AC violation.

## Release Gate Criteria

- All core test cases pass:
  - `TC-M1-001`, `TC-M1-002`, `TC-M1-003`, `TC-M1-005`, `TC-M1-007`, `TC-M1-008`, `TC-M1-010`, `TC-M1-011`, `TC-M1-013`, `TC-M1-014`, `TC-M1-015`
- All edge/partial tests are either pass or have documented waiver with explicit risk acceptance:
  - `TC-M1-004`, `TC-M1-006`, `TC-M1-009`, `TC-M1-012`, `TC-M1-016`, `TC-M1-017`
- Coverage audit test `TC-M1-018` passes and evidence is included in release report.
- `./gradlew :composeApp:check` is green for release candidate commit.
- No unresolved crash-class defects remain open for Milestone 01 scope.

## Waiver Rules

- Waivers are only allowed for edge/partial tests, never for core tests or `TC-M1-018`.
- Each waiver must include explicit risk statement, owner approval, and mitigation plan.
- Any waiver that implies crash risk, deadlock risk, or data/state corruption risk blocks release.

## Reporting Format

For each run, publish:

- Date/time and commit SHA
- Environment details
- Test case pass/fail summary by phase
- AC coverage summary
- Open defects and severity
- Release recommendation: `GO` or `NO-GO`

## Traceability

- AC source: `features/milestone-01-acceptance-criteria.md`
- Test case source: `features/milestone-01-test-cases.md`
- Story source files: `features/milestone-01-story-1-engine-contract-and-session-lifecycle.md` through `features/milestone-01-story-4-reset-reinitializes-engine.md`
