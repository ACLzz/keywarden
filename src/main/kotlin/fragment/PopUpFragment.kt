package fragment

import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Pos
import tornadofx.*

class PopUpFragment : Fragment() {
    val text: String by param()
    val warning: Boolean by param()
    val removeMe = SimpleBooleanProperty(false)

    override val root = vbox {
        style {
            alignment = Pos.BOTTOM_RIGHT
            paddingAll = 10.0
        }

        removeWhen(removeMe)

        label {
            text = this@PopUpFragment.text

            style {
                textFill = c("#C7C7C7")
                paddingAll = 7.0
                backgroundColor += if (warning) {
                    c("#9f0000")
                } else {
                    c("#98936E")
                }
            }
            runLater(5.seconds) {
                removeMe.set(true)
            }

            setOnMouseClicked {
                removeMe.set(true)
            }
        }
    }
}