package com.jsoftware.engine

class EngineSession(
    private val engineFactory: () -> JEngine,
) {
    private val lock = Any()
    private var activeEngine: JEngine? = null
    private var isShutdown = false

    fun eval(input: String): JResult {
        return getActiveEngineOrCreate().eval(input)
    }

    fun reset() {
        getActiveEngineOrCreate().reset()
    }

    fun shutdown() {
        synchronized(lock) {
            val engine = activeEngine
            activeEngine = null
            if (!isShutdown && engine != null) {
                engine.shutdown()
            }
            isShutdown = true
        }
    }

    private fun getActiveEngineOrCreate(): JEngine {
        synchronized(lock) {
            check(!isShutdown) {
                "EngineSession has been shut down. Create a new session to continue."
            }

            val engine = activeEngine
            if (engine != null) {
                return engine
            }

            return engineFactory().also { createdEngine ->
                activeEngine = createdEngine
            }
        }
    }
}
