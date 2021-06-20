package fragment

import app.*
import controller.Client
import controller.MainController
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import tornadofx.*

class ActionBar(private val fetchAndUpdatePasswords: () -> Unit) : Fragment() {
    private val mainController: MainController by inject()

    override val root = hbox {
        val h = 50.0
        minHeight = h
        maxHeight = h
        addClass(ActionBarStyle.bar)

        this += ActionButton("Add", PlusIcon()).root.apply {
            style {
                paddingLeft = 15.0
            }

            setOnMouseClicked {
                Client.Passwords.createPassword(mainController.selectedCollectionProperty.value, "Untitled")
                fetchAndUpdatePasswords()
            }
        }
        this += ActionButton("Remove", TrashIcon()).root.apply {
            fun del() {
                if (mainController.selectedPasswordProperty.value == null)
                    return

                Client.Passwords.deletePassword(mainController.selectedPasswordProperty.value.id,
                    mainController.selectedCollectionProperty.value)
                fetchAndUpdatePasswords()
            }

            setOnMouseClicked {
                del()
            }
        }

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

class ActionButton(text: String?, icon: Icon, paddingRight: Double = 30.0) : Fragment() {
    override val root = hbox {
        alignment = Pos.CENTER_LEFT
        if (text != null) {
            label {
                this.text = text
                addClass(ActionBarStyle.actionLabel)
            }
        }
        vbox {
            alignment = Pos.CENTER_RIGHT
            this += icon
        }
        pane {
            maxWidth = paddingRight
            minWidth = paddingRight
        }
    }
}