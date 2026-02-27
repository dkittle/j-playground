---
stepsCompleted: ["discovery", "vision", "users", "functional", "technical", "complete"]
inputDocuments: ["Juno-65.zip (source archive)", "Juno.md", "Jo.md", "jios901.md"]
workflowType: 'prd'
classification:
  domain: general
  projectType: mobile_app
  complexity: low
date: '2026-02-26'
lastEdited: '2026-02-26'
editHistory:
  - date: '2026-02-26'
    changes: 'Systematic improvement from validation: separated requirements from architecture, added Product Scope and Success Criteria sections, rewrote FRs as user capabilities, reformatted NFRs with measurement methods, added Info view FRs, added iOS Platform Requirements'
---

# Product Requirements Document - j901

**Author:** Don
**Date:** 2026-02-26
**Version:** 2.0 (Revised from validation findings — separated requirements from implementation)
**Source:** Reverse-engineered from archived Xcode project `Juno-65.zip` and development journals

---

## 1. Executive Summary

**j901** is a free iOS application that brings the J programming language — a high-level, general-purpose array language in the APL family — to iPad and iPhone. Created by Kenneth E. Iverson and Roger Hui, J is renowned for its concise notation for mathematical and statistical operations, and j901 makes this power portable.

The app provides a six-view interactive development environment: a REPL terminal for expression evaluation, a multi-tab script editor with syntax highlighting, an in-app J documentation browser, PDF-based plot visualization, bitmap/image slate display, and a three-input modeling tool called the Mill. The J engine (version 903k/904) is embedded as a compiled C library with a notification-driven bridge to Swift.

j901 is offered free of charge as a **learning resource for teachers and students** of mathematics, statistics, science, and engineering, and as a **convenience tool for J experts** who normally use desktop computers. Due to iOS platform constraints (no dynamic libraries, no package manager, sandboxed filesystem), the app functions primarily as an educational tool rather than a full professional platform.

**Current Status:** Released on the Apple App Store as version 1.1. The app is developed independently by Ian Clark, wholly independent of Jsoftware Inc.

---

## 2. Product Vision

### Problem Statement

J is a powerful array programming language used in mathematics, statistics, finance, and scientific computing. However, access to J has historically been limited to desktop platforms (Windows, Linux, macOS). Students, teachers, and professionals lack a way to practice J interactively on mobile devices — the platform they carry everywhere.

### Vision

Deliver a fully functional J programming environment on iOS that lets users write, execute, and visualize J code anywhere, with special focus on the iPad as a portable classroom tool.

---

## 3. Success Criteria

| ID | Criterion | Target | Measurement Method |
|----|-----------|--------|--------------------|
| SC-1 | App Store availability | Free, no in-app purchases | Verified via App Store listing: price = $0.00, no IAP entitlements |
| SC-2 | Platform coverage | iPad (primary), iPhone (secondary) | App launches and renders correctly on both device families |
| SC-3 | J engine compatibility | Version 903.1+ | Engine version confirmed via `9!:14''` returning ≥ 903.1 |
| SC-4 | Foreign conjunction support | All standard m!:n except 15!:n (DLL/Memory) | Automated test for each foreign conjunction category (0!:n through 128!:n) |
| SC-5 | Script ecosystem | Load, edit, save .ijs files from local and iCloud storage | User completes full file lifecycle: create → edit → save → close → reopen |
| SC-6 | Visualization | Built-in plot and viewmat without external packages | `plot` and `viewmat` produce visible output in Plot and Slate views |

---

## 4. Product Scope

### In-Scope (Current Release — v1.1)

- REPL terminal with input history and dot-command shortcuts
- Multi-tab script editor with J syntax highlighting and find
- In-app J documentation browser (NuVoc, Help, Guides, web links)
- PDF plot visualization with multi-plot navigation
- Bitmap/image slate display with multi-image navigation
- Interactive three-input modeling tool (Mill) with heartbeat
- iCloud Drive and local file access via iOS document picker
- Physical keyboard shortcuts (Cmd, Ctrl, Alt, Shift combinations)
- J engine v903k embedded with 66 iOS UI operations via `ui` verb
- Watched noun system for J↔app state synchronization
- iTunes/Finder file sharing
- Dark mode support

### Out-of-Scope

- Package manager (pacman) — blocked by iOS restriction on dynamic libraries
- Dynamic library loading (15!:n foreign conjunction) — blocked by iOS restriction
- Multi-user or collaborative editing features
- Cloud-based J execution or remote engine
- App Store monetization (in-app purchases, subscriptions)
- Android or other non-iOS platforms

### Future Considerations

- J engine upgrade path (v904+)
- Additional bundled addon packages
- Enhanced plot interactivity (pan, zoom, export)
- Cross-device script synchronization via iCloud
- Accessibility improvements (VoiceOver, Dynamic Type)

---

## 5. Target Users

### Primary: Mathematics & Science Educators and Students

