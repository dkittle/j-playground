# Milestone 01 Test Cases

## Purpose

Define executable test cases for Milestone 01 using:

- `features/milestone-01-acceptance-criteria.md`
- `features/milestone-01-story-1-engine-contract-and-session-lifecycle.md`
- `features/milestone-01-story-2-jvm-engine-binding-bootstrap.md`
- `features/milestone-01-story-3-simple-expression-evaluation.md`
- `features/milestone-01-story-4-reset-reinitializes-engine.md`

## Scope

In scope:

- JVM engine bootstrap
- Shared engine/session contract behavior
- Simple expression evaluation semantics
- Reset teardown/reinitialization behavior
- Serialization and responsiveness constraints

Out of scope:

- Milestone 2: Compose Desktop REPL (UI shell, input history, keyboard shortcuts, multi-line detection, styled error output, output truncation, monospaced font)
- Milestone 3: Android tablet (JNI/NDK binding, touch input, on-screen keyboard)
- Milestone 4: iOS tablet (static linking, Kotlin/Native C-interop)
- Milestone 5: Polish & Learnability (syntax highlighting, dark mode, font config, prompt state indicators, session log, input history persistence)
- Milestone 6: Productivity (script file loading, J help/NuVoc, plot/bitmap visualization)

## AC Coverage Matrix

| AC ID | Test Case IDs |
| --- | --- |
| M1-AC-001 | TC-M1-001 |
| M1-AC-002 | TC-M1-002 |
| M1-AC-003 | TC-M1-003 |
| M1-AC-004 | TC-M1-005 |
| M1-AC-005 | TC-M1-006 |
| M1-AC-006 | TC-M1-007 |
| M1-AC-007 | TC-M1-008, TC-M1-012 |
| M1-AC-008 | TC-M1-009, TC-M1-012 |
| M1-AC-009 | TC-M1-010 |
| M1-AC-010 | TC-M1-011, TC-M1-012 |
| M1-AC-011 | TC-M1-013, TC-M1-016 |
| M1-AC-012 | TC-M1-014, TC-M1-016, TC-M1-017 |
| M1-AC-013 | TC-M1-015, TC-M1-017 |
| M1-AC-014 | TC-M1-004 |
| M1-AC-015 | TC-M1-018 |

## NFR Coverage Matrix

| NFR ID | Requirement | Test Case IDs |
| --- | --- | --- |
| NFR-03 | Errors don't crash app | TC-M1-006, TC-M1-011 |
| NFR-04 | Recovers from errors without restart | TC-M1-011, TC-M1-012 |
| NFR-05 | Reset reliability, no memory leaks | TC-M1-014, TC-M1-016 |

## Detailed Test Cases

### TC-M1-001 Shared Contract Is Enforced in Common Code

- Story: M1.1
- AC: M1-AC-001
- Type: Unit + compile boundary
- Coverage: Core
- Preconditions: Buildable project state.
- Steps:
1. Build shared/common and JVM source sets.
2. Instantiate JVM engine through the shared engine interface in a test.
3. Validate result objects map to shared result model fields.
- Expected Result:
1. Build succeeds with no contract mismatch.
2. JVM implementation is consumable via the common interface.

### TC-M1-002 Exactly One Interpreter Instance Per Session

- Story: M1.1
- AC: M1-AC-002
- Type: Integration
- Coverage: Core
- Preconditions: App session started.
- Steps:
1. Evaluate `2+2`.
2. Evaluate a second expression in same session.
3. Capture interpreter/session identifier exposed by test harness for both calls.
- Expected Result:
1. The same interpreter instance is reused for both evaluations.
2. No second interpreter is created.

### TC-M1-003 No State Persistence Across App Restart

- Story: M1.1
- AC: M1-AC-003
- Type: Integration + manual
- Coverage: Core
- Preconditions: App can be restarted.
- Steps:
1. In Session A, define a symbol (for example `a=:` value).
2. Exit app fully.
3. Relaunch app as Session B.
4. Evaluate symbol from Session A.
- Expected Result:
1. Session B has no access to Session A state.
2. A J error indicating missing symbol/definition is returned.

