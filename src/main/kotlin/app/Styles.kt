package app

import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*
import javax.swing.text.html.StyleSheet

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
    }

    init {
        label and heading {
            padding = box(10.px)
            fontSize = 12.px
            fontWeight = FontWeight.BOLD
        }
    }
}

class ActionBarStyle : Stylesheet() {
    init {

    }
}

class PasswordsTable : Stylesheet() {
    init {
        Companion.tableView {
            // TODO
        }
    }
}

class IconStyle : Stylesheet() {
    companion object {
        val ico by cssclass()
        val fillColor = c("#00000000")
        val strokeColor = Color.BLACK
        val strokeWidth = 2.0
        val size = 24.0
    }

    init {
        ico {
            stroke = strokeColor
            strokeWidth = Dimension(Companion.strokeWidth, Dimension.LinearUnits.px)
            minHeight = Dimension(Companion.size, Dimension.LinearUnits.px)
            minWidth = Dimension(Companion.size, Dimension.LinearUnits.px)
        }
    }
}