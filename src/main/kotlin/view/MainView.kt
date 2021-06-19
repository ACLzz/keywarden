package view

import app.Config
import app.mainBackgroundColor
import app.secondForegroundColor
import app.whiteColor
import controller.Client
import controller.MainController
import fragment.*
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.input.KeyCode
import javafx.scene.layout.Priority
import tornadofx.*

class MainView : View("Keywarden") {
    private val bw = Config.w * Config.k
    private val bh = Config.h * Config.k
    private val cw = 280.0

    private val mainController: MainController by inject()

    init {
        val (colls, err) = Client.Collections.fetchCollections()
        if (err != null) {
            popNotify(scope, err, true)
        } else {
            mainController.collectionsProperty.set(colls.asObservable())
            if (mainController.collectionsProperty.value.isNotEmpty()) {
                mainController.selectedCollectionProperty.set(mainController.collectionsProperty.value[0])
            }
        }
    }

    override val root = hbox {
        style {
            backgroundColor += mainBackgroundColor
            textFill = whiteColor
        }

        textfield {
            maxHeight = 0.0
            prefHeight = 0.0
            minHeight = 0.0

            maxWidth = 0.0
            prefWidth = 0.0
            minWidth = 0.0

            requestFocus()
        }

        prefWidth = bw
        prefHeight = bh
        visibleProperty().onChange {
            currentWindow?.sizeToScene()
        }
        val passwordsTableFragment = PasswordsTableFragment()

        setOnKeyPressed {
            if (it.code == KeyCode.DELETE) {
                if (mainController.selectedItemProperty.value == null)
                    return@setOnKeyPressed

                Client.Passwords.deletePassword(mainController.selectedItemProperty.value.id,
                    mainController.selectedCollectionProperty.value)
                passwordsTableFragment.fetchAndUpdatePasswords()
            }
        }

        vbox {
            toFront()
            style {
                backgroundColor += secondForegroundColor
            }

            minWidth = cw
            prefHeight = Config.h
            this += SearchFragment()

            label {
                text = "Collections"
                style {
                    textFill = whiteColor
                    fontSize = 14.pt
                    paddingLeft = 15.0
                }
            }
            this += CollectionsListFragment(passwordsTableFragment::updatePasswords)
        }

        vbox {
            prefWidth = Config.w - cw
            gridpaneConstraints {
                vgrow = Priority.ALWAYS
                hgrow = Priority.ALWAYS
            }
            this += ActionBar(passwordsTableFragment::fetchAndUpdatePasswords)
            this += passwordsTableFragment
            // TODO "region" for notifications
        }
    }
}