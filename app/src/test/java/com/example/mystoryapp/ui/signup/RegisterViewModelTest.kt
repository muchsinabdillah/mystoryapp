package com.example.mystoryapp.ui.signup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.myunlimitedquotes.getOrAwaitValue
import com.example.mystoryapp.viewmodel.DataDummy
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest{

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @Mock
    private lateinit var regViewModel: RegisterViewModel

    @Before
    fun setUp() {
        regViewModel = Mockito.mock(RegisterViewModel::class.java)
    }

    @Test
    fun `when message should return the right data and not null`() {
        val expectedRegisterMessage = MutableLiveData<String>()
        expectedRegisterMessage.value = "User Created"

        `when`(regViewModel.message).thenReturn(expectedRegisterMessage)

        val actualRegisterMessage = regViewModel.message.getOrAwaitValue()

        verify(regViewModel).message
        assertNotNull(actualRegisterMessage)
        assertEquals(expectedRegisterMessage.value, actualRegisterMessage)
    }
    @Test
    fun `verify register function is working`() {
        val dummyRequestRegister = DataDummy.generateDummyRequestRegister()
        val expectedRegisterMessage = MutableLiveData<String>()
        expectedRegisterMessage.value = "User Created"

        regViewModel.register(dummyRequestRegister)

        verify(regViewModel).register(dummyRequestRegister)

        `when`(regViewModel.message).thenReturn(expectedRegisterMessage)

        val actualData = regViewModel.message.getOrAwaitValue()

        verify(regViewModel).message
        assertNotNull(actualData)
        assertEquals(expectedRegisterMessage.value, actualData)
    }
}