package view

import app.Config
import app.Styles
import controller.ClientController
import fragment.PopUpFragment
import javafx.geometry.HPos
import javafx.geometry.Pos
import javafx.stage.StageStyle
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
        style {
            alignment = Pos.CENTER
            backgroundColor += c("#363636")
        }

        hbox {
            style {
                alignment = Pos.CENTER
                paddingBottom = 15.0
            }
            vbox {
                style {
                    fontSize = 13.pt
                    paddingRight = 10.0
                    paddingTop = 5.0
                }

                label {
                    text = "Login"
                    paddingBottom = 10.0
                    textFill = c("#C7C7C7")
                }
                label {
                    text = "Password"
                    textFill = c("#C7C7C7")
                }
            }

            vbox {
                textfield {
                    minWidth = 150.0
                    setOnKeyTyped {
                        login = this.text
                    }
                }
                stackpane {
                    val _height = 10.0
                    minHeight = _height
                    maxHeight = _height
                }
                passwordfield {
                    minWidth = 150.0
                    setOnKeyTyped {
                        password = this.text
                    }
                }
            }
        }

        hbox {
            style {
                alignment = Pos.CENTER
            }

            button {
                text = "Login"
                addClass(Styles.button)

                setOnMouseClicked {
                    login()
                }
            }

            pane {
                minWidth = 10.0
                maxWidth = 10.0
            }

            button {
                text = "Register"
                addClass(Styles.button)

                setOnMouseClicked {
                    register()
                }
            }
        }
    }

    fun login() {
        val err = client.login(login, password)
        if (err == "") {
            replaceWith<MainView>(transition = ViewTransition.FadeThrough(1.5.seconds))
        } else {
            find<PopUpFragment>(mapOf(PopUpFragment::text to err, PopUpFragment::warning to true)).openModal(stageStyle = StageStyle.UNDECORATED)
        }
    }

    fun register() {
        val err = client.register(login, password)
        if (err == "") {
            find<PopUpFragment>(mapOf(PopUpFragment::text to "Registered", PopUpFragment::warning to false)).openModal(stageStyle = StageStyle.UNDECORATED)
        } else {
            find<PopUpFragment>(mapOf(PopUpFragment::text to err, PopUpFragment::warning to true)).openModal(stageStyle = StageStyle.UNDECORATED)
        }
    }
}