Teachers and students who use J for teaching array-based computation, linear algebra, statistics, and number theory. The iPad form factor enables classroom use, interactive demonstrations, and homework practice. J's concise notation (e.g., `+/ 1 2 3 4` for summation, `*/~ i.5` for outer product) is ideal for teaching mathematical concepts without programming boilerplate.

### Secondary: J Experts Seeking Mobile Access

Professional J users who work on desktops but want quick access for prototyping, exploring ideas, or running calculations on the go. These users understand the iOS limitations and accept them for the convenience of portability.

---

## 6. User Stories

### Terminal / REPL (Home)

| ID | Story | Priority |
|----|-------|----------|
| US-T1 | As a user, I can type J expressions and see results immediately in the terminal so I can explore the language interactively | P0 |
| US-T2 | As a user, I can navigate my input history (first/prev/next/last) using the keyboard toolbar so I can re-execute or modify previous expressions | P0 |
| US-T3 | As a user, I can enter multi-line explicit definitions (verb/noun/adverb/conjunction definitions ending with `)`) so I can define reusable J words | P0 |
| US-T4 | As a user, I can use dot-commands (`.l`, `.o`, `.e`, `.d`, etc.) as shortcuts for common file and navigation operations | P1 |
| US-T5 | As a user, I can view a color-coded session log (transaction history) showing my inputs and J's responses | P1 |
| US-T6 | As a user, I can use the heartbeat system to run periodic J expressions for monitoring or animation | P2 |

### Script Editor (Edit)

| ID | Story | Priority |
|----|-------|----------|
| US-E1 | As a user, I can open, edit, and save J scripts (.ijs files) in a multi-tab editor with a segmented control for tab switching | P0 |
| US-E2 | As a user, I can load scripts from iCloud Drive or "On My iPad" storage via the standard iOS document picker | P0 |
| US-E3 | As a user, I can see J syntax highlighting (verbs, nouns, adverbs, conjunctions, strings, numbers, comments) applied to my code in real-time | P1 |
| US-E4 | As a user, I can toggle line numbers on/off and switch between plain and syntax-colored views via the Eye menu | P1 |
| US-E5 | As a user, I can find text within scripts using case-sensitive or case-insensitive search with next/prev navigation | P1 |
| US-E6 | As a user, I can adjust font size (small/medium/large) independently per view controller | P1 |
| US-E7 | As a user, I can create, close, duplicate, swap, and reorder tabs to manage multiple open scripts | P1 |
| US-E8 | As a user, I can run the current script or a selected portion from the editor | P0 |

### Visualization (Plot & Slate)

| ID | Story | Priority |
|----|-------|----------|
| US-V1 | As a user, I can generate 2D plots from J plotting verbs and view them as PDFs in the Plot view | P0 |
| US-V2 | As a user, I can navigate between multiple generated plots using swipe gestures or page controls | P1 |
| US-V3 | As a user, I can view bitmap images (viewmat output) in the Slate view for matrix visualization | P1 |
| US-V4 | As a user, I can delete individual plots or clear all plots from the stack | P2 |

### Mill (Interactive Modeling)

| ID | Story | Priority |
|----|-------|----------|
| US-M1 | As a user, I can enter J expressions in three input fields and see the result assigned to the `it_z_` noun | P1 |
| US-M2 | As a user, I can see a description of the resulting noun (type, shape, value) alongside the formatted output | P1 |
| US-M3 | As a user, I can load sample mill scripts to explore interactive modeling patterns | P2 |

### Information & Help

| ID | Story | Priority |
|----|-------|----------|
| US-I1 | As a user, I can access J documentation (NuVoc, Help, Guides) within the app's Info view | P0 |
| US-I2 | As a user, I can view HTML help pages, bundled reference materials, and external web links from a curated menu | P1 |

### System & Settings

| ID | Story | Priority |
|----|-------|----------|
| US-S1 | As a user, I can control app behavior via persistent boolean flags (clear input after return, defocus after return, all hotkeys, etc.) | P1 |
| US-S2 | As a user, I can read and write app settings from J code using the `ui` verb or watched nouns in the `_i_` locale | P2 |
| US-S3 | As a user, I can use physical keyboard shortcuts (Cmd, Ctrl, Alt combinations) for all major operations on iPad with an external keyboard | P1 |

---

## 7. Functional Requirements

### FR-1: Terminal (Home View)

| ID | Requirement | Traces To |
|----|-------------|-----------|
| FR-1.1 | Users can type J expressions and see results immediately in a scrollable monospace terminal | US-T1 |
| FR-1.2 | Users can navigate input history (first, previous, next, last) via keyboard toolbar buttons | US-T2 |
| FR-1.3 | Users can enter multi-line explicit definitions (verb, noun, adverb, conjunction definitions ending with `)`) | US-T3 |
| FR-1.4 | Users can use dot-commands (`.l`, `.o`, `.e`, `.d`, `.f`, `.g`, `.h`, `.z`) as shortcuts for common file and navigation operations | US-T4 |
| FR-1.5 | Users can view a color-coded session log showing inputs and J responses with timestamps | US-T5 |
| FR-1.6 | Users can run periodic J expressions via the heartbeat system for monitoring or animation | US-T6 |
| FR-1.7 | Users can distinguish J prompt states (idle, datum continuation, debug) via color-coded visual indicators | US-T1 |
| FR-1.8 | Users can provide input to interactive J expressions (e.g., `1!:1[1`) via the terminal input field | US-T1 |

