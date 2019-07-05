package com.chyrta.mimi.data.remote

import com.chyrta.mimi.data.local.model.Artist
import com.chyrta.mimi.data.local.model.Song
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MimiService {

    @GET("{artistName}")
    fun getArtist(@Path("artistName") artistName: String): Single<Artist>

    @GET("feed")
    fun getTopSongs(
      @Query("type") type: String = "popular",
      @Query("page") page: Int = 1,
      @Query("count") count: Int = 10
    ): Single<List<Song>>

    @GET("{artistName}")
    fun getArtistSongs(
      @Path("artistName") artistName: String,
      @Query("type") type: String = "tracks",
      @Query("page") page: Int,
      @Query("count") count: Int = 10
    ): Single<List<Song>>

    companion object {
        const val BASE_URL = "https://api-v2.hearthis.at/"
    }
}
