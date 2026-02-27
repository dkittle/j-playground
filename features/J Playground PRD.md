
# J REPL

## Product Requirements Document (PRD)

---
# 1. Vision

Provide a modern, portable, developer-polished J REPL that runs locally across desktop and tablets using an embedded J engine, offering a clean and consistent execution experience without replacing existing J IDE tooling.

This project complements tooling maintained by Jsoftware by offering:

- Cross-platform portability
- Lightweight experience
- Clean UX
- Embedded runtime
- Zero setup required

---

# 2. Target Personas

## Persona A — Curious Learner

- Wants to experiment with J interactively
- Needs immediate feedback
- Prefers clean UI over full IDE complexity

## Persona B — Practicing J Developer

- Wants to test expressions quickly
- Wants multi-line support
- Wants reliable interpreter state
- Does not need full project tooling

---

# 3. MVP Scope (v1)

## 3.1 Core Feature: Stateful REPL

- Single interpreter instance per app session
- Interpreter initialized at app launch
- Maintains state across commands
- One-click reset button
- No persistence across restarts

---

## 3.2 Multi-line Input Support

REPL must support J explicit definitions.

Example:
foo =: 3 : 0
y + 1
)

Requirements:

- Detect incomplete definitions
- Continue buffering input until definition terminator
- Execute entire block atomically
- Display output only after completion

---

## 3.3 Output Handling

- Render output as monospaced text
- Truncate output beyond 2,000 characters
- Append indicator:
  `[Output truncated at 2000 characters]`
- Allow horizontal scrolling
- Preserve whitespace formatting

---

## 3.4 Error Handling

Errors must:

- Be visually distinct from normal output
- Preserve original J error text
- Use styled error container
- Be scrollable
- Not crash interpreter

---

## 3.5 Reset Behavior

Reset button must:

- Destroy interpreter instance
- Reinitialize fresh interpreter
- Clear REPL output
- Clear input buffer
- Be available at all times

---

## 3.6 Execution Model

- All eval operations serialized
- Execution off UI thread
- UI remains responsive
- No interrupt capability in v1
- If evaluation hangs, app remains responsive but evaluation cannot be canceled

---

## 3.7 Bundled J Runtime

- J binaries bundled per platform
- Loaded via FFI
- No external installation required
- Platform-specific engine modules:
  - JVM → JNI
  - Desktop Native → Kotlin/Native C interop
  - Android → JNI via NDK
  - iOS → static library interop

---

## 3.8 Input History

Users must be able to recall and re-execute previous inputs without retyping.

Requirements:

- Up arrow recalls previous input, down arrow recalls next input
- History navigates through all inputs submitted in the current session
- Selecting a history entry replaces the current input field contents
- History is not persisted across app restarts in v1
- Minimum 100 entries retained per session

---

## 3.9 Keyboard Shortcuts

Physical keyboard users must have efficient access to core operations.

Requirements:

- Enter executes input (when block is complete)
- Shift+Enter inserts newline
- Up/Down arrow navigates input history (when cursor is at first/last line)
- Platform-standard copy/paste (Cmd+C/V on macOS, Ctrl+C/V elsewhere)
- Keyboard shortcut for reset (platform-appropriate modifier + R)
- Font size adjustment (platform-appropriate modifier + Plus/Minus)

---

# 4. Non-Goals (Out of Scope for v1)

## Planned for Future Milestones

These features are validated by existing J tools (j901, QTIDE) but excluded from v1 to maintain scope discipline:

- Syntax highlighting (both j901 and QTIDE have this — high user expectation)
- Dark mode / theming
- Font size control beyond keyboard shortcuts
- Script file loading and execution
- Session log / transaction history
- Input history persistence across restarts
- In-app J help / NuVoc reference
- Prompt state indicators (idle vs. continuation vs. debug)
- Visualization (plots, bitmap/matrix display)
- Execution interrupt / cancel

## Permanently Out of Scope

These are IDE-level features that conflict with j-playground's lightweight REPL positioning:

- Full project management (QTIDE territory)
- WD widget framework / GUI builder (QTIDE-specific)
- Addon/package manager
- AI assistance
- Multiple interpreter tabs
- Terminal emulation
- Collaborative editing
- Cloud-based J execution

---

# 5. Non-Functional Requirements

| ID | Category | Requirement | Measurement Method | Source |
|----|----------|-------------|--------------------|--------|
| NFR-01 | Performance | App launches to interactive REPL in under 3 seconds | Cold start to cursor active in input field, on target hardware | j901 NFR-P1, QTIDE NFR-01 |
| NFR-02 | Performance | Basic J expressions evaluate in under 100ms | Time from submit to result display for `+/ i.100` | j901 NFR-P2 |
| NFR-03 | Reliability | 100% of J engine errors surface as styled output; 0% cause app crashes | Execute error-producing expressions (`1%0`, `'abc'+1`, stack overflow) and confirm app stability | j901 NFR-R1, QTIDE NFR-11 |
| NFR-04 | Reliability | App recovers from all J engine errors without requiring restart | Execute error-producing then valid expressions in sequence; confirm correct results | j901 NFR-R3 |
| NFR-05 | Reliability | Reset reliably destroys and reinitializes engine with no memory leaks | Repeat reset cycle 50 times; monitor process memory for growth | Original PRD section 3.5, 9 |
| NFR-06 | Availability | App is fully functional without network connectivity | All features operate with airplane mode enabled | j901 NFR-I2 |
| NFR-07 | Compatibility | App renders and functions correctly on all target platforms (JVM desktop, Android tablet, iOS tablet) | Platform-specific test pass on each target | j901 NFR-C2, QTIDE NFR-06 |
| NFR-08 | Usability | All core REPL operations accessible via keyboard shortcuts on devices with physical keyboards | Keyboard shortcut audit against core operations list | j901 US-S3, QTIDE NFR-13 |

