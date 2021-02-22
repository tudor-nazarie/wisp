package wisp.utils

import kotlinx.serialization.json.Json

val formatJson: Json = Json { 
    ignoreUnknownKeys = true
}
