package fragment

import javafx.stage.StageStyle
import tornadofx.*

fun popNotify(scope: Scope, text: String, warning: Boolean) {
    find<PopUpFragment>(scope, mapOf(PopUpFragment::text to text, PopUpFragment::warning to warning)).openModal(stageStyle = StageStyle.UNDECORATED)
    if (warning) {
        println("WARNING: $text")
    }
}

class PopUpFragment : Fragment() {
    // TODO use stackpane
    val text: String by param()
    val warning: Boolean by param()

    override val root = pane {
        style {
            backgroundColor += if (warning) {
                c("#9f0000")
            } else {
                c("#98936E")
            }
        }

        label {
            text = this@PopUpFragment.text
            textFill = c("#C7C7C7")
            paddingAll = 7.0
        }
        runLater(5.seconds) {
            super.close()
        }

        setOnMouseClicked {
            super.close()
        }
    }
}