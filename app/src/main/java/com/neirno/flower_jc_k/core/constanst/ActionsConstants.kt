package com.neirno.flower_jc_k.core.constanst

object ActionsConstants {
    const val WATERING = 1
    const val FERTILIZING = 2
    const val SPRAYING = 3

    // Если вы хотите формат string -> int, вы можете использовать map:
    val ACTIONS: Map<String, Int> = mapOf(
        "WATERING" to WATERING,
        "FERTILIZING" to FERTILIZING,
        "SPRAYING" to SPRAYING
    )
}