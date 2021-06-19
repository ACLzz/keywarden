package fragment

import app.whiteColor
import controller.Client
import controller.MainController
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.scene.control.TableColumn
import javafx.scene.effect.BlurType
import javafx.scene.effect.InnerShadow
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
        fetchAndUpdatePasswords(false)
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
            mainController.selectedItemProperty.set(it)
        }

        fun buildColumn(title: String, prop: KProperty1<Password, ObservableValue<String>>, updateArg: String,
                        weight: Int, placeholder: Boolean = true): TableColumn<Password, String> {
            return column(title, prop).apply {
                makeEditable()
                setOnEditCommit {
                    val err = when (updateArg.lowercase()) {
                        "title" -> Client.Passwords.updatePassword(it.rowValue.id, mainController.selectedCollectionProperty.value, title=it.newValue)
                        "login" -> Client.Passwords.updatePassword(it.rowValue.id, mainController.selectedCollectionProperty.value, login=it.newValue)
                        "password" -> Client.Passwords.updatePassword(it.rowValue.id, mainController.selectedCollectionProperty.value, password=it.newValue)
                        else -> Client.Passwords.updatePassword(it.rowValue.id, mainController.selectedCollectionProperty.value, email=it.newValue)
                    }
                    if (err != null) {
                        popNotify(scope, err, true)
                    }

                    if (placeholder)
                        replacePlaceholder(it.tablePosition.row, text.lowercase())
                }

                setOnEditStart {
                    editingRow = it.tablePosition.row
                    if (placeholder) {
                        updateProperty(it.tablePosition.row, text.lowercase())
                    }
                }

                if (placeholder) {
                    setOnEditCancel { replacePlaceholder(editingRow, text.lowercase()) }
                }
                weightedWidth(weight)
            }
        }

        buildColumn("Title", Password::titleProperty, "title", 2, placeholder = false)
        buildColumn("Login", Password::loginProperty, "login", 2)
        buildColumn("Password", Password::passwordProperty, "password", 3)
        buildColumn("Email", Password::emailProperty, "email", 4)

        placeholder = label("No passwords in that collection")

        smartResize()
    }

    fun updateProperty(row: Int, property: String) {
        val passwordObj = passwords[row]

        val (data, err) = Client.Passwords.getPassword(passwordObj.id, mainController.selectedCollectionProperty.value)
        if (err != null) {
            popNotify(scope, err, true)
            return
        }
        val (login, email, password) = data

        when(property) {
            "login" -> passwordObj.loginProperty.set(login)
            "email" -> passwordObj.emailProperty.set(email)
            else -> passwordObj.passwordProperty.set(password)
        }
    }

    fun replacePlaceholder(row: Int, property: String) {
        if (passwords.size < row + 1) {
            return
        }
        runAsync {
            Thread.sleep(1_000)
            val passwordObj = passwords[row]
            when(property) {
                "login" -> passwordObj.loginProperty.set(placeholder)
                "email" -> passwordObj.emailProperty.set(placeholder)
                else -> passwordObj.passwordProperty.set(placeholder)
            }
        }
    }

    fun updatePasswords(newPasswords: List<Password>) {
        // uses for updating password list when changing collection in CollectionListFragment
        passwords.setAll(newPasswords.asObservable())
        root.refresh()
    }

    fun fetchAndUpdatePasswords(rootInitialized: Boolean = true) {
        if (mainController.selectedCollectionProperty.value != null && mainController.selectedCollectionProperty.value.isNotEmpty()) {
            val (passes, err) = Client.Collections.fetchPasswords(mainController.selectedCollectionProperty.value)
            if (err != null) {
                popNotify(scope, err, true)
            } else {
                if (rootInitialized)
                    updatePasswords(passes)
                else
                    passwords.setAll(passes)
            }
        }
    }
}
