package fragment

import app.Config
import app.secondForegroundColor
import app.whiteColor
import controller.Client
import controller.MainController
import javafx.scene.control.cell.TextFieldListCell
import javafx.scene.input.KeyCode
import javafx.scene.input.TransferMode
import javafx.scene.layout.Priority
import model.Password
import tornadofx.*
import kotlin.reflect.KFunction1

class CollectionsListFragment(updatePasswords: KFunction1<List<Password>, Unit>) : Fragment() {
    private val mainController: MainController by inject()

    val list = listview(mainController.collectionsProperty.asObservable()) {
        style {
            backgroundColor += secondForegroundColor
        }
        prefHeight = Config.h
        placeholder = label("No collections added yet").apply { style { textFill = whiteColor } }

        isEditable = true
        cellFactory = TextFieldListCell.forListView()

        cellFormat { t ->
            fun buildDefaultGraphic(text: String?) = label {
                this.text = text
                style {
                    textFill = whiteColor
                }
            }

            graphic = buildDefaultGraphic(t)

            onDoubleClick {
                graphic = textfield {
                    text = t
                    requestFocus()

                    setOnKeyPressed {
                        if (it.code != KeyCode.ENTER) {
                            return@setOnKeyPressed
                        }

                        if( t == text) {
                            graphic = buildDefaultGraphic(text)
                            return@setOnKeyPressed
                        }

                        runAsync {
                            Client.Collections.updateCollection(t, text)
                        } ui { err ->
                            var newText = t
                            if (err != null) {
                                mainController.popNotify(err, true)
                            } else {
                                val i = mainController.collectionsProperty.value.toMutableList()
                                i[i.indexOf(t)] = text
                                i.sort()
                                mainController.collectionsProperty.set(i.asObservable())
                                items = mainController.collectionsProperty
                                refresh()

                                mainController.selectedCollectionProperty.set(text)
                                newText = text
                            }

                            graphic = buildDefaultGraphic(newText)
                        }
                    }
                }
            }

            setOnDragDropped {
                var success = false
                if (it.dragboard.hasString() && t != mainController.selectedCollectionProperty.value) {
                    success = true
                    val pId = it.dragboard.string.toInt()

                    runAsync { Client.Passwords.updatePassword(
                        pId,
                        mainController.selectedCollectionProperty.value,
                        coll=t
                    )}.ui { err ->
                        if (err != null) {
                            mainController.popNotify(err, true)
                        } else {
                            mainController.fetchAndUpdatePasswords()
                        }
                    }
                }

                it.isDropCompleted = success
                it.consume()
            }

            setOnDragEntered {
                graphic = buildDefaultGraphic(t).apply {
                    style(append=true) {
                        textFill = c("#ffffff")
                    }
                }
            }

            setOnDragExited {
                graphic = buildDefaultGraphic(t)
            }

            setOnDragOver {
                if (it.dragboard.hasString()) {
                    it.acceptTransferModes(TransferMode.MOVE)
                }
                it.consume()
            }

            setOnMouseClicked {
                if (t.isEmpty()) {
                    return@setOnMouseClicked
                }

                val (passwords, err) = Client.Collections.fetchPasswords(t)
                if (err != null) {
                    mainController.popNotify(err, true)
                } else {
                    updatePasswords(passwords)
                    mainController.selectedCollectionProperty.set(t)
                }
                it.consume()
            }
        }
    }

    override val root = vbox {
        style {
            backgroundColor += secondForegroundColor
            vgrow = Priority.ALWAYS
        }

        this += list
    }

    fun getNextItem() : String? {
        return if (list.selectedItem != null) {
            val idx = list.items.indexOf(mainController.selectedCollectionProperty.valueSafe)+1
            if (list.items.size <= idx) {
                null
            } else
                list.items[idx]
        } else {
            null
        }
    }

    fun fetchAndUpdateCollections() {
        runAsync {
            Client.Collections.fetchCollections()
        } ui {
            val (colls, err) = it
            if (err != null) {
                mainController.popNotify(err, true)
            } else {
                val isEmptyItems = mainController.collectionsProperty.isEmpty()
                colls.sort()
                mainController.collectionsProperty.set(colls.asObservable())
                if (isEmptyItems)
                    list.items = mainController.collectionsProperty
                list.refresh()
            }
        }
    }
}