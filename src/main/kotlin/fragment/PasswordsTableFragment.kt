package fragment

import controller.MainController
import javafx.scene.layout.Priority
import model.Password
import tornadofx.*

class PasswordsTableFragment : Fragment() {
    private val controller: MainController by inject()
    override val root = tableview(controller.getPasswords("").asObservable()) {
        hgrow = Priority.ALWAYS
        vgrow = Priority.ALWAYS
        column("Title", Password::title).makeEditable()
        column("Login", Password::login).makeEditable()
        column("Password", Password::password).makeEditable()
        column("Email", Password::email).makeEditable()
        smartResize()
    }
}