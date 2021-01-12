package di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import network.OpenDotaService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

fun provideMoshi(): Moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

fun provideOpenDotaService(moshi: Moshi): OpenDotaService = Retrofit.Builder()
    .baseUrl("https://api.opendota.com/api/")
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()
    .create(OpenDotaService::class.java)