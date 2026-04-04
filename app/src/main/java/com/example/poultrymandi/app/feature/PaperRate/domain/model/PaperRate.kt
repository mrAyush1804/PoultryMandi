package com.example.poultrymandi.app.feature.PaperRate.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class PaperRate(
    val id: String? = null,

    @SerialName("image_url")
    val imageUrl: String = "",

    @SerialName("association_name")
    val associationName: String = "",

    @SerialName("date_label")
    val dateLabel: String = "",

    val city: String = "",

    @SerialName("created_at")
    val createdAt: String = "",
)