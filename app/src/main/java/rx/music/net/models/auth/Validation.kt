package rx.music.net.models.auth

/** Created by Maksim Sukhotski on 4/4/2017. */
class Validation(val error: String,
                 val error_description: String,
                 val phone_mask: String,
                 val redirect_uri: String,
                 val validation_sid: String,
                 val validation_type: String)