### FR-2: Script Editor (Edit View)

| ID | Requirement | Traces To |
|----|-------------|-----------|
| FR-2.1 | Users can open, edit, and save J scripts (.ijs files) in a multi-tab editor with up to 8 simultaneously open scripts | US-E1 |
| FR-2.2 | Users can load scripts from iCloud Drive or local device storage via the standard iOS document picker | US-E2 |
| FR-2.3 | Users can see J syntax highlighting with tokens classified as verbs, nouns, adverbs, conjunctions, controls, strings, numbers, and comments across 4 color schemes | US-E3 |
| FR-2.4 | Users can toggle line numbers on/off and switch between plain and syntax-colored views via the Eye menu | US-E4 |
| FR-2.5 | Users can search within scripts using case-sensitive or case-insensitive find with next/prev navigation | US-E5 |
| FR-2.6 | Users can adjust font size (small, medium, large) independently per view | US-E6 |
| FR-2.7 | Users can create, close, duplicate, swap, and reorder tabs to manage multiple open scripts | US-E7 |
| FR-2.8 | Users can run the current script or a selected portion from the editor and see results in the terminal | US-E8 |

### FR-3: Visualization (Plot & Slate)

| ID | Requirement | Traces To |
|----|-------------|-----------|
| FR-3.1 | Users can generate 2D plots from J plotting verbs and view them as rendered PDFs in the Plot view | US-V1 |
| FR-3.2 | Users can navigate between multiple generated plots using swipe gestures or page controls | US-V2 |
| FR-3.3 | Users can view bitmap images (viewmat output) in the Slate view for matrix visualization | US-V3 |
| FR-3.4 | Users can delete individual plots/images or clear all from the navigation stack | US-V4 |

### FR-4: Mill (Interactive Modeling)

| ID | Requirement | Traces To |
|----|-------------|-----------|
| FR-4.1 | Users can enter J expressions in three input fields and see the evaluated result with type, shape, and formatted value | US-M1 |
| FR-4.2 | Users can view a description of the resulting noun (type, shape) alongside the formatted output | US-M2 |
| FR-4.3 | Users can load sample mill scripts to explore interactive modeling patterns | US-M3 |
| FR-4.4 | Users can enable heartbeat-driven periodic re-evaluation in Mill view for monitoring or animation | US-M1 |

### FR-5: Information & Help (Info View)

| ID | Requirement | Traces To |
|----|-------------|-----------|
| FR-5.1 | Users can access J documentation (NuVoc, Help, Guides) within the app's Info view | US-I1 |
| FR-5.2 | Users can view HTML help pages and bundled reference materials in an embedded browser | US-I2 |
| FR-5.3 | Users can navigate to external web resources from a curated links menu | US-I2 |

### FR-6: Navigation & Input

| ID | Requirement | Traces To |
|----|-------------|-----------|
| FR-6.1 | Users can navigate between 6 views (Home, Edit, Info, Plot, Slate, Mill) via tab bar | US-S3 |
| FR-6.2 | Users can use physical keyboard shortcuts (Cmd, Ctrl, Alt, Shift combinations) for all major operations | US-S3 |
| FR-6.3 | Users can use swipe gestures (left, right, up) and tap gestures across all views for navigation and actions | US-S3 |
| FR-6.4 | Users can access app files via iTunes/Finder file sharing | US-E2 |

### FR-7: J-to-iOS Bridge Operations

| ID | Requirement | Traces To |
|----|-------------|-----------|
| FR-7.1 | J code can trigger 66 distinct iOS UI operations via the `ui` verb, including alerts, navigation, clipboard, display, audio, cursor, and editor operations | US-S2 |
| FR-7.2 | J code can read and write 46 app settings via the `_i_` locale watched nouns or the `ui` verb's set/get operations | US-S2 |
| FR-7.3 | J code can access the iOS clipboard for copy and paste operations | US-S2 |

### FR-8: Settings & Configuration

| ID | Requirement | Traces To |
|----|-------------|-----------|
| FR-8.1 | Users can control app behavior via 20 persistent boolean flags (clear input after return, defocus after return, all hotkeys, no beeps, preprocess J, etc.) | US-S1 |
| FR-8.2 | Users can configure integer parameters (heartbeat interval, log limits) and string values (test sentence) that persist across sessions | US-S1 |

---

## 8. Non-Functional Requirements

### Performance

