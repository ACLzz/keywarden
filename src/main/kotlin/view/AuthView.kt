package view

import app.Config
import app.Styles
import app.whiteColor
import controller.Client
import controller.MainController
import fragment.PopUpFragment
import javafx.geometry.HPos
import javafx.geometry.Pos
import javafx.scene.input.KeyCode
import javafx.stage.StageStyle
import tornadofx.*

class AuthView : View("Auth") {
    private val bw = Config.w * Config.k
    private val bh = Config.h * Config.k
    private var login = ""
    private var password = ""
    private lateinit var pressLogin: () -> Unit
    private val mainController: MainController by inject()

    private val saveLogin = checkbox {
        text = "remember login"
        isSelected = Config.saveUsername
        style {
            textFill = whiteColor
        }
    }

    private val loginField = textfield {
        if (Config.saveUsername) {
            text = Config.savedUsername
            login = text
        }
        minWidth = 150.0
        setOnKeyTyped {
            login = this.text
        }

        setOnKeyPressed {
            if(it.code.equals(KeyCode.ENTER)){
                passField.requestFocus()
            }
        }
    }

    private val passField = passwordfield {
        minWidth = 150.0
        if (Config.saveUsername) {
            whenDocked { requestFocus() }
        }

        setOnKeyTyped {
            password = this.text
        }
        setOnKeyPressed {
            if(it.code.equals(KeyCode.ENTER)){
                pressLogin()
            }
        }
    }

    private val mainRoot = vbox {
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
                style {
                    alignment = Pos.CENTER
                }
                this += loginField
                region {
                    val h = 10.0
                    minHeight = h
                    maxHeight = h
                }
                this += passField
            }
        }

        vbox {
            style {
                alignment = Pos.CENTER
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

                    pressLogin = { login() }
                }

                region {
                    val w = 20.0
                    minWidth = w
                    maxWidth = w
                }

                this += saveLogin
            }

            region {
                val h = 10.0
                minHeight = h
                maxHeight = h
            }

            button {
                text = "Register"
                addClass(Styles.button)
                style {
                    alignment = Pos.CENTER
                }

                setOnMouseClicked {
                    register()
                }
            }
        }
    }

    override val root = stackpane {
        this += mainRoot

        mainController.popNotify = { text, warning ->
            this += mainController.buildNotify(text, warning)
        }
    }

    fun login() {
        Client.Auth.login(login, password)?.let {
            mainController.popNotify(it, true)
            return
        }

        if (saveLogin.isSelected) {
            Config.savedUsername = login
        }
        Config.saveUsername = saveLogin.isSelected
        Config.saveConfig()

        mainController.initCollections()
        mainController.getUsername()
        replaceWith<MainView>(transition = ViewTransition.FadeThrough(1.5.seconds))
    }

    fun register() {
        Client.Auth.register(login, password)?.let {
            mainController.popNotify(it, true)
            return
        }
        mainController.popNotify("Registered", false)
    }
}