
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

# 4. Non-Goals (Out of Scope for v1)

- Syntax highlighting
- File-based workspace
- Addon/package support
- AI assistance
- Execution interrupt
- Multiple interpreter tabs
- Session persistence
- IDE-level navigation features
- Terminal emulation

---

# 5. UI Requirements

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
# 6. Architecture

## 6.1 Module Structure
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

## 6.2 JEngine Interface

```kotlin
interface JEngine {
    fun eval(input: String): JResult
    fun reset()
    fun shutdown()
}
```

## 6.3 JResult Model

```
data class JResult(
    val output: String,
    val isError: Boolean
)
```

## 6.4 Threading Rules

- Eval runs in background dispatcher    
- All UI state updated via Compose state
- Engine access guarded by single-threaded executor
- No concurrent evaluations allowed

---

# 7. Output Truncation Policy

If output length > 2000 characters:

- Truncate at 2000    
- Append truncation notice
- Do not partially break UTF-8 characters
- Do not break mid-line if avoidable

Future versions may allow expandable output blocks.

---

# 8. Platform Constraints

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
# **9. Technical Risks**

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

# **10. Roadmap**

## **Milestone 1 — Engine Proof of Concept**

- Bind J engine on JVM
- Eval simple expressions
- Reset working
    
## **Milestone 2 — Compose Desktop REPL**

- UI shell
- Multi-line support
- Styled errors
- Truncation logic

## **Milestone 3 — Android Tablet**

- Native binding    
- UI adjustments for touch
    
## **Milestone 4 — iOS Tablet**

- Static linking
- Memory validation
    

---

# **11. Definition of Done for v1**

- REPL works identically on desktop and tablet
    
- Multi-line definitions fully functional
    
- Reset reliable
    
- Output truncation enforced
    
- Errors styled distinctly
    
- No interpreter leaks across resets
    
- J runtime fully bundled
    
- Open-source repo structured and documented
    

---

# **12. Open-Source Positioning**

  

Project principles:

- Minimal core
    
- Clean architecture
    
- Platform parity
    
- Strict scope discipline
    
- Contributors welcome in engine layer and UI layer separately
