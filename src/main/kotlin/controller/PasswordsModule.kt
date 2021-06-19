package controller

import model.placeholder
import java.lang.Exception

class PasswordsModule : ClientModule() {
    fun createPassword(collection: String, title: String, login: String = placeholder, password: String = placeholder, email: String = placeholder) : String? {
        val result = super.fetchRequest("collection/$collection", "POST", listOf(
            Pair("title", title),
            Pair("login", login),
            Pair("password", password),
            Pair("email", email)
        ))

        return try {
            val err = result.obj().get("error")
            err.toString()
        }catch (e: Exception) {
            null
        }
    }

    fun getPassword(id: Int, collection: String) : Pair<List<String>, String?> {
        val result = super.fetchRequest("collection/$collection/$id", "GET", null)

        return try {
            val err = result.obj().get("error")
            Pair(listOf(), err.toString())
        }catch (e: Exception) {
            val _data = result.obj()
            val data = listOf(_data.getString("title"), _data.getString("login"), _data.getString("email"), _data.getString("password"))

            Pair(data, null)
        }
    }

    fun updatePassword(id: Int, collection: String,
                       title: String? = null, login: String? = null,
                       password: String? = null, email: String? = null): String? {
        var data = listOf<Pair<String, String>>()

        when {
            title != null -> {
                data = data + Pair("title", title)
            }
            login != null -> {
                data = data + Pair("login", login)
            }
            password != null -> {
                data = data + Pair("password", password)
            }
            email != null -> {
                data = data + Pair("email", email)
            }
        }

        val result = super.fetchRequest("collection/$collection/$id", "PUT", data)

        return try {
            val err = result.obj().get("error")
            err.toString()
        }catch (e: Exception) {
            null
        }
    }

    fun deletePassword(id: Int, collection: String) : String? {
        val result = super.fetchRequest("collection/$collection/$id", "DELETE", null)

        return try {
            val err = result.obj().get("error")
            err.toString()
        }catch (e: Exception) {
            null
        }
    }
}