package com.example.a438finalproject.Network

    import retrofit2.Retrofit
    import retrofit2.converter.moshi.MoshiConverterFactory

    object ApiClient {
        const val BASE_URL = "https://guidance.streetline.com/v3/guidance/by-point/"

        fun makeRetroFitService() : LocationInterface {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build().create(LocationInterface::class.java)
        }
    }
