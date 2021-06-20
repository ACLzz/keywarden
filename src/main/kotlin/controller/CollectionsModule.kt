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
            collections.addAll(_collections)
            Pair(collections, null)
        }
    }

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

    fun createCollection(title: String) : String? {
        val result = super.fetchRequest("collection", "POST", listOf(
            Pair("title", title),
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
                data = data + Pair("title", title)
            }
        }

        val result = super.fetchRequest("collection/$collection", "PUT", data)

        return try {
            val err = result.obj().get("error")
            err.toString()
        }catch (e: Exception) {
            null
        }
    }

    fun deleteCollection(collection: String) : String? {
        val result = super.fetchRequest("collection/$collection", "DELETE", null)

        return try {
            val err = result.obj().get("error")
            err.toString()
        }catch (e: Exception) {
            null
        }
    }
}