package com.example.fredagsprojekt3

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.StringWriter
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.URL

class SecApi : AppCompatActivity() {

    private lateinit var searchTermEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var gifImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sec_api)

        searchTermEditText = findViewById(R.id.searchText)
        searchButton = findViewById(R.id.searchBtn)
        gifImageView = findViewById(R.id.imageField)

        searchButton.setOnClickListener {
            val searchTerm = searchTermEditText.text.toString()
            searchGifs(searchTerm)

        }
    }

    private fun searchGifs(searchTerm: String){
        Thread {
            val searchResult = getSearchResults(searchTerm)
            val resultsArray = searchResult?.optJSONArray("results")

            if (resultsArray != null && resultsArray.length() > 0) {
                val firstResult = resultsArray.optJSONObject(0)
                val mediaFormats = firstResult?.optJSONObject("media_formats")
                val gifUrl = mediaFormats?.optJSONObject("nanowebm")?.optString("url")

                runOnUiThread {
                    if (!gifUrl.isNullOrEmpty()) {
                        Glide.with(this@SecApi)
                            .load(gifUrl)
                            .into(gifImageView)
                    } else {
                        Log.e("MyApp", "Gif URL is empty")
                    }
                }
            } else {
                Log.e("MyApp", "No results found in API response")
            }
        }.start()
    }


    private fun getSearchResults(searchTerm: String): JSONObject? {
        val url = "https://tenor.googleapis.com/v2/search?q=${searchTerm}&key=AIzaSyDFdPKZl-OmFwZAFlqy-EL5OrrIKjQB2Kg&client_key=my_test_app&limit=1"

        try {
            return get(url)
        } catch (ignored: IOException) {
        } catch (ignored: JSONException) {
        }
        return null
    }

    private fun get(url: String): JSONObject {
        var connection: HttpURLConnection? = null
        try {
            // Get request
            connection = URL(url).openConnection() as HttpURLConnection
            connection.doInput = true
            connection.doOutput = true
            connection.requestMethod = "GET"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")

            // Handle failure
            val statusCode = connection.responseCode
            if (statusCode != HttpURLConnection.HTTP_OK && statusCode != HttpURLConnection.HTTP_CREATED) {
                val error = "HTTP Code: '$statusCode' from '$url'"
                Log.d("max", error)
                throw ConnectException(error)
            }
            return parser(connection)

        } catch (ignored: Exception) {
            Log.d("max", "failed")
        } finally {
            connection?.disconnect()
        }
        return JSONObject("")
    }

    private fun parser(connection: HttpURLConnection): JSONObject {
        val buffer = CharArray(1024 * 4)
        var n: Int
        var stream: InputStream? = null
        try {
            stream = BufferedInputStream(connection.inputStream)
            val reader = InputStreamReader(stream, "UTF-8")
            val writer = StringWriter()
            while (-1 != reader.read(buffer).also { n = it }) {
                writer.write(buffer, 0, n)
            }
            return JSONObject(writer.toString())
        } catch (ignored: IOException) {
        } finally {
            stream?.close()
        }
        return JSONObject("")
    }
}
