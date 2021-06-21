package app

import com.sksamuel.hoplite.ConfigLoader
import javafx.stage.Screen
import java.io.File
import kotlin.io.path.Path

private val configPath = System.getProperty("user.home") + "/.keywarden/config.yml"

object Config {
    var h: Double
    var w: Double
    var url: String = ""
    var savedUsername: String = ""
    var saveUsername: Boolean = false

    init {
        data class YamlConfig(var server_url: String, var save_username: Boolean, var saved_username: String)
        val config = ConfigLoader().loadConfigOrThrow<YamlConfig>(paths=listOf(Path(configPath)))
        with (config) {
            savedUsername = saved_username
            saveUsername = save_username
            url = server_url
        }

        val bounds = Screen.getPrimary().bounds
        h = bounds.height
        w = bounds.width
    }

    fun saveConfig() {
        with (Config) {
            val data = """
                server_url: $url
                save_username: $saveUsername
                saved_username: $savedUsername""".trimIndent()
            File(configPath).writeText(data)
        }
    }

    var k = 0.66
}