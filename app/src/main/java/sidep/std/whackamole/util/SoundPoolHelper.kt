package sidep.std.whackamole.util

import android.content.Context
import android.media.SoundPool
import sidep.std.whackamole.R

object SoundPoolHelper {

    private var soundPool: SoundPool? = null
    private var tapSoundId: Int = 0
    private var gameOverSoundId: Int = 0
    private var isInitialized: Boolean = false

    fun init(context: Context) {
        if (isInitialized) return

        soundPool = SoundPool.Builder().setMaxStreams(3).build()
        tapSoundId = soundPool?.load(context, R.raw.screen_tap, 1) ?: 0
        gameOverSoundId = soundPool?.load(context, R.raw.game_over, 1) ?: 0
        isInitialized = true
    }

    fun playTapSound() = soundPool?.play(tapSoundId, 1f,1f, 0,0,1f)
    fun playGameOverSound() = soundPool?.play(gameOverSoundId, 1f, 1f, 0, 0, 1f)

    fun clean() {
        soundPool?.release()
        soundPool = null
//        isInitialized = false
    }
}