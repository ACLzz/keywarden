package controller

import java.lang.Exception

class AuthModule : ClientModule() {
    fun register(login: String, password: String): String {
        val result = super.fetchRequest(
            "auth/", "POST",
            listOf(Pair("login", login), Pair("password", password))
        )

        return try {
            val err = result.obj().get("error")
            err.toString()
        }catch (e: Exception) {
            ""
        }
    }

    fun login(login: String, password: String): String {
        val result = super.fetchRequest(
            "auth/login", "POST",
            listOf(Pair("login", login), Pair("password", password))
        )

        return try {
            val err = result.obj().get("error")
            err.toString()
        }catch (e: Exception) {
            try {
                Client.settoken(result.obj().get("token").toString())
                ""
            } catch (e: Exception) {
                "invalid credentials"
            }
        }
    }
}