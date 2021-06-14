package fragment

import controller.Client
import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import javafx.scene.layout.Priority
import javafx.stage.StageStyle
import model.Password
import model.placeholder
import tornadofx.*

class PasswordsTableFragment(collections: SimpleListProperty<String>) : Fragment() {
    private val passwords = FXCollections.observableArrayList<Password>()
    init {
        var coll = ""
        if (collections.isNotEmpty()) {
            coll = collections[0]
            val (passes, err) = Client.fetchPasswords(coll)
            if (err != null) {
                find<PopUpFragment>(mapOf(PopUpFragment::text to err, PopUpFragment::warning to true)).openModal(
                    stageStyle = StageStyle.UNDECORATED
                )
            } else {
                passwords.setAll(passes)
            }
        }

    }

    @OptIn(ExperimentalStdlibApi::class)
    override val root = tableview(passwords) {
        hgrow = Priority.ALWAYS
        vgrow = Priority.ALWAYS
        enableCellEditing()

        column("Title", Password::titleProperty).makeEditable()
        column("Login", Password::loginProperty).apply {
            makeEditable()
            setOnEditStart {
                updateProperty(it.tablePosition.row, text.lowercase())
            }
            setOnEditCommit {
                replacePlaceholder(it.tablePosition.row, text.lowercase())
            }
            setOnEditCancel {
                replacePlaceholder(it.tablePosition.row, text.lowercase())
            }
        }
        column("Password", Password::passwordProperty).apply {
            makeEditable()
            setOnEditStart {
                updateProperty(it.tablePosition.row, text.lowercase())
            }
            setOnEditCommit {
                replacePlaceholder(it.tablePosition.row, text.lowercase())
            }
            setOnEditCancel {
                replacePlaceholder(it.tablePosition.row, text.lowercase())
            }
        }
        column("Email", Password::emailProperty).apply {
            makeEditable()
            setOnEditStart {
                updateProperty(it.tablePosition.row, text.lowercase())
            }
            setOnEditCommit {
                replacePlaceholder(it.tablePosition.row, text.lowercase())
            }
            setOnEditCancel {
                replacePlaceholder(it.tablePosition.row, text.lowercase())
            }
        }

        smartResize()
    }

    fun updateProperty(row: Int, property: String) {
        val passwordObj = passwords[row]
        val (login, email, password) = Client.getPassword(passwordObj.id)

        if (property == "login") {
            passwordObj.loginProperty.set(login)
        } else if (property == "email") {
            passwordObj.emailProperty.set(email)
        } else {
            passwordObj.passwordProperty.set(password)
        }
    }

    fun replacePlaceholder(row: Int, property: String) {
        val passwordObj = passwords[row]

        if (property == "login") {
            passwordObj.loginProperty.set(placeholder)
        } else if (property == "email") {
            passwordObj.emailProperty.set(placeholder)
        } else {
            passwordObj.passwordProperty.set(placeholder)
        }
    }

    fun updatePasswords(newPasswords: List<Password>) {
        // uses for updating password list when changing collection in CollectionListFragment
        passwords.setAll(newPasswords.asObservable())
        root.refresh()
    }
}
