package com.jsoftware.engine

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.fail

class EngineSessionTest {

    @Test
    fun sharedContractIsEnforcedInCommonCode_TC_M1_001() {
        val delegate = RecordingEngine { input ->
            if (input == "2+2") {
                JResult(output = "4", isError = false)
            } else {
                JResult(output = "domain error", isError = true)
            }
        }
        val engine: JEngine = delegate

        val success = engine.eval("2+2")
        assertEquals("4", success.output)
        assertFalse(success.isError)

        val error = engine.eval("bad")
        assertEquals("domain error", error.output)
        assertTrue(error.isError)

        engine.reset()
        engine.shutdown()
        assertEquals(1, delegate.resetCount)
        assertEquals(1, delegate.shutdownCount)
    }

    @Test
    fun exactlyOneInterpreterInstancePerSession_TC_M1_002() {
        val factory = RecordingEngineFactory()
        val session = EngineSession(engineFactory = factory::create)

        session.eval("2+2")
        session.eval("3+3")

        assertEquals(1, factory.createCount)
        assertEquals(listOf("2+2", "3+3"), factory.createdEngines.single().evalInputs)
    }

    @Test
    fun noStatePersistenceAcrossAppRestart_TC_M1_003() {
        val factory = StatefulEngineFactory()
        val sessionA = EngineSession(engineFactory = factory::create)

        val defineResult = sessionA.eval("a=:42")
        assertFalse(defineResult.isError)
        sessionA.shutdown()

        val sessionB = EngineSession(engineFactory = factory::create)
        val lookupResult = sessionB.eval("a")
        assertTrue(lookupResult.isError)
        assertEquals("value error: a", lookupResult.output)
    }

    @Test
    fun deterministicShutdownIncludingRepeatCall_TC_M1_004() {
        val factory = RecordingEngineFactory()
        val session = EngineSession(engineFactory = factory::create)

        session.eval("2+2")
        val firstEngine = factory.createdEngines.single()

        session.shutdown()
        session.shutdown()

        assertEquals(1, firstEngine.shutdownCount)
        try {
            session.eval("3+3")
            fail("Session must reject evaluation after shutdown.")
        } catch (_: IllegalStateException) {
            // Expected: lifecycle owner requires a fresh session after shutdown.
        }

        val freshSession = EngineSession(engineFactory = factory::create)
        val freshResult = freshSession.eval("1+1")
        assertFalse(freshResult.isError)
        assertEquals(2, factory.createCount)
    }
}

private class RecordingEngineFactory {
    var createCount: Int = 0
        private set
    val createdEngines: MutableList<RecordingEngine> = mutableListOf()

    fun create(): JEngine {
        createCount += 1
        return RecordingEngine().also { createdEngines += it }
    }
}

private class RecordingEngine(
    private val evaluator: (String) -> JResult = { input ->
        JResult(output = input, isError = false)
    },
) : JEngine {
    val evalInputs: MutableList<String> = mutableListOf()
    var resetCount: Int = 0
        private set
    var shutdownCount: Int = 0
        private set

    override fun eval(input: String): JResult {
        evalInputs += input
        return evaluator(input)
    }

    override fun reset() {
        resetCount += 1
    }

    override fun shutdown() {
        shutdownCount += 1
    }
}

private class StatefulEngineFactory {
    var createCount: Int = 0
        private set

    fun create(): JEngine {
        createCount += 1
        return StatefulSymbolEngine()
    }
}

private class StatefulSymbolEngine : JEngine {
    private val symbols: MutableMap<String, String> = mutableMapOf()

    override fun eval(input: String): JResult {
        val trimmedInput = input.trim()
        if ("=:" in trimmedInput) {
            val (name, value) = trimmedInput.split("=:", limit = 2)
            symbols[name.trim()] = value.trim()
            return JResult(output = "", isError = false)
        }

        val value = symbols[trimmedInput]
        return if (value == null) {
            JResult(output = "value error: $trimmedInput", isError = true)
        } else {
            JResult(output = value, isError = false)
        }
    }

    override fun reset() {
        symbols.clear()
    }

    override fun shutdown() = Unit
}
