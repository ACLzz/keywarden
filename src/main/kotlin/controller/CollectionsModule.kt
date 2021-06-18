package controller

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
}