package com.chyrta.mimi.di

import com.chyrta.mimi.artist.ArtistActionProcessorHolder
import com.chyrta.mimi.artist.ArtistViewModel
import com.chyrta.mimi.artist.SongAdapter
import com.chyrta.mimi.artists.ArtistsActionProcessorHolder
import com.chyrta.mimi.artists.ArtistsAdapter
import com.chyrta.mimi.artists.ArtistsViewModel
import com.chyrta.mimi.data.MimiRepository
import com.chyrta.mimi.data.remote.MimiService
import com.chyrta.mimi.util.schedulers.BaseSchedulerProvider
import com.chyrta.mimi.util.schedulers.SchedulerProvider
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

val schedulerProviders = module {
    single<BaseSchedulerProvider> { SchedulerProvider }
}

val artistsModule = module {
    single { ArtistsActionProcessorHolder(get(), get()) }
    factory { ArtistsAdapter() }
    viewModel { ArtistsViewModel(get()) }
}

val artistModule = module {
    single { ArtistActionProcessorHolder(get(), get()) }
    factory { SongAdapter() }
    viewModel { ArtistViewModel(get()) }
}

val networkModule = module {
    single { provideMoshi() }
    single { provideLogger() }
    single { provideClient(get()) }
    single { provideRetrofit(get(), get()) }
    single { provideSongsService(get()) }
}

val repositoryModule = module {
    single { MimiRepository(get()) }
}

fun provideMoshi(): Moshi {
    return Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
}

fun provideLogger(): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BASIC
    return logging
}

fun provideClient(logger: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(logger)
        .build()
}

fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit {
    return Retrofit.Builder()
        .client(client)
        .baseUrl(MimiService.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
}

fun provideSongsService(retrofit: Retrofit): MimiService {
    return retrofit.create(MimiService::class.java)
}
