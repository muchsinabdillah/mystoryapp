package com.example.mystoryapp.data.resource.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.myunlimitedquotes.getOrAwaitValue
import com.example.mystoryapp.data.model.StoryEntity
import com.example.mystoryapp.data.resource.remote.response.GetLoginResponse
import com.example.mystoryapp.viewmodel.DataDummy
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import java.io.File

@ExperimentalCoroutinesApi
class RemoteRepositoryTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: RemoteRepository

    @Mock
    private var mockFile = File("fileName")

    @Before
    fun setUp() {
        storyRepository = mock(RemoteRepository::class.java)
    }

    @Test
    fun `when datastory should return the right data and not null`() {
        val dummyStories = DataDummy.generateDummyNewsEntity()
        val expectedStories = MutableLiveData<List<StoryEntity>>()
        expectedStories.value = dummyStories

        `when`(storyRepository.datastory).thenReturn(expectedStories)

        val actualStories = storyRepository.datastory.getOrAwaitValue()

        verify(storyRepository).datastory

        assertNotNull(actualStories)
        assertEquals(expectedStories.value, actualStories)
        assertEquals(dummyStories.size, actualStories.size)
    }
    @Test
    fun `when message should return the right data and not null`() {
        val expectedRegisterMessage = MutableLiveData<String>()
        expectedRegisterMessage.value = "Story Uploaded"

        `when`(storyRepository.message).thenReturn(expectedRegisterMessage)

        val actualRegisterMessage = storyRepository.message.getOrAwaitValue()

        verify(storyRepository).message
        assertNotNull(actualRegisterMessage)
        assertEquals(expectedRegisterMessage.value, actualRegisterMessage)
    }


    @Test
    fun `when userlogin should return the right login response and not null`() {
        val dummyResponselogin = DataDummy.generateDummyResponseLogin()

        val expectedLogin = MutableLiveData<GetLoginResponse>()
        expectedLogin.value = dummyResponselogin

        `when`(storyRepository.userlogin).thenReturn(expectedLogin)

        val actualLoginResponse = storyRepository.userlogin.getOrAwaitValue()

        verify(storyRepository).userlogin
        assertNotNull(actualLoginResponse)
        assertEquals(expectedLogin.value, actualLoginResponse)
    }

    @Test
    fun `verify register function is working`() {
        val dummyRequestRegister = DataDummy.generateDummyRequestRegister()
        val expectedRegisterMessage = MutableLiveData<String>()
        expectedRegisterMessage.value = "User Created"

        storyRepository.register(dummyRequestRegister)

        verify(storyRepository).register(dummyRequestRegister)

        `when`(storyRepository.message).thenReturn(expectedRegisterMessage)

        val actualData = storyRepository.message.getOrAwaitValue()

        verify(storyRepository).message
        assertNotNull(actualData)
        assertEquals(expectedRegisterMessage.value, actualData)
    }

    @Test
    fun `verify login function is working`() {
        val dummyRequestLogin = DataDummy.generateDummyRequestLogin()
        val dummyResponseLogin = DataDummy.generateDummyResponseLogin()

        val expectedResponseLogin = MutableLiveData<GetLoginResponse>()
        expectedResponseLogin.value = dummyResponseLogin

        storyRepository.login(dummyRequestLogin)

        verify(storyRepository).login(dummyRequestLogin)

        `when`(storyRepository.userlogin).thenReturn(expectedResponseLogin)

        val actualData = storyRepository.userlogin.getOrAwaitValue()

        verify(storyRepository).userlogin
        assertNotNull(actualData)
        assertEquals(expectedResponseLogin.value, actualData)
    }
    @Test
    fun `verify upload function is working`() {
        val expectedRegisterMessage = MutableLiveData<String>()
        expectedRegisterMessage.value = "Story Uploaded"

        val requestImageFile = mockFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            "fileName",
            requestImageFile
        )
        val description: RequestBody = "ini description".toRequestBody("text/plain".toMediaType())
        val token = "ini token"
        val latlng = LatLng(1.1, 1.1)

        storyRepository.upload(
            imageMultipart,
            description,
            latlng.latitude,
            latlng.longitude
        )

        verify(storyRepository).upload(
            imageMultipart,
            description,
            latlng.latitude,
            latlng.longitude
        )

        `when`(storyRepository.message).thenReturn(expectedRegisterMessage)

        val actualRegisterMessage = storyRepository.message.getOrAwaitValue()

        verify(storyRepository).message
        assertNotNull(actualRegisterMessage)
        assertEquals(expectedRegisterMessage.value, actualRegisterMessage)
    }


}