package fragment

import app.Config
import app.main
import app.secondForegroundColor
import app.whiteColor
import controller.Client
import controller.MainController
import javafx.scene.control.cell.TextFieldListCell
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

        selectedItem.apply {
            setOnMouseClicked {
                if (selectedItem == null || selectedItem.toString().isEmpty()) {
                    return@setOnMouseClicked
                }

                val (passwords, err) = Client.Collections.fetchPasswords(selectedItem.toString())
                if (err != null) {
                    mainController.popNotify(err, true)
                } else {
                    updatePasswords(passwords)
                    mainController.selectedCollectionProperty.set(selectedItem.toString())
                }
            }

            setOnEditCommit {
                if (items[it.index] == it.newValue) {
                    return@setOnEditCommit
                }

                runAsync {
                    Client.Collections.updateCollection(selectedItem.toString(), it.newValue)
                } ui { err ->
                    if (err != null) {
                        mainController.popNotify(err, true)
                    } else {
                        val i = mainController.collectionsProperty.value.toMutableList()
                        i[i.indexOf(selectedItem.toString())] = it.newValue
                        i.sort()
                        mainController.collectionsProperty.set(i.asObservable())
                        items = mainController.collectionsProperty
                        refresh()
                    }
                }
            }

            placeholder = label("No collections added yet").apply { style { textFill = whiteColor } }
        }

        isEditable = true
        setCellFactory(TextFieldListCell.forListView())
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
            if (list.items.size <= 1) {
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