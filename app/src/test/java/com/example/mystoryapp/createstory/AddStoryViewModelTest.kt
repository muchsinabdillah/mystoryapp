package com.example.mystoryapp.createstory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.myunlimitedquotes.getOrAwaitValue
import com.example.mystoryapp.ui.createstory.AddStoryViewModel
import com.google.android.gms.maps.model.LatLng
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
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var addViewModel: AddStoryViewModel
    private var mockFile = File("fileName")

    @Before
    fun setUp() {
        addViewModel = mock(AddStoryViewModel::class.java)
    }



    @Test
    fun `when message should return the right data and not null`() {
        val expectedRegisterMessage = MutableLiveData<String>()
        expectedRegisterMessage.value = "Story Uploaded"

        `when`(addViewModel.message).thenReturn(expectedRegisterMessage)

        val actualRegisterMessage = addViewModel.message.getOrAwaitValue()

        verify(addViewModel).message
        assertNotNull(actualRegisterMessage)
        assertEquals(expectedRegisterMessage.value, actualRegisterMessage)
    }

    @Test
    fun `verify upload function is working`() {
        val expectedUploadMessage = MutableLiveData<String>()
        expectedUploadMessage.value = "Story Uploaded"

        val requestImageFile = mockFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            "fileName",
            requestImageFile
        )
        val description: RequestBody = "ini description".toRequestBody("text/plain".toMediaType())
        val latlng = LatLng(1.1, 1.1)

        addViewModel.upload(imageMultipart, description, latlng.latitude, latlng.longitude)
        verify(addViewModel).upload(
            imageMultipart,
            description,
            latlng.latitude,
            latlng.longitude
        )

        `when`(addViewModel.message).thenReturn(expectedUploadMessage)
        val actualUploadMessage = addViewModel.message.getOrAwaitValue()
        verify(addViewModel).message
        assertNotNull(actualUploadMessage)
        assertEquals(expectedUploadMessage.value, actualUploadMessage)
    }
}