| ID | Requirement | Measurement Method |
|----|-------------|--------------------|
| NFR-P1 | App launches to interactive terminal in under 3 seconds | Measured from cold start tap to cursor active in terminal, on baseline iPad hardware |
| NFR-P2 | Basic J arithmetic expressions evaluate in under 100ms | Measured as time from return-press to result display for `+/ i.100` |
| NFR-P3 | Syntax highlighting applies within 1 second of last keystroke | Measured as delay between typing stop and color application on a 500-line script |
| NFR-P4 | J↔app state synchronization completes within 500ms of variable change | Measured as polling interval for watched noun detection |
| NFR-P5 | Tab transitions animate in under 350ms | Measured from tab tap to animation completion |

### Reliability

| ID | Requirement | Measurement Method |
|----|-------------|--------------------|
| NFR-R1 | 100% of J engine errors surface as text output; 0% cause app crashes | Verified by executing error-producing expressions (`1%0`, `'abc'+1`, stack overflow) and confirming app stability |
| NFR-R2 | Input and output buffers truncate gracefully at configured limits without crash | Verified by sending input exceeding buffer limits and confirming truncation with no crash |
| NFR-R3 | App recovers from all J engine errors without requiring restart | Verified by executing error-producing then valid expressions in sequence |
| NFR-R4 | Interactive input blocks until user responds with no timeout | Verified by triggering `1!:1[1`, waiting 60+ seconds, then providing input and confirming correct result |

### Compatibility

| ID | Requirement | Measurement Method |
|----|-------------|--------------------|
| NFR-C1 | App runs on iOS 14.5 and later | Verified by building for iOS 14.5 minimum deployment target and running on device/simulator |
| NFR-C2 | App renders correctly on both iPad and iPhone device families | Verified by running on iPad and iPhone with correct layout and no clipping |
| NFR-C3 | All documented keyboard shortcuts respond correctly with external keyboard | Verified by testing each shortcut in the keyboard shortcut map on iPad with physical keyboard |
| NFR-C4 | All views adapt to system light/dark mode setting | Verified by toggling system appearance and confirming all 6 views update colors correctly |
| NFR-C5 | Users can open .ijs files from iCloud Drive via document picker | Verified by loading a .ijs file from iCloud Drive and confirming content displays in editor |

### iOS Platform Requirements

| ID | Requirement | Details |
|----|-------------|---------|
| NFR-I1 | Required entitlements: iCloud container, File Provider | App requires iCloud entitlement for document picker access |
| NFR-I2 | App is fully functional without network connectivity | J engine and all bundled content run locally; no network dependency for core features |
| NFR-I3 | No dynamic code generation or JIT compilation | All code compiled into binary per App Store Review Guidelines section 2.5.2 |
| NFR-I4 | No user data collected or transmitted | No analytics, no telemetry, no network calls except user-initiated web links in Info view |

---

## 9. Technical Architecture

### 9.1 System Architecture Overview

```
┌──────────────────────────────────────────────────────────┐
│                      iOS App Layer                        │
│                                                          │
│  ┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐ │
│  │ Home │ │ Edit │ │ Info │ │ Plot │ │Slate │ │ Mill │ │
│  │(Term)│ │      │ │      │ │      │ │      │ │      │ │
│  └──┬───┘ └──┬───┘ └──┬───┘ └──┬───┘ └──┬───┘ └──┬───┘ │
│     │        │        │        │        │        │      │
│  ┌──┴────────┴────────┴────────┴────────┴────────┴──┐   │
│  │              TabBarController                     │   │
│  │         (Central Navigation Hub)                  │   │
│  │    + EngineDelegate + Foreign_2_9 extensions      │   │
│  └──────────────────────┬────────────────────────────┘   │
│                         │                                │
│  ┌──────────────────────┴────────────────────────────┐   │
│  │            Swift Service Layer                     │   │
│  │  ┌─────────────┐ ┌────────────┐ ┌──────────────┐  │   │
│  │  │ Scriptorium │ │Transactions│ │  WatchNoun   │  │   │
│  │  │ (Scripts)   │ │  (Log)     │ │ (_i_ locale) │  │   │
│  │  └─────────────┘ └────────────┘ └──────────────┘  │   │
│  │  ┌─────────────┐ ┌────────────┐ ┌──────────────┐  │   │
│  │  │  PathMan    │ │ColorPalette│ │  DotCommands │  │   │
│  │  │ (5 formats) │ │ (4 schemes)│ │  (.l .o .e)  │  │   │
│  │  └─────────────┘ └────────────┘ └──────────────┘  │   │
│  └──────────────────────┬────────────────────────────┘   │
│                         │                                │
│  ┌──────────────────────┴────────────────────────────┐   │
│  │       Objective-C Bridge (je-glue.m)              │   │
│  │  runs() → JInit() + JDo() + joutput() + jinput() │   │
│  │  NSNotification IPC: finish_2bangco9,             │   │
│  │    flag_2bangco9, action_joutput, action_jinput   │   │
│  │  Shared buffers: outputbuf, bangco_buffer,        │   │
│  │    jinput_buffer, inputbuf                        │   │
│  └──────────────────────┬────────────────────────────┘   │
│                         │                                │
│  ┌──────────────────────┴────────────────────────────┐   │
│  │           J Engine (C) — jsrc-903k                │   │
│  │  x.c: F2(jtforeign) → routes m!:n                │   │
│  │  x2ui_IAC.c: iOS-specific 2!:9 implementation    │   │
│  │  a.c, v*.c, m.c: Array operations, memory mgmt   │   │
│  │  jlib.h, jt.h: Core type system                  │   │
│  └───────────────────────────────────────────────────┘   │
└──────────────────────────────────────────────────────────┘
```

