package com.chyrta.mimi.data.local.model

import com.squareup.moshi.Json
import dm.audiostreamer.MediaMetaData
import java.util.concurrent.TimeUnit

data class Song(
  val id: Int,
  @Json(name = "created_at") val createdAt: String,
  @Json(name = "release_date") val releaseDate: String,
  @Json(name = "release_timestamp") val releaseTimestamp: Int,
  @Json(name = "user_id") val userId: Int,
  val duration: Int,
  val permalink: String,
  val description: String,
  val geo: String,
  val tags: String,
  @Json(name = "taged_artists") val tagedArtists: String,
  val bpm: String,
  val key: String,
  val license: String,
  val version: String,
  val type: String,
  val downloadable: Int,
  val genre: String,
  @Json(name = "genre_slush") val genreSlush: String,
  val title: String,
  val uri: String,
  @Json(name = "permalink_url") val permalinkUrl: String,
  val thumb: String,
  @Json(name = "artwork_url") val artworkUrl: String,
  @Json(name = "artwork_url_retina") val artworkUrlRetina: String,
  @Json(name = "background_url") val backgroundUrl: String,
  @Json(name = "waveform_data") val waveformData: String,
  @Json(name = "waveform_url") val waveformUrl: String,
  val user: Artist,
  @Json(name = "stream_url") val streamUrl: String,
  @Json(name = "preview_url") val previewUrl: String,
  @Json(name = "download_url") val downloadUrl: String,
  @Json(name = "download_filename") val downloadFilename: String,
  @Json(name = "playback_count") val playbackCount: Int,
  @Json(name = "download_count") val downloadCount: Int,
  @Json(name = "favoritings_count") val favoritingsCount: Int,
  @Json(name = "reshares_count") val resharesCount: Int,
  @Json(name = "comment_count") val commentCount: Int,
  val played: Boolean,
  val favorited: Boolean,
  val liked: Boolean,
  val reshared: Boolean
)

fun convertSongToMediaMetaData(song: Song): MediaMetaData {
    val mediaMetadata = MediaMetaData()
    mediaMetadata.mediaTitle = song.title
    mediaMetadata.mediaArtist = song.user.username
    mediaMetadata.mediaId = song.id.toString()
    mediaMetadata.mediaUrl = song.streamUrl
    mediaMetadata.mediaArt = song.artworkUrlRetina
    mediaMetadata.mediaDuration = TimeUnit.SECONDS.toMillis(song.duration.toLong()).toString()
    return mediaMetadata
}
