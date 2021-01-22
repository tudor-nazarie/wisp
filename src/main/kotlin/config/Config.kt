package config

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

@JsonClass(generateAdapter = true)
data class Config(val token: String, val activators: String) {
    companion object : KoinComponent {
        private val moshi: Moshi by inject()

        @JvmStatic
        fun read(path: String): Config? {
            // TODO: 22/01/2021 show a graceful error message if the file does not exist
            val json = File(path).bufferedReader().use { it.readText() }
            val adapter = moshi.adapter(Config::class.java)
            return adapter.fromJson(json)
        }
    }
}