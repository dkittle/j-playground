# J Engine Interface Design

**Purpose:** Reference document for developers implementing platform-specific J engine bindings in j-playground.

**Scope:** Documents how j901 (iOS) and QTIDE (desktop) interface with the J engine at the C level, extracts the common patterns, and maps them to j-playground's KMP architecture.

---

## 1. J Engine C API

Both j901 and QTIDE use the same underlying C API to interact with the J interpreter. The API surface is small:

```c
// Allocate and initialize a J interpreter instance.
// Returns an opaque pointer (jt) used in all subsequent calls.
void* JInit(void);

// Execute a J sentence. Returns 0 on success, non-zero on error.
// Output is NOT returned here -- it arrives via the Joutput callback.
int JDo(void *jt, char *sentence);

// Free the interpreter instance.
int JFree(void *jt);

// Callback: engine calls this to emit output.
// type: 0 = normal output, 1 = error output, 2 = log output
void Joutput(void *jt, int type, char *s);

// Callback: engine calls this when it needs input (e.g. 1!:1[1).
// Blocks the engine thread until the host provides a string.
char* Jinput(void *jt, char *prompt);
```

Key properties of this API:

- **JDo does not return output.** It returns an error code. All textual output (results, errors, log messages) arrives asynchronously via the `Joutput` callback during or after `JDo` execution.
- **Jinput blocks the engine thread.** When J code requests user input, the engine calls `Jinput` and halts until the host returns a C string.
- **The engine is single-threaded.** Concurrent calls to `JDo` on the same `jt` instance corrupt interpreter state. All access must be serialized.
- **The callback table is registered at startup.** The host provides function pointers for `Joutput`, `Jinput`, and (in QTIDE) `Wd` and `smoptions` before any `JDo` calls.

---

## 2. j901 Approach (iOS/Swift)

j901 wraps the C API in an Objective-C bridge file (`je-glue.m`) and communicates with the Swift UI layer via `NSNotification` IPC.

### 2.1 Architecture

```
Swift UI (HomeVC, EditVC, ...)
       |
       | NSNotification IPC
       v
Objective-C Bridge (je-glue.m)
  runs() -> JInit() + JDo()
  joutput() callback
  jinput() callback
       |
       | Direct C calls
       v
J Engine (jsrc-903k, statically linked)
```

### 2.2 Initialization

- **Lazy.** The engine is initialized on the first call to `runs()`, not at app launch.
- `JInit()` allocates the interpreter. A boot sentence then loads `ios.ijs`, which sets up `VERSION_z_`, `GEOMETRY_j_`, `DATESTAMP_j_`, and the iOS locale.

### 2.3 Execution Dispatch

1. User types a J expression in the terminal.
2. `EngineDelegate.workItem()` dispatches to `DispatchQueue.global()` (background thread).
3. Multi-line input: remaining lines are staged in `jinput_buffer` (9999 bytes). Each subsequent `jinput()` callback pops the next line from this buffer.
4. `runs(firstLine)` calls `JDo(jt, sentence)`.
5. A dispatch semaphore serializes all `workItem` calls -- the next evaluation cannot begin until the current one completes.

### 2.4 Output Capture

```
JDo() executing...
  -> J engine calls joutput(jt, type, result)
       -> outputbuf accumulates the string
       -> posts NSNotification @"action_joutput"
  -> Swift observer receives notification on main thread
       -> appends to terminal UI
```

- Output arrives incrementally during execution, not as a single block after `JDo` returns.
- `outputbuf` is a shared C buffer that accumulates output across multiple `joutput` calls within a single `JDo` invocation.

### 2.5 Input Handling

```
JDo() executing J code that calls 1!:1[1 ...
  -> J engine calls jinput(jt, prompt)
       -> checks jinput_buffer for pre-staged lines
       -> if empty: posts NSNotification @"action_jinput"
                    blocks on dispatch semaphore
       -> Swift UI shows input prompt to user
       -> user types response
       -> response written to jinput_buffer
       -> semaphore signaled
       -> jinput() returns the user's string to the engine
  -> JDo() resumes
```

- `jinput()` blocks the engine thread. This is by design -- the J engine cannot proceed without input.
- The semaphore is the synchronization primitive between the engine thread (blocked in `jinput`) and the main thread (waiting for user input).

