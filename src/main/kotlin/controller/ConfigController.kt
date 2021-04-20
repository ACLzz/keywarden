package controller

import javafx.stage.Screen
import tornadofx.*

class ConfigController : Controller() {
    val h: Double
    val w: Double

    init {
        val bounds = Screen.getPrimary().bounds
        h = bounds.height
        w = bounds.width
    }
    val k = 0.66
}