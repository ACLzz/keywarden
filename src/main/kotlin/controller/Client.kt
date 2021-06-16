package controller

import app.Config
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpDelete
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpPut
import com.github.kittinunf.fuel.json.FuelJson
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import model.Password
import model.placeholder
import org.json.JSONObject
import java.lang.Exception

object Client {
    private val tokenProperty = SimpleStringProperty("")
    private var token by tokenProperty

    fun register(login: String, password: String): String {
        val result = fetchRequest("auth/", "POST",
            listOf(Pair("login", login), Pair("password", password)))

        return try {
            val err = result.obj().get("error")
            err.toString()
        }catch (e: Exception) {
            ""
        }
    }

    fun login(login: String, password: String): String {
        val result = fetchRequest("auth/login", "POST",
            listOf(Pair("login", login), Pair("password", password)))

        return try {
            val err = result.obj().get("error")
            err.toString()
        }catch (e: Exception) {
            try {
                token = result.obj().get("token").toString()
                ""
            } catch (e: Exception) {
                "invalid credentials"
            }
        }
    }

    fun fetchCollections() : Pair<List<String>, String?> {
        val result = fetchRequest("collection", "GET", null)

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
                collections = collections + collection.toString()
            }
            Pair(collections, null)
        }
    }

    fun fetchPasswords(collection: String): Pair<List<Password>, String?> {
        if (collection == "") {
            return Pair(listOf(), null)
        }

        val result = fetchRequest("collection/$collection", "GET", null)

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

    fun getPassword(id: Int) = listOf("login", "email", "password")

    fun fetchRequest(path: String, _request: String, data: List<Pair<String, String>>? = null): FuelJson {
        val url = Config.url + path
        val request = when (_request) {
            "GET" -> url.httpGet()
            "POST" -> url.httpPost()
            "PUT" -> url.httpPut()
            "DELETE" -> url.httpDelete()
            else -> url.httpGet()
        }
        if (data != null) {
            val jsonData = with (JsonBuilder()) {
                for (p in data) {
                    add(p.first, p.second)
                }
                build()
            }.toString()
            request.jsonBody(jsonData)
        }
        request.appendHeader(Pair("Authorization", token))

        val (req, response, result) = request.responseJson()
        if (result is Result.Failure) {
            var errStr: String
            if (response.statusCode == -1) {
                errStr = "{\"error\": \"No connection to server\"}"
            } else {
                errStr = String(response.data)
            }
            errStr = errStr.replace("\n", "")
            return FuelJson(errStr)
        }
        return result.get()
    }
}