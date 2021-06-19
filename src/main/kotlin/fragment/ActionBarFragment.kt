package fragment

import app.*
import controller.Client
import controller.MainController
import javafx.beans.property.StringProperty
import javafx.geometry.Pos
import javafx.scene.control.TablePosition
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCombination
import javafx.scene.layout.Priority
import model.Password
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
                if (mainController.selectedItemProperty.value == null)
                    return

                Client.Passwords.deletePassword(mainController.selectedItemProperty.value.id,
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