### 9.2 Threading Model

| Operation | Thread | Blocking | Typical Duration |
|-----------|--------|----------|-----------------|
| `runs()` (J execution) | DispatchQueue.global() | Yes | 100ms–10s |
| `joutput()` callback | J engine thread (within runs) | No | <1ms |
| `jinput()` callback | J engine thread | Yes (semaphore) | Until user responds |
| 2!:9 handler | Main thread (async dispatch) | No | <1ms |
| Flag access | Both threads (notification) | Notification wait | ~10ms |
| UI updates | Main thread (async) | No | <16ms (frame) |
| WatchNoun polling | Main thread (GCD timer) | No | 499ms interval |
| Syntax coloring | Main thread (delayed 999ms) | No | Variable |

### 9.3 Data Flow: J Execution

```
User Input (keyboard/toolbar)
  → EngineDelegate.workItem() [DispatchQueue.global()]
    → jinput_buffer_PUT(remaining lines)
    → outputbuf/outputline cleared
    → runs(firstLine) → JInit()+JDo()
      → J evaluates expression
      → joutput(jt, type, result)  [callback]
        → outputbuf accumulates
        → posts @"action_joutput" notification
      → jinput(jt, prompt)  [if input needed]
        → pops from jinput_buffer OR
        → posts @"action_jinput" + blocks on semaphore
    → OUTPUTLINE1 cached
    → semaphore signaled (next workItem can proceed)
  → Main thread: UI updates from notifications
```

### 9.4 Data Flow: 2!:9 Foreign Conjunction (J → iOS UI)

```
J code: 'beep' ui 1322
  → J engine: F2(jtforeign) for m=2, n=9
    → x2ui_IAC.c: F2(jtx2ui2)
      → formats x-arg and y-arg into NSString_2bangco9
      → posts @"finish_2bangco9" notification
  → Swift: TabBarController observes notification
    → action_2bangco9(): extracts x="beep", y="1322"
    → service_ui(x: "beep", y: "1322")
      → switch on XCASE.beep → beep(1322)
  → AudioServices plays system sound 1322
```

### 9.5 Key Data Models

#### Script (Scriptorium)

```
Script {
  moniker: String          // Display name
  ext: String              // File extension (.ijs, .ijt, .txt)
  type: ScriptType         // jscript, lab, javascript, text
  readpath: String         // Source file location
  writepath: String        // Save destination
  readURL: URL             // Resolved read URL
  writeURL: URL            // Resolved write URL
  contentsOld: String      // Original loaded content
  contentsNew: String      // Current edited content
  contentsAtt: [NSAttr..]  // Cached syntax-colored lines
  good: Bool?              // Validation state
  read_only: Bool?         // Write protection
  date_created: Date
  date_modified: Date
  date_opened: Date
}
```

#### Transaction (Session Log)

```
Transaction {
  prompt: String           // Prompt marker (length = PR state: 0/3/6)
  sentence: String         // User's J expression
  response: String         // J engine output
  dataType: String         // J noun type (num, char, boxed)
  shape: String            // J array shape (e.g., "3 20")
  created: Date
  updated: Date
  truncated: Bool          // Long output flag
}
```

#### Path Formats (PathMan)

```
PATH_KIND enum {
  long       // /private/.../Documents/j/user/act.ijs
  tilde      // ~user/act.ijs
  jslash     // j/user/act.ijs
  bundle     // (Bundle.main resource)
  cloud      // iCloud document URL
  tmp        // /tmp/... temporary file
  unknown    // Unclassified
}
```

#### Watched Noun Categories

```
Boolean Flags:  PRINT1, PRINT2, NOT_MAIDEN_RUN, CLEAR_INPUT_AFTER_RETURN,
                DEFOCUS_AFTER_RETURN, ALL_HOTKEYS, NO_BEEPS, PREPROCESS_J, etc.

Integer Params: HEARTBEAT_MSEC, LOG_MAX_INDEX, LOG_OLDEST_INDEX,
                WORKITEM_QSIZE_MAX, WATCH_DELAY_POST_ENTER

String Values:  TEST_SENTENCE, PLOT_PATH, SLATE_PATH, WEBLINK,
                LOG_CONTAINING, LOG_APPENDED

Transfer Bufs:  BOOL, DOUBLE, FLOAT, INT, STRING (self-clearing channels)
```

