package controller

import model.Password
import model.placeholder
import java.lang.Exception

class CollectionsModule : ClientModule() {
    fun fetchCollections() : Pair<List<String>, String?> {
        val result = super.fetchRequest("collection", "GET", null)

        return try {
            val err = result.obj().get("error")
            Pair(listOf(), err.toString())
        }catch (e: Exception) {
            var _collections: List<String>
            try {
                _collections = result.array().toList() as List<String>
            } catch (e: Exception) {
                _collections = listOf()
            }

            var collections: List<String> = listOf()
            for (collection in _collections) {
                collections = collections + collection
            }
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
}