package fragment

import tornadofx.*

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