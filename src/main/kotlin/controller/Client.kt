package controller

import app.Config
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpPut
import com.github.kittinunf.fuel.json.FuelJson
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import javafx.beans.property.SimpleStringProperty
import org.bouncycastle.jce.provider.BouncyCastleProvider
import tornadofx.*
import java.security.MessageDigest
import java.security.Security
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

open class ClientModule {
    lateinit var fetchRequest: ((String, String, p3: List<Pair<String, String>>?) -> FuelJson)
}

object Client {
    private val tokenProperty = SimpleStringProperty("")
    private var token by tokenProperty
    private lateinit var password: String
    fun settoken(t: String) { token = t }
    fun setpassword(p: String) { password = p }

    val Auth = AuthModule().apply { fetchRequest = ::fetchRequest }
    val Collections = CollectionsModule().apply { fetchRequest = ::fetchRequest }
    val Passwords = PasswordsModule().apply { fetchRequest = ::fetchRequest }

    private fun fetchRequest(path: String, _request: String, data: List<Pair<String, String>>? = null): FuelJson {
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

        val (_, response, result) = request.responseJson()
        if (result is Result.Failure) {
            var errStr: String
            if (response.statusCode == -1) {
                errStr = "{\"error\": \"${result.error.message}\"}"
            } else {
                errStr = String(response.data)
            }
            errStr = errStr.replace("\n", "")
            return FuelJson(errStr)
        }
        return result.get()
    }

    fun encryptIt(d: String): String {
        Security.addProvider(BouncyCastleProvider())

        val key = Base64.getEncoder().encodeToString(password.hashMe("SHA-256").toByteArray()).slice(0..31)
        val keyBytes: ByteArray = key.toByteArray(charset("UTF8"))
        val skey = SecretKeySpec(keyBytes, "AES")
        val input = d.toByteArray(charset("UTF8"))

        synchronized(Cipher::class.java) {
            val cipher = Cipher.getInstance("AES/ECB/PKCS7Padding")
            cipher.init(Cipher.ENCRYPT_MODE, skey)

            val cipherText = ByteArray(cipher.getOutputSize(input.size))
            var ctLength = cipher.update(
                input, 0, input.size,
                cipherText, 0
            )
            ctLength += cipher.doFinal(cipherText, ctLength)
            return String(Base64.getEncoder().encode(cipherText))
        }
    }

    fun decryptIt(d: String): String {
        Security.addProvider(BouncyCastleProvider())

        val key = Base64.getEncoder().encodeToString(password.hashMe("SHA-256").toByteArray()).slice(0..31)
        val keyBytes = key.toByteArray(charset("UTF8"))
        val skey = SecretKeySpec(keyBytes, "AES")
        val input = org.bouncycastle.util.encoders.Base64
            .decode(d.trim { it <= ' ' }.toByteArray(charset("UTF8")))

        synchronized(Cipher::class.java) {
            val cipher = Cipher.getInstance("AES/ECB/PKCS7Padding")
            cipher.init(Cipher.DECRYPT_MODE, skey)

            val plainText = ByteArray(cipher.getOutputSize(input.size))
            var ptLength = cipher.update(input, 0, input.size, plainText, 0)
            ptLength += cipher.doFinal(plainText, ptLength)
            return String(plainText).trim { it <= ' ' }
        }
    }
}


fun String.hashMe(algo: String): String {
    val bytes = this.toByteArray()
    val md = MessageDigest.getInstance(algo)
    val digest = md.digest(bytes)
    return digest.fold("") { str, it -> str + "%02x".format(it) }
}
