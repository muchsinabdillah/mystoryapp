package com.example.mystoryapp.viewmodel

import com.example.mystoryapp.data.model.Login
import com.example.mystoryapp.data.model.LoginEntity
import com.example.mystoryapp.data.model.RegisterEntity
import com.example.mystoryapp.data.model.StoryEntity
import com.example.mystoryapp.data.resource.remote.response.GetLoginResponse

object DataDummy {
    fun generateDummyNewsEntity(): List<StoryEntity> {
        val newsList = ArrayList<StoryEntity>()
        for (i in 0..10) {
            val news = StoryEntity(
                "story-3j5_SJu1m0VHSFwe $i",
                "Taufik Amaryansyah",
                "test",
                "https://story-api.dicoding.dev/images/stories/photos-1665896345911_6jptWeYj.jpg",
                "2022-10-16T04:59:05.916Z",
                "-6.2285881",
                "107.0809884"
            )
            newsList.add(news)
        }
        return newsList
    }
    fun generateDummyQuoteResponse(): List<StoryEntity> {
        val items: MutableList<StoryEntity> = arrayListOf()
        for (i in 0..100) {
            val quote = StoryEntity(
                "story-3j5_SJu1m0VHSFwe $i",
                "Taufik Amaryansyah",
                "test",
                "https://story-api.dicoding.dev/images/stories/photos-1665896345911_6jptWeYj.jpg",
                "2022-10-16T04:59:05.916Z",
                "-6.2285881",
                "107.0809884"
            )
            items.add(quote)
        }
        return items
    }

    fun generateDummyRequestLogin(): LoginEntity {
        return  LoginEntity("muchsin@rsyarsi.co.id", "123456")
    }

    fun generateDummyResponseLogin(): GetLoginResponse {
        val newLoginResult = Login("user-49waVShUdCdKJ1B3", "Muchsin", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTQ5d2FWU2hVZENkS0oxQjMiLCJpYXQiOjE2NjU5MzYyNjB9.ViOaHhThdJOJ06wM5M7y_mGv7A4H5u76NLS1PzPloao")
        return GetLoginResponse(false, "Login successfully", newLoginResult)
    }

    fun generateDummyRequestRegister(): RegisterEntity {
        return RegisterEntity("monica", "123@gmail.com", "123456")

    }
}