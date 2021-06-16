package fragment

import app.*
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import tornadofx.*

class ActionBar : Fragment() {
    override val root = hbox {
        val h = 50.0
        minHeight = h
        maxHeight = h
        addClass(ActionBarStyle.bar)

        this += ActionButton("Add", PlusIcon()).apply {
            style {
                paddingLeft = 15.0
            }
        }
        this += ActionButton("Edit", EditIcon())
        this += ActionButton("Remove", TrashIcon())

        region {
            hgrow = Priority.ALWAYS
        }

        hbox {
            alignment = Pos.CENTER_RIGHT
            label {
                this.text = "username"
                addClass(ActionBarStyle.username)
            }
            style {
                paddingRight = 15.0
            }
        }
    }
}

class ActionButton(text: String, icon: Icon) : Fragment() {
    override val root = hbox {
        alignment = Pos.CENTER_LEFT
        label {
            this.text = text
            addClass(ActionBarStyle.actionLabel)
        }
        vbox {
            alignment = Pos.CENTER_RIGHT
            this += icon
        }
        pane {
            val w = 30.0
            maxWidth = w
            minWidth = w
        }
    }
}