### 2.6 The 2!:9 Foreign Conjunction Bridge

j901 extends the J engine with `2!:9`, a custom foreign conjunction that enables J code to call 66 iOS UI operations:

```
J: 'beep' ui 1322
  -> F2(jtforeign) routes m=2, n=9
    -> x2ui_IAC.c: F2(jtx2ui2)
      -> packs x-arg and y-arg into NSString in bangco_buffer (99999 bytes)
      -> posts NSNotification @"finish_2bangco9"
  -> Swift: TabBarController.action_2bangco9()
    -> service_ui(x: "beep", y: "1322")
      -> dispatches to beep(), alert(), goto(), setclip(), etc.
```

This bridge is specific to j901. j-playground does not need it for the MVP REPL, but the pattern (foreign conjunction -> host callback -> platform operation) is instructive for future extensibility.

### 2.7 Shared Buffers Summary

| Buffer | Size | Purpose |
|--------|------|---------|
| `outputbuf` | Dynamic | Accumulates `joutput()` text within a single execution |
| `jinput_buffer` | 9999 bytes | Pre-staged multi-line input; popped by `jinput()` |
| `inputbuf` | Variable | User input staging from Swift UI |
| `bangco_buffer` | 99999 bytes | Arguments for 2!:9 foreign conjunction |

---

## 3. QTIDE Approach (Desktop/C++)

QTIDE interfaces with the J engine via a direct C ABI. The engine is loaded as an external shared library (`libjsdk`).

### 3.1 Architecture

```
Qt Application (Note, Term, Nedit, Tedit, ...)
       |
       | Direct C++ calls
       v
C ABI Bridge (extern "C")
  JDo(), Wd(), Joutput(), Jinput(), smoptions()
       |
       | Shared library (libjsdk)
       v
J Engine (dynamically loaded)
```

### 3.2 C ABI Surface

```c
// Execute a J sentence
int JDo(void *jt, char *sentence);

// Widget framework operations (QTIDE-specific)
int wd(char *command, int cmdlen, char *&result, int &resultlen);

// Callback table registered at startup
void* callbacks[] = { Joutput, Jwd, Jinput, unused, smoptions };
```

### 3.3 Callback Registration

At startup, QTIDE registers a callback table with the J engine. This table provides function pointers for:

