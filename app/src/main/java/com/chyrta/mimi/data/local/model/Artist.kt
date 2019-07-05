package com.chyrta.mimi.data.local.model

import com.squareup.moshi.Json

data class Artist(
  val id: String,
  val permalink: String,
  val username: String,
  val uri: String,
  @Json(name = "permalink_url") val permalinkUrl: String = "",
  @Json(name = "avatar_url") val avatarUrl: String = "",
  @Json(name = "background_url") val backgroundUrl: String = "",
  val description: String = "",
  val geo: String = "",
  @Json(name = "track_count") val trackCount: Int = -1,
  @Json(name = "likes_count") val likesCount: Int = -1,
  @Json(name = "followers_count") val followersCount: Int = -1,
  val following: Boolean = false
)