---

# 6. UI Requirements

Built with Compose Multiplatform.

## Layout
\------------------------------
\| REPL Output Scroll Area     \|
\|                                                \|
\|                                                \|
\|                                                \|
\-----------------------------
\| Multi-line Input Field           \|
\-----------------------------
\| [ Reset ]  [ Run ] (optional) \|
\-----------------------------

## Behavior

- Enter executes if block complete
- Shift+Enter inserts newline
- Output auto-scrolls to bottom
- Reset clears UI instantly
- Errors styled distinctly
- Monospaced font throughout REPL area

---
# 7. Architecture

## 7.1 Module Structure
:core
   JEngine interface
   JResult model
   SessionState model

:engine-jvm
:engine-native
:engine-android
:engine-ios

:ui-compose
   REPLScreen
   REPLViewModel
   OutputRenderer

---

## 7.2 JEngine Interface

```kotlin
interface JEngine {
    fun eval(input: String): JResult
    fun reset()
    fun shutdown()
}
```

## 7.3 JResult Model

```
data class JResult(
    val output: String,
    val isError: Boolean
)
```

## 7.4 Threading Rules

- Eval runs in background dispatcher    
- All UI state updated via Compose state
- Engine access guarded by single-threaded executor
- No concurrent evaluations allowed

---

# 8. Output Truncation Policy

If output length > 2000 characters:

- Truncate at 2000    
- Append truncation notice
- Do not partially break UTF-8 characters
- Do not break mid-line if avoidable

Future versions may allow expandable output blocks.

---

# 9. Platform Constraints

## iOS

- Must statically link J runtime
- Must ensure no forbidden dynamic loading
## Android

- Include J native libs in AAR
- Handle ABI splits
## Desktop

- Provide per-platform binaries
- Document supported OS versions

---
# **10. Technical Risks**

## **High Risk**

- Cross-platform native binding correctness
- Memory lifecycle management
- Reset reliability without leaks

## **Medium Risk**

- Multi-line detection correctness
- Output truncation safety
    
## **Low Risk**

- UI layer complexity
    

---

# **11. Roadmap**

## **Milestone 1 — Engine Proof of Concept**

- Bind J engine on JVM
- Eval simple expressions
- Reset working

## **Milestone 2 — Compose Desktop REPL**

- UI shell with REPL output scroll area and multi-line input
- Input history (up/down arrow navigation)
- Keyboard shortcuts (execute, reset, history, copy/paste, font size)
- Multi-line explicit definition detection and buffered execution
- Styled error output (visually distinct from normal output)
- Output truncation at 2,000 characters with indicator
- Monospaced font throughout REPL area

## **Milestone 3 — Android Tablet**

- Native binding via JNI/NDK
- UI adjustments for touch input
- On-screen keyboard considerations for J special characters

## **Milestone 4 — iOS Tablet**

- Static linking of J engine
- Memory lifecycle validation
- Kotlin/Native C-interop verification

## **Milestone 5 — Polish & Learnability**

Validated by j901 and QTIDE user patterns:

- Syntax highlighting for J tokens (verbs, nouns, adverbs, conjunctions, strings, numbers, comments)
- Dark mode / theming support
- Font family and size configuration
- Prompt state indicators (idle, continuation, debug)
- Session log / transaction history view
- Input history persistence across restarts

## **Milestone 6 — Productivity**

- Script file loading and execution (.ijs files)
- In-app J help / NuVoc vocabulary reference
- Visualization: plot output rendering
- Visualization: bitmap/matrix display (viewmat)
    

---

# **12. Definition of Done for v1**

- REPL works identically on desktop and tablet
- Multi-line definitions fully functional
- Input history navigable via up/down arrow
- Keyboard shortcuts functional for all core operations
- Reset reliable with no interpreter leaks
- Output truncation enforced at 2,000 characters
- Errors styled distinctly from normal output
- Engine errors never crash app (NFR-03)
- App launches to interactive REPL in under 3 seconds (NFR-01)
- Fully functional offline (NFR-06)
- J runtime fully bundled per platform
- Open-source repo structured and documented
    

---

# **13. Open-Source Positioning**

  

Project principles:

- Minimal core
    
- Clean architecture
    
- Platform parity
    
- Strict scope discipline

- Contributors welcome in engine layer and UI layer separately

---

# **14. References**

- `features/other/prd-j901.md` — j901 iOS IDE PRD (reverse-engineered). Source for validated mobile/tablet user needs.
- `features/other/prd-qtide.md` — QTIDE desktop IDE PRD (reverse-engineered). Source for validated desktop user needs.
- `features/j-engine-interface-design.md` — Technical design: how j901 and QTIDE interface with the J engine, mapped to j-playground's KMP architecture.
