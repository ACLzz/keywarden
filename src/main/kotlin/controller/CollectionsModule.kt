package controller

import model.Password
import model.placeholder
import java.lang.Exception

class CollectionsModule : ClientModule() {
    fun fetchCollections() : Pair<MutableList<String>, String?> {
        val result = super.fetchRequest("collection", "GET", null)

        return try {
            val err = result.obj().get("error")
            Pair(mutableListOf(), err.toString())
        }catch (e: Exception) {
            var _collections: MutableList<String>
            try {
                _collections = result.array().toMutableList() as MutableList<String>
            } catch (e: Exception) {
                _collections = mutableListOf()
            }

            val collections: MutableList<String> = mutableListOf()
            _collections.forEach {
                collections.add(Client.decryptIt(it.fromBase58()))
            }
            Pair(collections, null)
        }
    }

    fun fetchPasswords(collection: String): Pair<List<Password>, String?> {
        if (collection == "") {
            return Pair(listOf(), null)
        }

        val result = super.fetchRequest("collection/${Client.encryptIt(collection).toBase58()}", "GET", null)

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
                    Client.decryptIt(coll["title"] as String), collection, placeholder, placeholder, placeholder, coll["id"] as Int
                )
                collections = collections + password
            }
            Pair(collections, null)
        }
    }

    fun createCollection(title: String) : String? {
        val result = super.fetchRequest("collection", "POST", listOf(
            Pair("title", Client.encryptIt(title).toBase58()),
        ))

        return try {
            val err = result.obj().get("error")
            err.toString()
        }catch (e: Exception) {
            null
        }
    }

    fun updateCollection(collection: String, title: String? = null): String? {
        var data = listOf<Pair<String, String>>()

        when {
            title != null -> {
                data = data + Pair("title", Client.encryptIt(title).toBase58())
            }
        }

        val result = super.fetchRequest("collection/${Client.encryptIt(collection).toBase58()}", "PUT", data)

        return try {
            val err = result.obj().get("error")
            err.toString()
        }catch (e: Exception) {
            null
        }
    }

    fun deleteCollection(collection: String) : String? {
        val result = super.fetchRequest("collection/${Client.encryptIt(collection).toBase58()}", "DELETE", null)

        return try {
            val err = result.obj().get("error")
            err.toString()
        }catch (e: Exception) {
            null
        }
    }
}