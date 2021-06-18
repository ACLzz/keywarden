package controller

import model.Password
import model.placeholder
import java.lang.Exception

class PasswordsModule : ClientModule() {
    fun fetchPasswords(collection: String): Pair<List<Password>, String?> {
        if (collection == "") {
            return Pair(listOf(), null)
        }

        val result = super.fetchRequest("collection/$collection", "GET", null)

        return try {
            val err = result.obj().get("error")
            Pair(listOf(), err.toString())
        }catch (e: Exception) {
            var _collections: List<HashMap<String, Any>>
            try {
                _collections = result.array().toList() as List<HashMap<String, Any>>
            } catch (e: Exception) {
                _collections = listOf()
            }

            var collections: List<Password> = listOf()
            for (coll in _collections) {
                val password = Password(
                    coll["title"] as String, placeholder, placeholder, placeholder, coll["id"] as Int
                )
                collections = collections + password
            }
            Pair(collections, null)
        }
    }

    fun getPassword(id: Int, collection: String) : Pair<List<String>, String?> {
        val result = super.fetchRequest("collection/$collection/$id", "GET", null)

        return try {
            val err = result.obj().get("error")
            Pair(listOf(), err.toString())
        }catch (e: Exception) {
            val _data = result.obj()
            val data = listOf(_data.getString("login"), _data.getString("email"), _data.getString("password"))

            Pair(data, null)
        }
    }
}