package controller

import java.lang.Exception

class AuthModule : ClientModule() {
    fun register(login: String, password: String): String? {
        val result = super.fetchRequest(
            "auth/", "POST",
            listOf(Pair("login", login), Pair("password", password.hashMe("SHA-512")))
        )

        return try {
            val err = result.obj().get("error")
            err.toString()
        }catch (e: Exception) {
            null
        }
    }

    fun login(login: String, password: String): String? {
        val result = super.fetchRequest(
            "auth/login", "POST",
            listOf(Pair("login", login), Pair("password", password.hashMe("SHA-512")))
        )

        return try {
            val err = result.obj().get("error")
            err.toString()
        }catch (e: Exception) {
            try {
                Client.settoken(result.obj().get("token").toString())
                Client.setpassword(password)
                null
            } catch (e: Exception) {
                "invalid credentials"
            }
        }
    }

    fun readUser(): Pair<String?, String?> {
        val result = super.fetchRequest("auth", "GET", null)

        return try {
            val err = result.obj().get("error")
            Pair(null, err.toString())
        } catch (e: Exception) {
            Pair(result.obj().get("username").toString(), null)
        }
    }

    fun deleteUser() : String? {
        val result = super.fetchRequest("auth", "DELETE", null)

        return try {
            val err = result.obj().get("error")
            err.toString()
        } catch (e: Exception) {
            null
        }
    }

    fun updateUser(login: String?, password: String?): String? {
        val data = mutableListOf<Pair<String, String>>()
        login?.let {
            data.add(Pair("login", login))
        }
        password?.let {
            data.add(Pair("password", password.hashMe("SHA-512")))
        }
        val result = super.fetchRequest("auth/", "PUT", data)

        return try {
            val err = result.obj().get("error")
            err.toString()
        }catch (e: Exception) {
            null
        }
    }

    fun logout() {
        super.fetchRequest("auth/logout", "POST", null)
        Client.settoken("")
        Client.setpassword("")
    }
}