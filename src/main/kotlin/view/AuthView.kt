package view

import app.Config
import controller.ClientController
import tornadofx.*

class AuthView : View("Auth") {
    private val bw = Config.w * Config.k
    private val bh = Config.h * Config.k
    private var login = ""
    private var password = ""
    private val client: ClientController by inject()

    override val root = vbox {
        prefWidth = bw
        prefHeight = bh

        hbox {
            vbox {
                label("Login")
                label("Password")
            }

            vbox {
                textfield {
                    minWidth = 150.0
                    setOnKeyTyped {
                        login = this.text
                    }
                }
                passwordfield {
                    minWidth = 150.0
                    setOnKeyTyped {
                        password = this.text
                    }
                }
            }
        }
        button {
            text = "Login"
            setOnMouseClicked {
                login()
            }
        }
    }

    fun login() {
        if (client.login(login, password)) {
            replaceWith<MainView>(transition = ViewTransition.FadeThrough(1.5.seconds))
        }
    }
}