### 9.6 Sandbox Directory Structure

```
~/Documents/
  j/
    system/         # Core J system libraries (ios.ijs, iosutils.ijs)
    user/           # User scripts
    addons/         # J addon packages (built-in subset)
    assets/         # Bundled assets (images, samples)
    tools/          # Developer utilities
    temp/           # Temporary files (plot.pdf, vmat.bmp, dump*.txt)
    plot/           # Generated plot PDFs (plot0.pdf, plot1.pdf, ...)
    slate/          # Generated bitmap images
    config/         # Configuration files (startup.ijs)
    test/           # Test scripts and payloads
```

### 9.7 Foreign Conjunction Coverage

| Category | Standard J | iOS Implementation |
|----------|-----------|-------------------|
| 0!:n Scripts | Full | Full — `0!:10` used for boot sentence |
| 1!:n Files | Full | Adapted — sandboxed paths, iCloud via DocPicker |
| 2!:n Host | Partial | 2!:9 extended for iOS UI bridge; 2!:55 alerts only (no terminate) |
| 3!:n Conversions | Full | Full — standard J |
| 4!:n Names | Full | Full — used for IDE introspection |
| 5!:n Representation | Full | Full — standard J |
| 6!:n Time | Full | Full — standard J |
| 7!:n Space | Full | Full — standard J |
| 8!:n Format | Full | Full — standard J |
| 9!:n Globals | Full | Full — standard J |
| 13!:n Debug | Full | Full — debug prompt support in UI |
| 15!:n DLL/Memory | **Removed** | **Not available** — no dylibs on iOS |
| 128!:n Misc | Partial | Partial — some crypto functions available |

### 9.8 iOS Platform Constraints

| Constraint | Impact | Mitigation |
|-----------|--------|-----------|
| No dynamic libraries (dylibs) | Cannot load fftw, regex, R integration | All code compiled into monolithic binary |
| No package manager (pacman) | Users cannot install J addons | Built-in subset of essential addons |
| Sandboxed filesystem | No arbitrary file system access | PathMan translates 5+ path formats; DocPicker for external files |
| No shell/process spawning | Standard 2!:n host commands unavailable | 2!:9 extended for iOS-specific host operations |
| App Store review requirements | No code generation, no JIT | J interprets; does not generate native code |
| App Transport Security | No plain HTTP | ATS re-enabled; HTTPS required for web links |

### 9.9 Engine Integration Specifications

| Specification | Details |
|---------------|---------|
| Engine embedding | J engine (jsrc-903k) compiled as C library linked into monolithic app binary |
| Initialization | Lazy init on first `runs()` call; boot sentence loads `ios.ijs`, sets `VERSION_z_`, `GEOMETRY_j_`, `DATESTAMP_j_` |
| Execution dispatch | J sentences dispatched via `runs(const char*)` on `DispatchQueue.global()` background queue |
| Multi-line support | Remaining lines staged in `jinput_buffer` (9999 bytes); popped on each `jinput()` callback |
| Concurrency control | All J execution serialized through dispatch semaphore (J engine is single-threaded) |
| Interactive input | `jinput()` blocks on dispatch semaphore until UI provides user input via `jinput_buffer` |

### 9.10 Foreign Conjunction Bridge Specifications

| Specification | Details |
|---------------|---------|
| IPC mechanism | `NSNotification` posts from C bridge to Swift: `finish_2bangco9`, `flag_2bangco9`, `action_joutput`, `action_jinput` |
| UI dispatch | `service_ui(x:y:)` routes 66 operation verbs (alert, confirm, beep, edit, goto, plot, slate, cursor, font, settings, clipboard, etc.) |
| Flag access | `flag_2bangco9` notification enables J code to read/write app settings bidirectionally |
| Clipboard bridge | `getclip`/`setclip` operations via `UIPasteboard.generalPasteboard` |
| Shared buffers | `outputbuf` (output accumulation), `bangco_buffer` (99999 bytes, 2!:9 args), `jinput_buffer` (9999 bytes, multi-line input), `inputbuf` (user input staging) |

### 9.11 File System Management Specifications

| Specification | Details |
|---------------|---------|
| Sandbox setup | `populate_sandbox()` copies bundle resources to `j/system`, `j/user`, `j/addons`, `j/assets`, `j/tools` on first launch |
| Path translation | `PathMan.swift` converts between 5 formats: long, tilde, j-slash, bundle, cloud |
| External file access | `UIDocumentPickerViewController` with security-scoped resource access and `NSFileCoordinator`; copies external files to temp directory |
| File sharing | `UIFileSharingEnabled` flag exposes Documents directory to iTunes/Finder |
| Directory safety | No `chdir()` usage; all directory operations use `FileManager` methods for iOS sandbox compliance |

### 9.12 Watched Noun System Specifications

