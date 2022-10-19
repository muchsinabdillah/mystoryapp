package com.example.mystoryapp.viewmodel
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.myunlimitedquotes.getOrAwaitValue
import com.example.mystoryapp.data.model.StoryEntity
import com.example.mystoryapp.data.resource.remote.response.GetLoginResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @Mock
    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        mainViewModel =  mock(MainViewModel::class.java)
    }

    @Test
    fun `when login message should return the right data and not null`() {
        val expectedLoginMessage = MutableLiveData<String>()
        expectedLoginMessage.value = "Login Successfully"

        `when`(mainViewModel.message).thenReturn(expectedLoginMessage)

        val actualMessage = mainViewModel.message.getOrAwaitValue()

        verify(mainViewModel).message
        assertNotNull(actualMessage)
        assertEquals(expectedLoginMessage.value, actualMessage)
    }


    @Test
    fun `when userlogin should return the right login user data and not null`() {
        val dummyResponselogin = DataDummy.generateDummyResponseLogin()

        val expectedLogin = MutableLiveData<GetLoginResponse>()
        expectedLogin.value = dummyResponselogin

        `when`(mainViewModel.userlogin).thenReturn(expectedLogin)

        val actualLoginResponse = mainViewModel.userlogin.getOrAwaitValue()

        verify(mainViewModel).userlogin
        assertNotNull(actualLoginResponse)
        assertEquals(expectedLogin.value, actualLoginResponse)

    }
    @Test
    fun `verify login function is working`()    {

        val dummyRequestLogin =  DataDummy.generateDummyRequestLogin()
        val dummyResponseLogin = DataDummy.generateDummyResponseLogin()

        val expectedResponseLogin = MutableLiveData<GetLoginResponse>()
        expectedResponseLogin.value = dummyResponseLogin

        mainViewModel.login(dummyRequestLogin)
        verify(mainViewModel).login(dummyRequestLogin)

        `when`(mainViewModel.userlogin).thenReturn(expectedResponseLogin)

        val actualData = mainViewModel.userlogin.getOrAwaitValue()
        verify(mainViewModel).userlogin
        assertNotNull(expectedResponseLogin)
        assertEquals(expectedResponseLogin.value, actualData)


    }

    @Test
    fun `when Story load message should return the right data and not null`() {
        val generateDummyQuoteResponse =  DataDummy.generateDummyQuoteResponse()
        val expectedLoadStoryMessage = MutableLiveData<List<StoryEntity>>()
        expectedLoadStoryMessage.value = generateDummyQuoteResponse

        `when`(mainViewModel.datastory).thenReturn(expectedLoadStoryMessage)

        val actualMessage = mainViewModel.datastory.getOrAwaitValue()

        verify(mainViewModel).datastory
        assertNotNull(actualMessage)
        assertEquals(expectedLoadStoryMessage.value, actualMessage)
    }
}