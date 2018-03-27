package com.alegra.andresjose1983.test.module

import android.app.Application
import com.alegra.andresjose1983.test.service.AlegraAPI
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Created by andre.
 */
@Module
class NetModule(val url: String) {

    @Provides
    @Singleton
    fun provideMoshiConverter() = MoshiConverterFactory.create()

    @Provides
    @Singleton
    fun provideCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(application.cacheDir, cacheSize.toLong())
    }

    @Provides
    @Singleton
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder {
        val client = OkHttpClient.Builder()
        client.addNetworkInterceptor(StethoInterceptor())

        return client
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor()

    @Provides
    @Singleton
    fun provideOkhttpClient(cache: Cache, client: OkHttpClient.Builder,
                            interceptor: HttpLoggingInterceptor,
                            application: Application): OkHttpClient {
        client.cache(cache)

        interceptor.level = HttpLoggingInterceptor.Level.BODY
        client.addInterceptor(interceptor)
        client.addInterceptor { chain ->
            val original = chain.request()

            val builder = original.newBuilder()
                    .header("Authorization", "Basic ".plus("YW5kcmVzam9zZTE5ODNAZ21haWwuY29tOjc0YzgwMjllZGRhOTgzZmNiZmRl"))
                    .method(original.method(), original.body())

            chain.proceed(builder.build())
        }
        client.addNetworkInterceptor(StethoInterceptor())

        return client.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(moshi: MoshiConverterFactory, client: OkHttpClient): Retrofit {
        var retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(moshi)
                .client(client)
                .build()

        return retrofit
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit) = retrofit.create(AlegraAPI::class.java)

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }
}