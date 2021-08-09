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

    private val passwordsTableFragment = PasswordsTableFragment()
    private val collectionsList = CollectionsListFragment(passwordsTableFragment::updatePasswords)

    private val mainRoot = hbox {
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

        mainController.fetchAndUpdatePasswords = passwordsTableFragment::fetchAndUpdatePasswords

        setOnKeyPressed {
            if (it.code == KeyCode.DELETE) {
                if (mainController.selectedPasswordProperty.value == null)
                    return@setOnKeyPressed

                mainController.deleteSelectedPassword()
            }
        }

        vbox {
            toFront()
            style {
                backgroundColor += secondForegroundColor
            }

            minWidth = cw
            prefHeight = Config.h
            this += SearchFragment(passwordsTableFragment::updatePasswords)

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
                        mainController.popPrompt("How would you call your new collection?", arrayOf("Create", "Don't create"), true
                        ) { choice, title ->
                            if (choice == "Create") {
                                val err = Client.Collections.createCollection(title)
                                if (err != null) {
                                    mainController.popNotify(err, true)
                                    return@popPrompt
                                }

                                collectionsList.fetchAndUpdateCollections()
                            }
                        }
                    }
                }

                this += ActionButton(null, TrashIcon()).root.apply {
                    setOnMouseClicked {
                        if (mainController.selectedCollectionProperty.value == null)
                            return@setOnMouseClicked

                        mainController.popPrompt(
                            "Do you really want to delete ${mainController.selectedCollectionProperty.value} collection?",
                            arrayOf("Yes", "No"), false
                        ) { choice, _ ->
                            if (choice == "Yes") {
                                Client.Collections.deleteCollection(mainController.selectedCollectionProperty.value)
                                mainController.selectedCollectionProperty.set(collectionsList.getNextItem())
                                collectionsList.fetchAndUpdateCollections()
                                passwordsTableFragment.fetchAndUpdatePasswords()
                            }
                        }
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
            this += ActionBar()
            this += passwordsTableFragment
        }
    }

    override val root = stackpane {
        this += mainRoot

        mainController.popNotify = { text, warning ->
            this += mainController.buildNotify(text, warning)
        }
    }
}