package app

import javafx.stage.Screen
import java.io.File

object Config {
    val h: Double
    val w: Double
    lateinit var url: String
    init {
        val configPath = System.getProperty("user.home") + "/.keywarden/server_url"
        File(configPath).forEachLine {
            if (it.length > 2) {
                url = it
            }
        }

        val bounds = Screen.getPrimary().bounds
        h = bounds.height
        w = bounds.width
    }

    var k = 0.66
}