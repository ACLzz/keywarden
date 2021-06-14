package controller

import app.Config
import tornadofx.*

class ClientController : Controller() {
    private val api: Rest by inject()
    init {
        api.baseURI = Config.url
    }

    fun register() {}
    fun login(login: String, password: String): Boolean {
        val data = with (JsonBuilder()) {
            add("login", login)
            add("password", password)
            build()
        }

        val response = api.post("auth/login", data)
        return if (response.statusCode in 201..299) {
            val token = response.one()["token"]
            println("Token is: $token") // FIXME delete this line
            true
        } else {
            val err = response.one()["error"]
            println("Error is: $err")   // TODO make pop-up
            false
        }
    }
}