| Specification | Details |
|---------------|---------|
| Polling mechanism | GCD timer polls J variables in `_i_` locale at 499ms intervals (`WatchNoun.swift`) |
| Watch types | Standing-order (continuous sync) and single-shot (one-time trigger) via `WT` enum |
| Variable categories | 46 watched variables: boolean flags (PRINT1, NO_BEEPS, etc.), integer params (HEARTBEAT_MSEC, LOG_MAX_INDEX), string values (TEST_SENTENCE, PLOT_PATH), transfer buffers (BOOL, INT, STRING) |
| Self-clearing channels | Transfer buffer variables (BOOL, DOUBLE, FLOAT, INT, STRING) reset to sentinel after read |

### 9.13 Data Persistence Specifications

| Data | Storage | Mechanism |
|------|---------|-----------|
| Boolean flags | UserDefaults | BOOLFLAGS array |
| Tab configuration | UserDefaults | SC4TAB array |
| Test sentence | UserDefaults | String key |
| Script contents | In-memory (Scriptorium) + file write | Script.write() to j/ paths |
| Transaction log | In-memory only | Transactions struct (session-scoped) |
| J session state | J engine memory | Lost on app termination |

---

## 10. UI Architecture

### 10.1 View Controller Map

| Tab | Index | Controller | Primary UI | Role |
|-----|-------|-----------|-----------|------|
| Home | 0 | HomeViewController (TermVC) | UITextView (resultView) + toolbar | REPL terminal |
| Edit | 1 | EditViewController | UITextView + UISegmentedControl + UITextField | Multi-tab script editor |
| Info | 2 | InfoViewController | WKWebView | Documentation browser |
| Plot | 3 | PlotViewController | WKWebView + UIPageControl | PDF plot viewer |
| Slate | 4 | SlateViewController | UIImageView + UIPageControl | Bitmap image viewer |
| Mill | 5 | MillViewController | 3x UITextField + 3x UITextView | Interactive modeling tool |

### 10.2 Modal Overlays

| Modal | Presenter | Purpose |
|-------|----------|---------|
| Settings | Any VC | Toggle 20 persistent boolean flags |
| Document Picker | TabBarController | Select files from iCloud/local storage |
| WebViewController (1-3) | Any VC | JAZZ debugger, HTML display, media viewers |
| Alert/Confirm | Foreign_2_9 | J-triggered user dialogs |

### 10.3 Keyboard Shortcut Map (Physical Keyboard)

| Shortcut | Action | Scope |
|----------|--------|-------|
| ⌃0–5 | Navigate to Home/Edit/Info/Plot/Slate/Mill | Global |
| ⌘B | J-break (interrupt engine) | Global |
| ⌘P | Show Palette | Modal contexts |
| ⌘D | Dismiss modal | Modal contexts |
| ⌘? | Help | Home |
| ⌘I | Toggle banner | Edit |
| ⌘9 | Scroll to bottom | Edit |
| ⌘F | Find | Edit |

### 10.4 Gesture Map

| Gesture | Home | Edit | Plot | Slate | Mill |
|---------|------|------|------|-------|------|
| Swipe Left | Yes | — | Next plot | Next slate | — |
| Swipe Right | — | — | Prev plot | Prev slate | — |
| Swipe Up | Yes | — | — | — | — |
| Tap (1 finger) | Yes | — | — | Beep | — |
| Tap (2 finger) | Yes | — | Clear all | Clear all | — |
| Tap on mask | Interaction | — | — | — | — |

---

## 11. J Language Reference (iOS Context)

### What Is J?

J is a high-level, general-purpose array programming language created by **Kenneth E. Iverson** (inventor of APL) and **Roger Hui** in 1990. Unlike APL, J uses **ASCII-only syntax**, making it accessible on any keyboard. Key characteristics:

- **Array programming**: Every operation implicitly works on arrays of any rank without loops
- **Tacit (point-free) programming**: Function trains (forks and hooks) compose operations without naming arguments
- **Concise notation**: Complex operations expressed in single lines (e.g., `+/ % #` is "mean")
- **Numeric precision**: 64-bit integers, complex floats, extended precision, rationals
- **Foreign conjunctions** (`m!:n`): Bridge between J engine and host environment
- **Locales**: Namespace system for code organization

### The `ui` Verb (iOS Extension)

The primary iOS extension to J, invoked as `'x-arg' ui 'y-arg'`:

| Category | Examples |
|----------|---------|
| Alerts | `'alert' ui 'message'`, `'confirm' ui 'question'`, `'hint' ui 'brief'` |
| Clipboard | `'getclip' ui ''`, `'setclip' ui 'text'` |
| Navigation | `'goto' ui 'Edit'`, `'goto' ui 'Plot'` |
| Files | `'edit' ui 'path.ijs'`, `'dump' ui ''` |
| Display | `'show' ui 'file'`, `'showhtml' ui 'file.html'` |
| Settings | `'set' ui 'PRINT1 1'`, `'get' ui 'PRINT1'` |
| Cursor | `'cursor' ui 'loc len'`, `'cursorline' ui ''` |
| Audio | `'beep' ui 1322` |
| Heartbeat | `'heart' ui 'start'`, `'heart' ui 'stop'` |
| Editor | `'segment' ui 'insert 2'`, `'fontsize' ui '+2'` |
| Scripts | `'scripts' ui 'contents'`, `'script' ui '3'` |

