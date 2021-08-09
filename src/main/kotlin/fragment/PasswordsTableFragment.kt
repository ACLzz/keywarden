package fragment

import app.whiteColor
import controller.Client
import controller.MainController
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.scene.input.ClipboardContent
import javafx.scene.input.MouseButton
import javafx.scene.input.TransferMode
import javafx.scene.layout.Border
import javafx.scene.layout.Priority
import model.Password
import model.placeholder
import tornadofx.*
import kotlin.reflect.KProperty1

class PasswordsTableFragment : Fragment() {
    private val passwords = FXCollections.observableArrayList<Password>()
    private val mainController: MainController by inject()
    private var editingRow = 0

    init {
        fetchAndUpdatePasswords()
    }

    @OptIn(ExperimentalStdlibApi::class)
    override val root = tableview(passwords) {
        hgrow = Priority.ALWAYS
        vgrow = Priority.ALWAYS
        enableCellEditing()
        style {
            this.textFill = whiteColor
        }
        border = Border.EMPTY

        onSelectionChange {
            mainController.selectedPasswordProperty.set(it)
        }

        fun buildColumn(columnTitle: String, prop: KProperty1<Password, ObservableValue<String>>,
                        weight: Int, placeholder: Boolean = true) = column(columnTitle, prop).apply {
            makeEditable()

            setOnEditCommit {
                val rowId = it.tablePosition.row

                runAsync {
                    when (columnTitle.lowercase()) {
                        "title" -> Client.Passwords.updatePassword(it.rowValue.id, mainController.selectedCollectionProperty.value, title=it.newValue)
                        "login" -> Client.Passwords.updatePassword(it.rowValue.id, mainController.selectedCollectionProperty.value, login=it.newValue)
                        "password" -> Client.Passwords.updatePassword(it.rowValue.id, mainController.selectedCollectionProperty.value, password=it.newValue)
                        else -> Client.Passwords.updatePassword(it.rowValue.id, mainController.selectedCollectionProperty.value, email=it.newValue)
                    }
                } ui { err ->
                    if (err != null) {
                        mainController.popNotify(err, true)
                    }

                    if (columnTitle.lowercase() == "title") {
                        updateProperty(rowId, text.lowercase())
                        this@tableview.sort()
                    }
                }

                if (placeholder)
                    replacePlaceholder(it.tablePosition.row, text.lowercase())
            }

            setOnEditStart {
                editingRow = it.tablePosition.row
                updateProperty(it.tablePosition.row, text.lowercase())
            }

            if (placeholder) {
                setOnEditCancel { replacePlaceholder(editingRow, text.lowercase()) }
            }
            weightedWidth(weight)
        }

        selectedCell.apply {
            setOnMouseClicked {
                if (it.button == MouseButton.SECONDARY && selectedCell != null) {
                    val (resp, err) = Client.Passwords.getPassword(passwords[selectedCell!!.row].id, mainController.selectedCollectionProperty.valueSafe)
                    err?.let {
                        mainController.popNotify(err, true)
                        return@setOnMouseClicked
                    }

                    val (titl, login, email, password) = resp
                    val data = ClipboardContent()
                    data.putString(when (columns[selectedCell!!.column].text.lowercase()) {
                        "login" -> login
                        "password" -> password
                        "email" -> email
                        else -> titl
                    })
                    clipboard.setContent(data)
                }

                setOnDragDetected {
                    val db = this@tableview.startDragAndDrop(*TransferMode.ANY)
                    val cp = ClipboardContent()
                    val id = passwords[selectedCell!!.row].id.toString()
                    cp.putString(id)
                    db.setContent(cp)
                }
            }
        }

        buildColumn("Title", Password::titleProperty,  2, placeholder = false).apply {
            sortOrder.add(this)
            isSortable = true
        }
        buildColumn("Login", Password::loginProperty,  2)
        buildColumn("Password", Password::passwordProperty, 3)
        buildColumn("Email", Password::emailProperty, 4)

        placeholder = label("No passwords in that collection")

        smartResize()
    }

    fun updateProperty(row: Int, property: String) {
        val passwordObj = passwords[row]

        val (data, err) = Client.Passwords.getPassword(passwordObj.id, mainController.selectedCollectionProperty.value)
        if (err != null) {
            mainController.popNotify(err, true)
            return
        }
        val (title, login, email, password) = data

        when(property) {
            "title" -> passwordObj.titleProperty.set(title)
            "login" -> passwordObj.loginProperty.set(login)
            "email" -> passwordObj.emailProperty.set(email)
            else -> passwordObj.passwordProperty.set(password)
        }
    }

    fun replacePlaceholder(row: Int, property: String) {
        if (passwords.size < row + 1) {
            return
        }
        val passwordObj = passwords[row]
        when(property) {
            "login" -> passwordObj.loginProperty.set(placeholder)
            "email" -> passwordObj.emailProperty.set(placeholder)
            else -> passwordObj.passwordProperty.set(placeholder)
        }
        root.refresh()
    }

    fun updatePasswords(newPasswords: List<Password>) {
        // uses for updating password list when changing collection in CollectionListFragment
        if (newPasswords.isEmpty())
            passwords.clear()
        else {
            passwords.setAll(newPasswords.asObservable())
            root.refresh()
            root.sort()
        }
    }

    fun fetchAndUpdatePasswords() {
        if (mainController.selectedCollectionProperty.value != null) {
            runAsync {
                Client.Collections.fetchPasswords(mainController.selectedCollectionProperty.value)
            } ui {
                val (passes, err) = it
                if (err != null) {
                    mainController.popNotify(err, true)
                } else {
                    updatePasswords(passes)
                }
            }
        } else {
            updatePasswords(listOf())
        }
    }
}
