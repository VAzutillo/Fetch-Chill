import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

data class ProfileResponse(
    val status: String,
    val message: String? = null,
    val profile: Profile? = null
)

data class Profile(
    val owner_name: String,
    val pet_name: String,
    val breed: String,
    val age: Int,
    val profile_image: String? = null,  // Add default null
    val image_url: String? = null       // Add default null
)

data class ProfileSaveResponse(
    val success: Boolean,
    val message: String,
    val image_url: String? = null  // Add default null
)