- **Joutput** -- receives engine output (same semantics as j901's `joutput`)
- **Jinput** -- provides user input when the engine requests it (same semantics as j901's `jinput`)
- **Jwd** -- routes `wd` verb calls from J to Qt widget operations
- **smoptions** -- provides startup/configuration options

### 3.4 Qt Event Loop Integration

- J execution happens on a background thread (or is dispatched from the Qt event loop).
- `Joutput` callbacks marshal text back to the Qt UI thread for display in the terminal widget.
- `Jinput` blocks the engine thread; the Qt event loop continues running on the main thread, allowing the user to type input.
- Widget operations via `Wd()` are dispatched to the Qt main thread since Qt widgets are not thread-safe.

### 3.5 Engine Loading

- QTIDE loads the J engine as a shared library (`libjsdk`) at startup.
- The executable (`jqt`) resolves the engine path via `jepath.cpp`, then dynamically loads the library and resolves function pointers.
- This contrasts with j901, which statically links the engine into the app binary (required by iOS).

---

## 4. Common Patterns

Despite different languages (Swift/ObjC vs. C++/Qt) and platforms (iOS vs. desktop), both products share the same fundamental patterns:

### 4.1 Engine is Single-Threaded

The J interpreter is not thread-safe. Both products enforce serialized access:

| Product | Serialization Mechanism |
|---------|------------------------|
| j901 | Dispatch semaphore on `DispatchQueue.global()` |
| QTIDE | Single execution thread; UI dispatches to engine thread |

**Implication for j-playground:** All `JDo` calls must be serialized. Concurrent calls corrupt interpreter state. This is non-negotiable.

### 4.2 Output via Callback, Not Return Value

`JDo()` returns an integer error code, not output text. Both products capture output by registering a `Joutput` callback:

```
JDo("2+2")
  -> returns 0 (success)
  -> Joutput callback receives "4\n" separately
```

**Implication for j-playground:** The `JEngine.eval()` method must internally capture callback output and package it into `JResult`. The caller never sees the callback mechanism.

### 4.3 Input via Blocking Callback

When J code requests user input (`1!:1[1]`), the engine calls `Jinput` and blocks until a string is returned:

```
JDo("input =: 1!:1[1")
  -> engine halts
  -> Jinput(jt, "   ") called
  -> host must provide input
  -> engine resumes with the returned string
```

Both products handle this by blocking the engine thread and signaling from the UI thread.

**Implication for j-playground:** The MVP does not support interactive input. But the architecture must not preclude it. The blocking callback pattern means the engine thread will halt -- the UI thread must remain responsive.

### 4.4 Lazy or Startup Initialization

| Product | Init Strategy |
|---------|---------------|
| j901 | Lazy -- first `runs()` call triggers `JInit()` |
| QTIDE | Startup -- engine loaded during application init |

j-playground uses lazy initialization via `EngineSession.getActiveEngineOrCreate()`.

### 4.5 Errors Are Text, Not Crashes

Both products treat J engine errors as text output, never as application crashes:

- `JDo()` returns a non-zero error code.
- The error message arrives via `Joutput` with `type=1` (error output).
- The host displays the error text and continues accepting input.
- NFR in both products: "100% of J engine errors surface as text output; 0% cause app crashes."

**Implication for j-playground:** `JResult.isError` captures this. The engine binding must catch the error code from `JDo`, collect the error text from the output callback, and return `JResult(output=errorText, isError=true)`.

---

## 5. j-playground KMP Mapping

### 5.1 Existing Abstractions

j-playground already defines the common contract in `commonMain`:

```kotlin
// JEngine.kt -- the platform-agnostic engine interface
interface JEngine {
    fun eval(input: String): JResult
    fun reset()
    fun shutdown()
}

data class JResult(
    val output: String,
    val isError: Boolean,
)
```

```kotlin
// EngineSession.kt -- serialized lifecycle wrapper
class EngineSession(private val engineFactory: () -> JEngine) {
    private val lock = Any()
    private var activeEngine: JEngine? = null
    private var isShutdown = false

    fun eval(input: String): JResult { synchronized(lock) { ... } }
    fun reset() { synchronized(lock) { ... } }
    fun shutdown() { synchronized(lock) { ... } }
}
```

`EngineSession` already enforces:
- **Serialization** via `synchronized(lock)` -- maps to the dispatch semaphore (j901) and single-thread dispatch (QTIDE).
- **Lazy initialization** via `getActiveEngineOrCreate()` -- maps to j901's lazy `JInit()`.
- **Deterministic shutdown** -- `shutdown()` is idempotent; double-call is safe; post-shutdown eval throws `IllegalStateException`.

### 5.2 Platform-Specific Implementations

Each platform implements `JEngine` by binding to the J engine C API through the appropriate FFI mechanism:

```
commonMain/
  JEngine.kt          -- interface (done)
  JResult.kt          -- data class (done, merged into JEngine.kt)
  EngineSession.kt    -- serialized wrapper (done)

jvmMain/
  JvmJEngine.kt       -- JNI binding to libj.so/dylib

androidMain/
  AndroidJEngine.kt   -- JNI binding (same as JVM, different packaging)

iosMain/
  IosJEngine.kt       -- Kotlin/Native cinterop with statically linked libj.a
```

### 5.3 JVM/Desktop Binding (JNI)

```
Kotlin (JvmJEngine) -> JNI -> libj.dylib/so/dll
```

```kotlin
// Sketch of JVM implementation
class JvmJEngine : JEngine {
    private val jt: Long  // opaque pointer, stored as Long

    init {
        System.loadLibrary("j")
        jt = nativeJInit()
        nativeRegisterCallbacks(jt)
    }

    override fun eval(input: String): JResult {
        outputBuffer.clear()
        val errorCode = nativeJDo(jt, input)
        return JResult(
            output = outputBuffer.toString(),
            isError = errorCode != 0
        )
    }

    override fun reset() {
        nativeJFree(jt)
        jt = nativeJInit()
        nativeRegisterCallbacks(jt)
    }

    override fun shutdown() {
        nativeJFree(jt)
    }

    // JNI native methods
    private external fun nativeJInit(): Long
    private external fun nativeJDo(jt: Long, sentence: String): Int
    private external fun nativeJFree(jt: Long): Int
    private external fun nativeRegisterCallbacks(jt: Long)
}
```

The JNI C layer implements the `Joutput` callback by writing to a thread-local or instance-scoped buffer that the Kotlin side reads after `JDo` returns:

```c
// jni_bridge.c (sketch)
static jobject g_output_buffer;  // Java StringBuilder reference

void Joutput(void *jt, int type, char *s) {
    // Append s to the Java-side output buffer via JNI
    JNIEnv *env = get_current_env();
    jstring js = (*env)->NewStringUTF(env, s);
    (*env)->CallVoidMethod(env, g_output_buffer, appendMethod, js);
}
```

### 5.4 Android Binding (JNI + NDK)

Identical C ABI to JVM. Differences are packaging only:

| Aspect | JVM/Desktop | Android |
|--------|-------------|---------|
| Library format | `.dylib` / `.so` / `.dll` | `.so` inside AAR |
| Loading | `System.loadLibrary("j")` | Same, but from APK native libs |
| ABI | Host architecture | `arm64-v8a`, `armeabi-v7a`, `x86_64` |
| Build | Gradle + host compiler | Gradle + NDK cross-compiler |

### 5.5 iOS Binding (Kotlin/Native cinterop)

```
Kotlin/Native (IosJEngine) -> cinterop -> libj.a (static)
```

Kotlin/Native's C interop generates Kotlin bindings from a `.def` file pointing at the J engine headers:

```
// j.def
headers = jlib.h
staticLibraries = libj.a
libraryPaths = /path/to/j/lib
```

```kotlin
// Sketch of iOS implementation
class IosJEngine : JEngine {
    private val jt: COpaquePointer

    init {
        jt = JInit()!!
        // Register callbacks via the J engine's callback registration mechanism
    }

    override fun eval(input: String): JResult {
        outputBuffer.clear()
        val errorCode = JDo(jt, input.cstr.ptr)
        return JResult(
            output = outputBuffer.toString(),
            isError = errorCode != 0
        )
    }

    override fun reset() {
        JFree(jt)
        jt = JInit()!!
    }

    override fun shutdown() {
        JFree(jt)
    }
}
```

Key iOS constraint: the J engine must be statically linked (`libj.a`). iOS prohibits loading dynamic libraries at runtime. This matches j901's approach exactly.

### 5.6 Threading Model

```
UI Thread (Compose)                Engine Thread
     |                                  |
     |  session.eval("2+2")             |
     |  --------------------------->    |
     |  (suspend / withContext)         |
     |                                  | synchronized(lock)
     |                                  | JDo(jt, "2+2")
     |                                  |   -> Joutput callback
     |                                  |   -> buffer.append(output)
     |                                  | return JResult(...)
     |  <---------------------------    |
     |  JResult(output="4",             |
     |          isError=false)          |
     |  Update Compose state            |
```

The current `EngineSession` uses `synchronized(lock)` for serialization. In production, this will be called from a coroutine dispatcher:

```kotlin
// In ViewModel
viewModelScope.launch {
    val result = withContext(Dispatchers.Default) {
        session.eval(input)
    }
    // Update UI state with result
}
```

The `synchronized(lock)` inside `EngineSession` ensures that even if multiple coroutines attempt concurrent evaluation, only one proceeds at a time. This is the KMP equivalent of j901's dispatch semaphore.

---

## 6. Key Design Decisions

### 6.1 Callback-Based Output Capture

**Decision:** `JEngine.eval()` returns a `JResult` containing the captured output, hiding the callback mechanism from callers.

**Rationale:** The J engine's `JDo()` function does not return output text. Output arrives via the `Joutput` callback during execution. Both j901 and QTIDE accumulate this output in a buffer. j-playground does the same inside each platform's `JEngine` implementation, then packages the buffer contents into `JResult.output` before returning. The caller sees a simple request/response API.

### 6.2 Engine Serialization is Non-Negotiable

**Decision:** All engine access goes through `EngineSession`, which holds a lock for the duration of each operation.

**Rationale:** The J engine is a single-threaded C interpreter with global mutable state. Concurrent `JDo` calls cause undefined behavior (memory corruption, crashes). Both j901 (dispatch semaphore) and QTIDE (single-thread dispatch) enforce this. j-playground's `synchronized(lock)` in `EngineSession` provides the same guarantee. There is no safe way to relax this constraint.

### 6.3 Lazy Initialization via Factory

**Decision:** `EngineSession` accepts an `engineFactory: () -> JEngine` and creates the engine on first use.

**Rationale:** Matches j901's lazy-init pattern. The engine is heavyweight (allocates the J interpreter, loads boot scripts). Deferring creation until first `eval()` keeps app startup fast. The factory pattern also enables `reset()` to destroy and recreate the engine without the session holder knowing the platform-specific construction details.

### 6.4 Reset = Destroy + Recreate

**Decision:** `JEngine.reset()` is semantically equivalent to destroying the current interpreter and creating a fresh one.

**Rationale:** The J engine has no public API to "clear all state." Both j901 and QTIDE handle reset by calling `JFree(jt)` followed by `JInit()`. j-playground's `reset()` maps to this same pattern. `EngineSession` can implement this by calling `shutdown()` on the current engine and letting the next `eval()` trigger lazy creation of a new one.

### 6.5 Shutdown is Terminal

**Decision:** After `EngineSession.shutdown()`, further `eval()` calls throw `IllegalStateException`. A new `EngineSession` must be created.

**Rationale:** This prevents use-after-free of the J interpreter. Once `JFree(jt)` is called, the pointer is invalid. The session enforces this at the Kotlin level with the `isShutdown` flag. Double-shutdown is safe (idempotent). This maps cleanly to app lifecycle: activity/fragment destruction calls `shutdown()`; a new activity creates a new session.

### 6.6 Error Discrimination via Error Code + Output Type

**Decision:** `JResult.isError` is derived from the `JDo` return code, and `JResult.output` contains the error text captured from the `Joutput` callback with `type=1`.

**Rationale:** Both j901 and QTIDE distinguish errors the same way. `JDo` returns non-zero on error. The error message text arrives via `Joutput` with type 1 (error) rather than type 0 (normal). The platform binding checks the return code and sets `isError` accordingly. The output buffer captures both normal and error output -- the type flag from `Joutput` can optionally be used to separate them in the future.

---

## 7. Summary: API Mapping Table

| C API | j901 (iOS/ObjC) | QTIDE (Desktop/C++) | j-playground (KMP) |
|-------|------------------|---------------------|---------------------|
| `JInit()` | Called in `runs()` on first use | Called at startup via dlsym | Called in `engineFactory()`, lazy via `EngineSession` |
| `JDo()` | `runs(sentence)` on background queue | Direct call from Term/Note | `JEngine.eval()` inside `synchronized(lock)` |
| `Joutput()` | Callback -> `outputbuf` -> NSNotification | Callback -> Qt signal -> Term widget | Callback -> buffer -> `JResult.output` |
| `Jinput()` | Callback -> semaphore block -> UI input | Callback -> Qt event loop -> user input | Not implemented in MVP; architecture supports it |
| `JFree()` | Not exposed (engine lives for app lifetime) | Called on shutdown | `JEngine.shutdown()` / `reset()` |
| Serialization | Dispatch semaphore | Single-thread dispatch | `synchronized(lock)` in `EngineSession` |
| Engine linking | Static (`libj.a` in app binary) | Dynamic (`libjsdk` shared library) | Static on iOS; dynamic on JVM/Android |
| Callback registration | Set in `je-glue.m` before first `JDo` | Callback table array at startup | Set in platform `JEngine` constructor |

---

## 8. Files Referenced

### j-playground (existing code)
- `/composeApp/src/commonMain/kotlin/com/jsoftware/engine/JEngine.kt` -- `JEngine` interface + `JResult`
- `/composeApp/src/commonMain/kotlin/com/jsoftware/engine/EngineSession.kt` -- serialized lifecycle wrapper
- `/composeApp/src/commonTest/kotlin/com/jsoftware/engine/EngineSessionTest.kt` -- contract + lifecycle tests

### Source PRDs
- `features/other/prd-j901.md` -- Sections 9.2, 9.3, 9.4, 9.9, 9.10
- `features/other/prd-qtide.md` -- Technical Architecture Summary, J Engine Integration (C ABI)
- `features/J Playground PRD.md` -- Sections 6, 8, 9
