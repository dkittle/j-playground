---
stepsCompleted: [01, 02, 02b, 02c, 03, 04, 05, 06, 07, 08, 09, 10, 11, 12]
inputDocuments: [codebase-analysis, jsoftware-website, github-readme]
workflowType: 'prd'
classification:
  domain: developer_tools
  projectType: desktop_app
  complexity: medium
lastEdited: '2026-02-26'
editHistory:
  - date: '2026-02-26'
    changes: 'Validation-guided fixes: trace references, implementation leakage, NFR specificity, Out of Scope, UJ-07'
---

# Product Requirements Document - QTIDE (JQt IDE)

**Author:** Codebase Analysis (Auto-Generated)
**Date:** 2026-02-26
**Version:** 1.0
**Source Repository:** https://github.com/jsoftware/qtide

---

## Executive Summary

### Vision

QTIDE is the official cross-platform integrated development environment and widget framework for the [J programming language](https://www.jsoftware.com/). It provides J developers with a professional-grade IDE featuring a multi-tabbed script editor, interactive terminal (REPL), project management, and a comprehensive widget toolkit (WD system) enabling J programs to build native desktop GUIs across 9+ platforms.

### Differentiator

QTIDE uniquely combines two roles in a single application:
1. **IDE** — A full-featured editor and terminal for writing, testing, and debugging J code
2. **Widget Framework** — A native GUI toolkit accessible from J via the `wd` verb, enabling J programs to create forms, dialogs, charts, grids, and OpenGL graphics without leaving the J ecosystem

No competing J environment provides both capabilities in a single, cross-platform binary.

### Target Users

- **J Language Developers** — Professional and hobbyist programmers using J for array-oriented computation, data analysis, mathematics, and algorithmic exploration
- **J Educators and Students** — Users learning J through interactive labs, demos, and the built-in help system
- **J Application Builders** — Developers creating desktop GUI applications powered by the J engine via the WD widget framework
- **Data Analysts** — Users leveraging J's array processing for interactive data exploration with grid/cube visualization

### Platform Support

Windows (32/64-bit, ARM64), macOS (x86_64, ARM64), Linux (x86_64, aarch64, armv6l), FreeBSD, OpenBSD, Android (ARM64, ARMv7), iOS, WebAssembly, Raspberry Pi.

---

## Success Criteria

| ID | Criterion | Measurement |
|----|-----------|-------------|
| SC-01 | Cross-platform parity | IDE builds and runs on all 9 target platforms with identical core functionality |
| SC-02 | J engine integration | All J language operations execute correctly through the JDo/Wd native bridge |
| SC-03 | Editor productivity | Script editing supports syntax highlighting, find/replace, line numbers, tab management, and project-scoped operations |
| SC-04 | Widget framework coverage | 50+ widget types available through the `wd` verb interface |
| SC-05 | Build configurability | Three build variants (Full, Fat, Slim) compile successfully with Qt 5.0–6.10 |
| SC-06 | Startup reliability | IDE loads configuration, restores previous session (open files, window positions), and presents a usable terminal within 3 seconds on modern hardware |
| SC-07 | Backward compatibility | Existing J scripts using `wd` commands continue to function without modification across version upgrades |

---

## Product Scope

### MVP (Current — v2.6.2)

The application is mature and in active maintenance. Current scope includes:

- Multi-tabbed script editor with J syntax highlighting
- Interactive J terminal (REPL) with command history
- Project management (create, open, build, snapshots)
- WD widget framework with 50+ widget types
- Grid/cube data visualization
- Git integration (status, terminal, file manager)
- Find in files, find in window
- Configurable themes, fonts, key bindings
- Cross-platform build system (Qt 5/6)
- Excel file read/write (QXlsx, Qt6+)
- Print and print-to-file support
- Context-sensitive vocabulary help (F1)
- WebSocket support for remote control
- OpenGL rendering integration
- Addon/package management

### Growth Phase

- WebAssembly target for browser-based J development
- Enhanced QML/Qt Quick integration (Fat build)
- Improved Android/iOS touch interfaces
- WebEngine-based documentation viewer

### Vision Phase

- Collaborative editing / remote J sessions via WebSocket
- Integrated debugger with breakpoints and variable inspection
- Language Server Protocol (LSP) integration
- Plugin marketplace for community extensions

### Out of Scope

- Visual form designer / drag-and-drop GUI builder (WD forms are code-defined)
- Automated refactoring tools for J code
- Full version control UI beyond git status display
- Cloud-hosted IDE or SaaS deployment
- Non-J language support (syntax highlighting, execution)
- Package registry hosting (addon packages are managed by jsoftware.com)

---

## User Journeys

### UJ-01: Write and Execute a J Script

**Persona:** J Developer
**Goal:** Create, edit, and run a J script interactively

1. User opens QTIDE; previous session restores (open files, window positions)
2. User creates a new script via File > New or Ctrl+N
3. User writes J code in the tabbed editor with syntax highlighting
4. User executes selected lines (Ctrl+Enter) or entire script (Ctrl+Shift+Enter)
5. Output appears in the terminal pane below the editor
6. User iterates: edits code, re-executes, observes results
7. User saves the script (Ctrl+S) and closes

**Traces to:** SC-03, SC-02, FR-01–FR-06

### UJ-02: Manage a J Project

**Persona:** J Application Builder
**Goal:** Organize related J scripts into a project, build, and snapshot

1. User creates a new project via Project > New
2. User adds scripts to the project; sidebar shows project files
3. User navigates between files using tabs or sidebar
4. User runs the project build command (Project > Build)
5. User takes a project snapshot for version management
6. User closes the project; state is saved for restoration

**Traces to:** SC-03, FR-15–FR-19, FR-37–FR-38

### UJ-03: Build a GUI Application with WD

**Persona:** J Application Builder
**Goal:** Create a desktop GUI form using the `wd` verb from J

1. User writes J script containing `wd` commands to define a form layout
2. Commands like `wd 'form create ...'`, `wd 'button create ...'`, `wd 'edit create ...'` build the UI
3. User runs the script; a native GUI window appears with the defined widgets
4. User interacts with the GUI; events trigger J callbacks
5. User queries widget state via `wd 'get id property'` and updates via `wd 'set id value'`
6. User iterates on layout and behavior in the editor, re-running to see changes

**Traces to:** SC-04, SC-07, FR-20–FR-28

### UJ-04: Explore Data with Grid/Cube Viewer

**Persona:** Data Analyst
**Goal:** Visualize multi-dimensional J array data in a grid

1. User loads data into J arrays via terminal or script
2. User invokes the grid/cube viewer widget
3. Grid displays data with hierarchical row/column headers
4. User navigates dimensions, sorts, and inspects values
5. User exports or copies data for further analysis

**Traces to:** SC-04, FR-25

### UJ-05: Learn J with Interactive Help

**Persona:** J Student
**Goal:** Access vocabulary help and demos while coding

1. User positions cursor on a J primitive in the editor
2. User presses F1; context-sensitive vocabulary help appears
3. User browses demos via Help > Demos
4. User steps through Lab topics (F6 advances)
5. User accesses addon documentation via Help > Addons

**Traces to:** FR-29–FR-31

### UJ-06: Configure the IDE

**Persona:** J Developer
**Goal:** Customize editor appearance and behavior

1. User opens Edit > Preferences or equivalent
2. User changes font, theme (dark/light), tab width, line wrap, line numbers
3. User configures custom key bindings
4. User sets function key mappings (F1–F12)
5. User adjusts file type associations and default extensions
6. Settings persist across sessions

**Traces to:** FR-32–FR-36

### UJ-07: Automate IDE via Remote Control

**Persona:** J Application Builder / Integration Developer
**Goal:** Control the IDE programmatically from external tools or J scripts

1. External tool connects to IDE via WebSocket on a configured port
2. Tool sends a command to execute a J expression remotely
3. IDE executes the expression and returns output via WebSocket
4. J program registers a callback to receive IDE events (file open, window focus)
5. IDE invokes the callback when the triggering event occurs
6. Developer builds automated workflows combining external tooling with IDE capabilities

**Traces to:** SC-02, FR-42–FR-43

---

## Domain Requirements

### Programming Language IDE Domain

| ID | Requirement | Rationale |
|----|-------------|-----------|
| DR-01 | Syntax-aware editing for J tokens (verbs, adverbs, conjunctions, nouns, control structures, comments, strings, numbers) | Core IDE function for a language-specific editor |
| DR-02 | REPL with command history and output display | Standard for interpreted/interactive languages |
| DR-03 | Script execution granularity: line, selection, entire script, clipboard | J development workflow requires incremental execution |
| DR-04 | Unicode and ASCII mode support | J traditionally uses ASCII; modern usage includes Unicode |

---

## Innovation Analysis

### Competitive Landscape

| Tool | Strengths | QTIDE Advantage |
|------|-----------|-----------------|
| **JConsole (text terminal)** | Lightweight, always available | Full GUI editor, syntax highlighting, project management, widget framework |
| **Generic text editors (VS Code, Emacs)** | Plugin ecosystems, modern UI | Native J engine integration, WD widget framework, context-sensitive J help |
| **Jupyter/JLab** | Notebook paradigm, visualization | Native desktop performance, WD GUI toolkit, OpenGL integration |

### Key Differentiators

1. **Integrated widget framework** — No other J tool provides a 50+ widget GUI toolkit accessible from J code
2. **Native J engine bridge** — Direct C-level integration via JDo/Wd callbacks, zero serialization overhead
3. **9-platform coverage** — Single codebase targeting desktop, mobile, embedded, and web platforms
4. **Dual-mode architecture** — Functions as both an IDE and a runtime for J GUI applications

---

## Project-Type Requirements

### Desktop Application (Qt/C++)

| ID | Requirement | Detail |
|----|-------------|--------|
| PT-01 | Qt framework compatibility | Support Qt 5.0–6.10 with compile-time version detection (QT50–QT610 defines) |
| PT-02 | Three build variants | Full (all features), Fat (includes QML/Quick), Slim (no multimedia/webkit/webengine/quick) |
| PT-03 | Cross-compilation | Build scripts for each target platform with architecture-specific flags |
| PT-04 | Library + executable architecture | Produce both `jqt` executable and `libjqt` shared library |
| PT-05 | C ABI for J integration | Extern "C" interface: `JDo()`, `Wd()`, `Joutput()`, `Jinput()`, `smoptions()` |

### Mobile (Android/iOS)

| ID | Requirement | Detail |
|----|-------------|--------|
| PT-06 | Android JNI integration | Shared library (`qtide`) loadable via Android NDK |
| PT-07 | Density-aware UI | Scale UI elements for varying screen densities |
| PT-08 | Virtual function keys | On-screen F1–F12 keys for devices without physical keyboards |

---

## Functional Requirements

### Editor (Note System)

| ID | Requirement | Traces To |
|----|-------------|-----------|
| FR-01 | Users can open, create, save, and close script files in a multi-tabbed editor | UJ-01, SC-03 |
| FR-02 | Editor displays J syntax highlighting for keywords, verbs, adverbs, conjunctions, nouns, numbers, strings, comments (single-line and multi-line), noun definitions, and control structures | UJ-01, DR-01 |
| FR-03 | Users can execute the current line, selection, entire script, or clipboard content in the J engine | UJ-01, DR-03 |
| FR-04 | Editor supports undo/redo (unlimited), cut/copy/paste, select all, and drag-and-drop text operations | UJ-01 |
| FR-05 | Find and replace within the current editor tab with regex support | UJ-01 |
| FR-06 | Find in Files (FIF): search across project or directory with file type filtering and regex support | UJ-01, UJ-02 |
| FR-07 | Editor displays optional line numbers in a gutter alongside the code | UJ-01, UJ-06 |
| FR-08 | Editor supports configurable line wrap and tab width | UJ-06 |
| FR-09 | Editor supports ASCII display mode for J primitives | DR-04 |
| FR-10 | Editor trims trailing whitespace on save (configurable) | UJ-06 |

### Terminal (Term System)

| ID | Requirement | Traces To |
|----|-------------|-----------|
| FR-11 | Users can enter J expressions in a terminal REPL with immediate execution and output display | UJ-01, DR-02 |
| FR-12 | Terminal maintains command history navigable via keyboard | DR-02 |
| FR-13 | Terminal supports J syntax highlighting for input and output | DR-01 |
| FR-14 | Terminal supports tab completion for J names and file paths | UJ-01 |

### Project Management

| ID | Requirement | Traces To |
|----|-------------|-----------|
| FR-15 | Users can create, open, close, and switch between J projects | UJ-02 |
| FR-16 | Projects track associated files; sidebar displays project file tree | UJ-02 |
| FR-17 | Users can build/run a project via a configurable build command | UJ-02 |
| FR-18 | Users can take and restore project snapshots (state preservation) | UJ-02 |
| FR-19 | Session state (open files, window positions, cursor positions) persists across IDE restarts | UJ-01, UJ-02, SC-06 |

### Widget Framework (WD System)

| ID | Requirement | Traces To |
|----|-------------|-----------|
| FR-20 | J programs can create, modify, and destroy GUI forms via `wd` verb string commands | UJ-03, SC-04 |
| FR-21 | WD supports input widgets: Button, CheckBox, RadioButton, LineEdit, Edit (rich text), ComboBox, DateEdit, TimeEdit, SpinBox, DoubleSpinBox, Dial, Slider, ProgressBar | UJ-03, SC-04 |
| FR-22 | WD supports container widgets: Form, Pane (grid/bin layout), Layout (box layouts), TabWidget, ScrollArea, GroupBox, Splitter | UJ-03 |
| FR-23 | WD supports display widgets: Label, Static, Browser (HTML), Table, TreeView, ListBox, Image, Bitmap, StatusBar, ToolBar | UJ-03 |
| FR-24 | WD supports graphics widgets: Isigraph (J visualization), OpenGL rendering, SVGView, WebView/WebEngineView, QuickView (QML), Multimedia, DrawObj (vector drawing) | UJ-03 |
| FR-25 | WD supports grid/cube widget for multi-dimensional array visualization with hierarchical headers | UJ-04 |
| FR-26 | WD provides unified get/set property interface: `wd 'get id property'` / `wd 'set id value'` across all widget types | UJ-03, SC-07 |
| FR-27 | WD delivers events to J callbacks when users interact with widgets (button clicks, text changes, menu selections, etc.) | UJ-03 |
| FR-28 | WD supports dialog creation: file open/save, font selection, color picker, message boxes, print dialogs | UJ-03 |

### Help System

| ID | Requirement | Traces To |
|----|-------------|-----------|
| FR-29 | Context-sensitive vocabulary help: pressing F1 with cursor on a J primitive opens its documentation | UJ-05 |
| FR-30 | Users can browse demos, labs, and addon documentation from the Help menu | UJ-05 |
| FR-31 | Users can access J language reference, vocabulary, and constants from the Help menu | UJ-05 |

### Configuration

| ID | Requirement | Traces To |
|----|-------------|-----------|
| FR-32 | Users can configure editor font (family, size), foreground/background colors, and theme (dark/light) | UJ-06 |
| FR-33 | Users can define custom key bindings for all IDE actions | UJ-06 |
| FR-34 | Users can map function keys (F1–F12) to arbitrary J expressions | UJ-06 |
| FR-35 | Users can configure file type associations, default file extension, and recent file list size | UJ-06 |
| FR-36 | All configuration settings persist in file-based storage (INI/JSON) and survive IDE restarts | UJ-06 |

### Git Integration

| ID | Requirement | Traces To |
|----|-------------|-----------|
| FR-37 | Users can view git status of the current project from within the IDE | UJ-02 |
| FR-38 | Users can open a git terminal or file manager scoped to the project directory | UJ-02 |

### File Operations

| ID | Requirement | Traces To |
|----|-------------|-----------|
| FR-39 | Users can read and write Excel workbook files (.xlsx) from J | UJ-04 |
| FR-40 | Users can print editor content and preview print output | UJ-01 |
| FR-41 | Users can export terminal or editor output to a file | UJ-01 |

### Communication

| ID | Requirement | Traces To |
|----|-------------|-----------|
| FR-42 | External tools and J programs can connect to the IDE via WebSocket to execute J expressions remotely and retrieve output, using a documented message protocol on a configurable port | UJ-07, SC-02 |
| FR-43 | J programs can register callback functions that the IDE invokes when specific events occur (file open, window focus, timer tick), enabling event-driven J automation without polling | UJ-07, SC-02 |

---

## Non-Functional Requirements

### Performance

| ID | Requirement | Measurement |
|----|-------------|-------------|
| NFR-01 | IDE startup (cold launch to usable terminal) completes within 3 seconds on a system with SSD and 8GB RAM | Timed from process start to first prompt |
| NFR-02 | Editor opens files up to 10MB without blocking the UI thread for more than 200ms | File load time measured; UI thread block duration profiled |
| NFR-03 | Syntax highlighting updates within 100ms of keystroke for files up to 10,000 lines | Visual delay measurement |
| NFR-04 | J expression execution through the native bridge adds less than 1ms overhead beyond J engine processing time | Benchmark bridge call vs. direct console execution |

### Compatibility

| ID | Requirement | Measurement |
|----|-------------|-------------|
| NFR-05 | IDE compiles and runs correctly against Qt 5.0 through Qt 6.10 | CI build matrix across Qt versions |
| NFR-06 | IDE binary runs on all 9 target platforms (Windows 32/64, macOS x86_64/ARM64, Linux x86_64/aarch64, Android ARM64/ARMv7, Raspberry Pi) | Platform-specific test passes |
| NFR-07 | WD verb commands maintain backward compatibility: scripts written for JQt v2.0 execute without modification on v2.6 | Regression test suite of WD scripts |

### Maintainability

| ID | Requirement | Measurement |
|----|-------------|-------------|
| NFR-08 | Codebase compiles with g++, clang, and MSVC without warnings at default warning level | CI build logs |
| NFR-09 | Build variants (Full, Fat, Slim) are controlled via environment variables without source modification | Build script verification |
| NFR-10 | A developer can add a new widget type to the WD framework and have it functional (responding to get/set/events) within 1 working day, following documented extension patterns | Time-to-implement measurement for a reference widget |

### Reliability

| ID | Requirement | Measurement |
|----|-------------|-------------|
| NFR-11 | IDE remains operational after J engine errors (stack overflow, domain error, out of memory): displays an error message in the terminal and accepts new input within 2 seconds | Error injection test suite; post-error input acceptance verification |
| NFR-12 | Unsaved file changes prompt a confirmation dialog before close/exit (configurable via ConfirmSave) | UI interaction test |

### Usability

| ID | Requirement | Measurement |
|----|-------------|-------------|
| NFR-13 | All IDE operations are accessible via keyboard shortcuts in addition to menus | Keyboard shortcut audit against menu action list |
| NFR-14 | IDE provides 150+ menu actions organized into File, Edit, View, Run, Project, Tools, Help categories | Menu item count verification |

### Portability

| ID | Requirement | Measurement |
|----|-------------|-------------|
| NFR-15 | Single C++ codebase compiles to all target platforms; platform-specific conditional compilation blocks do not exceed 50 across the entire codebase | Automated count of platform-conditional blocks in source |
| NFR-16 | Application binary size remains under 5MB (excluding Qt libraries) for the Slim build variant | Binary size measurement post-build |

---

## Technical Architecture Summary

### Component Architecture

```
┌─────────────────────────────────────────────────┐
│                    jqt (executable)              │
│         main.cpp → jepath.cpp → dllsrc/          │
├─────────────────────────────────────────────────┤
│                 libjqt (shared library)          │
│  ┌──────────┐ ┌──────────┐ ┌──────────────────┐ │
│  │  base/   │ │   wd/    │ │     grid/        │ │
│  │ (IDE)    │ │ (Widgets)│ │ (Cube/Matrix)    │ │
│  │ Note     │ │ Form     │ │ QGrid            │ │
│  │ Term     │ │ Pane     │ │ CubeView         │ │
│  │ Nedit    │ │ Child*   │ │ HierGrid         │ │
│  │ Tedit    │ │ Layout   │ └──────────────────┘ │
│  │ Config   │ │ Dialog   │ ┌──────────────────┐ │
│  │ State    │ │ Menu     │ │     high/        │ │
│  │ Project  │ │ DrawObj  │ │ (Syntax Highlight)│ │
│  └──────────┘ └──────────┘ └──────────────────┘ │
│  ┌──────────────────┐ ┌──────────────────────┐  │
│  │    excel/         │ │   QtWebsocket/       │  │
│  │   (QXlsx)        │ │  (WebSocket I/O)     │  │
│  └──────────────────┘ └──────────────────────┘  │
├─────────────────────────────────────────────────┤
│           J Engine (libjsdk) — External          │
│     JDo() | Wd() | Joutput() | Jinput()          │
└─────────────────────────────────────────────────┘
```

### J Engine Integration (C ABI)

```c
// Execute J expression
int JDo(void *jt, char *sentence);

// Widget operations from J
int wd(char *command, int cmdlen, char *&result, int &resultlen);

// Callback table registered at startup
void* callbacks[] = { Joutput, Jwd, Jinput, unused, smoptions };
```

### Data Model

**J Array Type (A):**
- Fields: TYPE (B01/LIT/INT/FL/CMPX/BOX/XNUM/RAT), RANK, SHAPE, ravel data
- Supports boolean, character, integer, float, complex, boxed, extended precision, and rational types
- Arbitrary dimensionality

**Configuration Model:**
- In-memory: `QMap<QString, QVariant>` singleton (Config class, 100+ properties)
- On-disk: INI/JSON format in user config directory
- Properties: fonts, colors, paths, key bindings, window positions, file cursor positions, project state

### Build System

| Variable | Effect |
|----------|--------|
| `JQTFAT` | Include QML/Quick support |
| `JQTSLIM` | Exclude multimedia, webkit, webengine, quick |
| `NO_WEBENGINE` | Disable WebEngine module |
| `JQTWEBKIT` | Use WebKit instead of WebEngine |
| `NO_OPENGL` | Disable OpenGL support |
| `JQTRPATH` | Enable RPATH on Linux for SDK distribution |

**Output:** Platform-specific binaries in `bin/{platform}-{arch}/release/`

---

## Appendix: Widget Type Inventory

| Category | Widgets |
|----------|---------|
| **Input** | Button, CheckBox, RadioButton, LineEdit, Edit, ComboBox, DateEdit, TimeEdit, SpinBox, DoubleSpinBox, Dial, Slider, ProgressBar |
| **Container** | Form, Pane, Layout, TabWidget, Tabs, ScrollArea, Scrollbar, GroupBox, Splitter |
| **Display** | Label, Static, Browser, Table, TreeView, ListBox, Image, Bitmap, StatusBar, ToolBar |
| **Graphics** | Isigraph, Isigraph2, Isigrid, OpenGL, OpenGL2, Ogl2, Gl2, Gl2class, SVGView, SVGView2, DrawObj |
| **Web/Media** | WebView, WebEngineView, QuickView1, QuickView2, QuickWidget, Multimedia |
| **Data** | QGrid (cube/matrix viewer with hierarchical headers) |
| **Dialogs** | File Open/Save, Font Selection, Color Picker, Message Box, Print Dialog, Print Preview |

---

## Appendix: Menu Structure

| Menu | Key Actions |
|------|-------------|
| **File** | New, Open, Save, Save As, Save All, Close, Close All, Recent Files, Addons, Directory Manager, Print, Quit |
| **Edit** | Undo, Redo, Cut, Copy, Paste, Select All, Find, Replace, Find in Files, Preferences |
| **View** | Font Size (+/-), Line Numbers, Line Wrap, ASCII Mode, Sidebar Toggle, Terminal Toggle |
| **Run** | Execute Line, Execute Selection, Execute Script, Execute Clipboard, Debug, Pretty Print, Toggle Comment |
| **Project** | New, Open, Close, Build, Run, Snapshot, Restore |
| **Tools** | Directory Manager, Package Manager, Function Keys, Launch Pad, Git Status, Git Terminal |
| **Help** | Vocabulary (F1), Demos, Labs, Constants, About |

---

## References

- [J Programming Language — jsoftware.com](https://www.jsoftware.com/)
- [QTIDE GitHub Repository](https://github.com/jsoftware/qtide)
- [Qt IDE Installation Guide — J Wiki](https://code.jsoftware.com/wiki/Guides/Qt_IDE/Install)
- [Qt IDE User Guide — J Wiki](https://code.jsoftware.com/wiki/Guides/Qt_IDE)
- [J Language — Wikipedia](https://en.wikipedia.org/wiki/J_(programming_language))
- [J Language — APL Wiki](https://aplwiki.com/wiki/J)
