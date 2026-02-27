package com.jsoftware.engine

class EngineSession(
    private val engineFactory: () -> JEngine,
) {
    private val lock = Any()
    private var activeEngine: JEngine? = null
    private var isShutdown = false

    fun eval(input: String): JResult {
        synchronized(lock) {
            return getActiveEngineOrCreate().eval(input)
        }
    }

    fun reset() {
        synchronized(lock) {
            getActiveEngineOrCreate().reset()
        }
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

    /** Must be called while holding [lock]. */
    private fun getActiveEngineOrCreate(): JEngine {
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
