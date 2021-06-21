package view

import app.*
import controller.Client
import controller.MainController
import fragment.*
import javafx.scene.input.KeyCode
import javafx.scene.layout.Priority
import tornadofx.*

class MainView : View("Keywarden") {
    private val bw = Config.w * Config.k
    private val bh = Config.h * Config.k
    private val cw = 280.0

    private val mainController: MainController by inject()

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
        val collectionsList = CollectionsListFragment(passwordsTableFragment::updatePasswords)

        setOnKeyPressed {
            if (it.code == KeyCode.DELETE) {
                if (mainController.selectedPasswordProperty.value == null)
                    return@setOnKeyPressed

                Client.Passwords.deletePassword(mainController.selectedPasswordProperty.value.id,
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

            hbox {
                style {
                    paddingBottom = 10.0
                }
                label {
                    text = "Collections"
                    style {
                        textFill = whiteColor
                        fontSize = 14.pt
                        paddingLeft = 15.0
                    }
                }

                region {
                    hgrow = Priority.ALWAYS
                }

                this += ActionButton(null, PlusIcon(), 10.0).root.apply {
                    setOnMouseClicked {
                        val defaultCollectionName = "Untitled"
                        var newCollectionName = ""
                        for (i in 1..1000) {
                            newCollectionName = defaultCollectionName + i.toString()
                            if (newCollectionName in mainController.collectionsProperty.toList()) {
                                continue
                            }
                            break
                        }

                        Client.Collections.createCollection(newCollectionName)
                        collectionsList.fetchAndUpdateCollections()
                    }
                }
                this += ActionButton(null, TrashIcon()).root.apply {
                    setOnMouseClicked {
                        if (mainController.selectedCollectionProperty.value == null)
                            return@setOnMouseClicked

                        Client.Collections.deleteCollection(mainController.selectedCollectionProperty.value)
                        mainController.selectedCollectionProperty.set(collectionsList.getNextItem())
                        collectionsList.fetchAndUpdateCollections()
                        passwordsTableFragment.fetchAndUpdatePasswords()
                    }
                }
            }
            this += collectionsList
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