### Watched Nouns (Preferred API)

The watched-noun system in the `_i_` locale provides a more declarative alternative to `2!:9`:

```j
PRINT1_i_ =: 1          NB. Enable verbose logging
HEARTBEAT_MSEC_i_ =: 500 NB. Set heartbeat interval
PLOT_PATH_i_ =: '...'   NB. Trigger plot display
```

The app polls these variables every 499ms and reacts to changes. This is the **recommended** approach for new J code on iOS.

---

## 12. Known Limitations & Constraints

| Area | Limitation | Workaround |
|------|-----------|-----------|
| Dynamic Libraries | 15!:n (DLL) foreign not available | All essential code compiled into binary |
| Package Manager | No pacman; cannot install addons at runtime | Essential addons bundled in app |
| File Access | Sandboxed; no arbitrary filesystem access | DocPicker for iCloud; PathMan for translation |
| Shell Access | No shell or process spawning | 2!:9 and ui verb for host operations |
| Plotting Libraries | fftw, regex unavailable | Built-in plot/viewmat with PDF output |
| Session Persistence | Transaction log and J state lost on termination | Script save is persistent; session is ephemeral |
| Syntax Coloring | Can be slow on large files (2000+ lines) | 999ms debounce; disable via Eye menu |
| Line Numbers | Alignment issues with wrapped lines | Toggle off for editing; view-only mode |

---

## 13. Appendix: Source File Map

### Core Engine Bridge
- `je-glue.h/m` — C bridge: runs(), joutput(), jinput(), buffer management
- `j901-Bridging-Header.h` — Swift/ObjC bridge declarations
- `EngineDelegate.swift` — workItem(), run_sync/async(), text dispatch
- `Foreign_2_9.swift` — 2!:9 dispatcher: 66 UI operations (1366 lines)
- `F29Texts.swift` — Help text and flag documentation
- `DotCommands.swift` — Abbreviated command system
- `jsrc-903k/` — J engine C source (x.c, x2ui_IAC.c, a.c, v*.c, etc.)

### View Controllers
- `HomeViewController.swift` — Terminal REPL
- `EditViewController.swift` + `Edit*.swift` (8 extensions) — Script editor
- `PlotViewController.swift` — PDF plot viewer
- `SlateViewController.swift` — Bitmap image viewer
- `MillViewController.swift` — Interactive modeling tool
- `InfoViewController.swift` — Documentation browser
- `TabBarController.swift` — Navigation hub + engine observer

### Data & State
- `Scriptorium.swift` — Script storage (Scripts/Script)
- `Transactions.swift` — Session log (Transactions/Transaction)
- `WatchNoun.swift` — J variable polling system
- `VAR.swift` — Enum-based flag access
- `Enums.swift` — Type-safe enumerations (40+ enums)
- `UserDefaults.swift` — Persistent preferences

### Infrastructure
- `PathMan.swift` — 5-format path translation
- `DocPicker.swift` — iCloud document picker
- `Cloud.swift` — iCloud bookmark management
- `ColorPalette.swift` — Syntax highlighting (4 color schemes)
- `Hotkey.swift` + `Hotkey*.swift` — Keyboard shortcut system
- `Keyboard*.swift` — Custom keyboard toolbars
- `Gestures.swift` — Touch gesture handling
- `Heartbeat.swift` — Periodic execution timer
- `GlobalFuncs.swift` — 150+ utility functions
- `StringExtension.swift` — 50+ string methods
- `Beeps.swift` — Semantic audio feedback

---

## 14. Glossary

| Term | Definition |
|------|-----------|
| **J** | Array programming language in the APL family, created by Iverson and Hui (1990) |
| **Foreign conjunction** | `m!:n` — J's mechanism for calling host environment functions |
| **2!:9** | iOS-specific foreign conjunction bridging J engine to iOS UI |
| **Tacit programming** | Point-free function composition using trains (forks/hooks) |
| **NuVoc** | The J vocabulary reference documentation portal |
| **Scriptorium** | In-memory cache of open scripts in the editor |
| **Transaction** | A logged input/output pair from a J evaluation |
| **WatchNoun** | J variable monitored by the iOS app for state synchronization |
| **Dot-command** | Abbreviated command (`.l`, `.o`, `.e`) preprocessed before J evaluation |
| **Locale** | J namespace; `_i_` is the iOS integration locale, `_z_` is the global locale |
| **Mill** | Three-input interactive modeling view for exploring J nouns |
| **Palette** | Toolbar/input bar with contextual labels and swipe gestures |
| **Codename** | Development phase name: Joyce→Jo→Justine→Jessica→Jetta→Jenny→June→Julie→Juno |
