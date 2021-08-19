package controller

import java.lang.Exception

val randomPlaceholder: String get() {
    val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
    return (1..(15..30).random())
        .map { charset.random() }
        .joinToString("")
}

class PasswordsModule : ClientModule() {
    fun createPassword(collection: String, title: String,
                       login: String = randomPlaceholder, password: String = randomPlaceholder,
                       email: String = randomPlaceholder) : String? {
        val result = super.fetchRequest("collection/${Client.encryptIt(collection).toBase58()}", "POST", listOf(
            Pair("title", Client.encryptIt(title)),
            Pair("login", Client.encryptIt(login)),
            Pair("password", Client.encryptIt(password)),
            Pair("email", Client.encryptIt(email))
        ))

        return try {
            val err = result.obj().get("error")
            err.toString()
        }catch (e: Exception) {
            null
        }
    }

    fun getPassword(id: Int, collection: String) : Pair<List<String>, String?> {
        val result = super.fetchRequest("collection/${Client.encryptIt(collection).toBase58()}/$id", "GET", null)

        return try {
            val err = result.obj().get("error")
            Pair(listOf(), err.toString())
        }catch (e: Exception) {
            val _data = listOf(result.obj().getString("title"), result.obj().getString("login"),
                result.obj().getString("email"), result.obj().getString("password"))

            val data = arrayListOf<String>()
            _data.forEach {
                data.add(Client.decryptIt(it))
            }

            Pair(data, null)
        }
    }

    fun updatePassword(id: Int, collection: String,
                       title: String? = null, login: String? = null,
                       password: String? = null, email: String? = null, coll: String? = null): String? {
        val data = arrayListOf<Pair<String, String>>()

        when {
            title != null -> data.add(Pair("title", Client.encryptIt(title)))
            login != null -> data.add(Pair("login", Client.encryptIt(login)))
            password != null -> data.add(Pair("password", Client.encryptIt(password)))
            email != null -> data.add(Pair("email", Client.encryptIt(email)))
            coll != null -> data.add(Pair("collection", Client.encryptIt(coll).toBase58()))
        }

        val result = super.fetchRequest("collection/${Client.encryptIt(collection).toBase58()}/$id", "PUT", data)

        return try {
            val err = result.obj().get("error")
            err.toString()
        }catch (e: Exception) {
            null
        }
    }

    fun deletePassword(id: Int, collection: String) : String? {
        val result = super.fetchRequest("collection/${Client.encryptIt(collection).toBase58()}/$id", "DELETE", null)

        return try {
            val err = result.obj().get("error")
            err.toString()
        }catch (e: Exception) {
            null
        }
    }
}