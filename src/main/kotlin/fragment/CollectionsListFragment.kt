package fragment

import app.Config
import app.Styles
import app.accentForegroundColor
import app.secondForegroundColor
import controller.Client
import controller.MainController
import javafx.beans.property.SimpleListProperty
import javafx.scene.control.cell.TextFieldListCell
import javafx.stage.StageStyle
import model.Password
import tornadofx.*
import java.awt.TextField
import kotlin.reflect.KFunction1

class CollectionsListFragment(updatePasswords: KFunction1<List<Password>, Unit>) : Fragment() {
    private val mainController: MainController by inject()
    override val root = vbox {
        style {
            backgroundColor += secondForegroundColor
            paddingTop = 10.0
        }

        listview(mainController.collectionsProperty.asObservable()) {
            style {
                backgroundColor += secondForegroundColor
            }
            prefHeight = Config.h
            selectedItem.apply {
                setOnMouseClicked {
                    if (selectedItem == null) {
                        return@setOnMouseClicked
                    }

                    val (passwords, err) = Client.Collections.fetchPasswords(selectedItem.toString())
                    if (err != null) {
                        popNotify(scope, err, true)
                    } else {
                        updatePasswords(passwords)
                        mainController.selectedCollectionProperty.set(selectedItem.toString())
                    }
                }
            }

            isEditable = true
            setCellFactory(TextFieldListCell.forListView())
        }
    }
}