### TC-M1-004 Deterministic Shutdown Including Repeat Call (Edge)

- Story: M1.1
- AC: M1-AC-014
- Type: Integration
- Coverage: Edge
- Preconditions: Active initialized interpreter.
- Steps:
1. Trigger shutdown from lifecycle owner.
2. Trigger shutdown again.
3. Reinitialize a fresh session.
- Expected Result:
1. First shutdown closes engine resources.
2. Second shutdown is deterministic (controlled no-op or explicit handled result).
3. Fresh session starts successfully.

### TC-M1-005 JVM Runtime Bootstrap Succeeds

- Story: M1.2
- AC: M1-AC-004
- Type: Integration
- Coverage: Core
- Preconditions: Bundled JVM runtime available.
- Steps:
1. Launch desktop app.
2. Run simple expression `1+1`.
- Expected Result:
1. Runtime initializes.
2. Expression output is returned (expected numeric result).

### TC-M1-006 JVM Bootstrap Failure Is Controlled (Edge)

- Story: M1.2
- AC: M1-AC-005
- NFR: NFR-03 (errors don't crash app)
- Type: Integration (fault injection)
- Coverage: Edge
- Preconditions: Failure injection toggle for init/binding path.
- Steps:
1. Start app with simulated binding/init failure.
2. Attempt first eval.
3. Observe app process status.
- Expected Result:
1. Controlled error is surfaced via app error path.
2. App remains running and responsive.
3. No unhandled crash occurs.

### TC-M1-007 JVM Implementation Isolated Behind Shared Boundary

- Story: M1.2
- AC: M1-AC-006
- Type: Static verification + compile boundary
- Coverage: Core
- Preconditions: Source sets are configured for `commonMain` and `jvmMain`.
- Steps:
1. Verify JVM-specific imports/types are not referenced from common engine contract files.
2. Build project.
- Expected Result:
1. Platform-specific implementation remains in `jvmMain`.
2. Common code compiles without JVM-specific dependency leakage.

### TC-M1-008 Simple Expressions Return Correct Output

- Story: M1.3
- AC: M1-AC-007
- Type: Integration
- Coverage: Core
- Preconditions: JVM engine initialized.
- Steps:
1. Evaluate a small set of deterministic expressions.
2. Capture output text.
- Expected Result:
1. Output matches expected J results for each expression.
2. Response formatting remains valid REPL text output.

### TC-M1-009 Evaluations Are Serialized Under Concurrency (Edge)

- Story: M1.3
- AC: M1-AC-008
- Type: Concurrency integration
- Coverage: Edge
- Preconditions: Harness can submit concurrent eval requests.
- Steps:
1. Submit multiple eval requests concurrently.
2. Record start/finish ordering and active-eval count.
- Expected Result:
1. At most one active eval at a time.
2. Requests are handled in deterministic serialized order.

### TC-M1-010 Evaluation Runs Off UI Thread

- Story: M1.3
- AC: M1-AC-009
- Type: Integration + responsiveness
- Coverage: Core
- Preconditions: UI harness or instrumentation can observe thread responsiveness.
- Steps:
1. Start a non-trivial evaluation.
2. While eval runs, interact with UI state updates unrelated to engine work.
- Expected Result:
1. Engine execution is not on UI thread.
2. UI remains responsive.

### TC-M1-011 J Error Text Preserved and Interpreter Recovers

- Story: M1.3
- AC: M1-AC-010
- NFR: NFR-03 (errors don't crash app), NFR-04 (recovers from errors without restart)
- Type: Integration
- Coverage: Core
- Preconditions: Initialized session.
- Steps:
1. Submit intentionally invalid J input.
2. Capture returned error text.
3. Submit valid expression immediately after.
- Expected Result:
1. Error text preserves original J message content.
2. Subsequent valid expression succeeds in same session.

### TC-M1-012 Partial Success: Mixed Valid/Invalid Sequence

- Story: M1.3
- AC: M1-AC-007, M1-AC-008, M1-AC-010
- NFR: NFR-04 (recovers from errors without restart)
- Type: Integration
- Coverage: Partial success
- Preconditions: Initialized session.
- Steps:
1. Run valid expression A.
2. Run invalid expression B.
3. Run valid expression C.
- Expected Result:
1. A returns success.
2. B returns controlled J error.
3. C returns success without session reset.
4. Sequence demonstrates partial success behavior without interpreter corruption.

### TC-M1-013 Reset Destroys Active Interpreter

- Story: M1.4
- AC: M1-AC-011
- Type: Integration
- Coverage: Core
- Preconditions: Active session with initialized interpreter.
- Steps:
1. Capture active interpreter/session id.
2. Trigger reset.
3. Capture interpreter/session id after reset.
- Expected Result:
1. Post-reset id differs from pre-reset id.
2. Old interpreter is no longer active.

### TC-M1-014 Reset Reinitializes with Fresh State

- Story: M1.4
- AC: M1-AC-012
- NFR: NFR-05 (reset reliability)
- Type: Integration
- Coverage: Core
- Preconditions: Ability to define symbols before reset.
- Steps:
1. Define a symbol in current session.
2. Trigger reset.
3. Evaluate previously defined symbol.
- Expected Result:
1. Symbol is absent after reset.
2. Fresh interpreter state is confirmed.

### TC-M1-015 Reset Clears Input/Output Immediately

- Story: M1.4
- AC: M1-AC-013
- Type: UI behavior
- Coverage: Core
- Preconditions: Input and output views contain text.
- Steps:
1. Enter pending input text.
2. Produce at least one output entry.
3. Trigger reset.
- Expected Result:
1. Input field clears immediately.
2. Output area clears immediately.

### TC-M1-016 Edge: Repeated Rapid Reset Requests

- Story: M1.4
- AC: M1-AC-011, M1-AC-012
- NFR: NFR-05 (reset reliability, no memory leaks)
- Type: Integration
- Coverage: Edge
- Preconditions: Running session.
- Steps:
1. Trigger reset repeatedly in quick succession.
2. Run a valid expression after final reset.
- Expected Result:
1. No crash or deadlock occurs.
2. Session remains usable.
3. Final session is fresh and operational.

### TC-M1-017 Partial Success: Reset Requested During In-Flight Eval

- Story: M1.4
- AC: M1-AC-012, M1-AC-013
- Type: Integration + UI behavior
- Coverage: Partial success
- Preconditions: Long-running evaluation can be triggered.
- Steps:
1. Start a long-running eval.
2. Trigger reset while eval is in progress.
3. Observe UI buffer behavior and post-eval session state.
4. Submit a new simple expression.
- Expected Result:
1. UI input/output clear immediately on reset action.
2. Since interrupt is out of scope in v1, reset finalization is deferred until the in-flight eval completes.
3. After in-flight eval completion, reset finalizes and the post-reset session is fresh and supports new evaluation.
4. System remains responsive and stable throughout.

### TC-M1-018 Milestone-Level Coverage Audit for Release Sign-Off

- Story: Milestone-Level
- AC: M1-AC-015
- Type: Traceability + release audit
- Coverage: Core
- Preconditions: Test execution results available for Milestone 01 suite.
- Steps:
1. Collect executed results for all Milestone 01 test cases.
2. Verify evidence exists for each required behavior category:
   - Eval success
   - Eval error handling
   - Serialized evaluation behavior
   - Reset behavior
3. Confirm required categories are represented by passing test cases and linked in release report.
- Expected Result:
1. Coverage audit proves all M1-AC-015 categories are represented by executed and passing test cases.
2. Release report includes traceable evidence for each category and references the relevant test case IDs.

## Story-to-Test Mapping

| Story | Test Case IDs |
| --- | --- |
| M1.1 | TC-M1-001, TC-M1-002, TC-M1-003, TC-M1-004 |
| M1.2 | TC-M1-005, TC-M1-006, TC-M1-007 |
| M1.3 | TC-M1-008, TC-M1-009, TC-M1-010, TC-M1-011, TC-M1-012 |
| M1.4 | TC-M1-013, TC-M1-014, TC-M1-015, TC-M1-016, TC-M1-017 |
| Milestone-Level | TC-M1-018 |

## Notes

- Detailed execution sequence and release-gate policy are defined in `features/milestone-01-test-plan.md`.
