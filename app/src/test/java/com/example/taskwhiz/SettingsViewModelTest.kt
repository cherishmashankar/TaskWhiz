package com.example.taskwhiz

import com.example.taskwhiz.presentation.viewmodel.SettingsViewModel
import app.cash.turbine.test
import com.example.taskwhiz.domain.model.AppTheme
import com.example.taskwhiz.domain.model.Language
import com.example.taskwhiz.domain.usecase.settings.GetLanguageUseCase
import com.example.taskwhiz.domain.usecase.settings.GetThemeUseCase
import com.example.taskwhiz.domain.usecase.settings.SetLanguageUseCase
import com.example.taskwhiz.domain.usecase.settings.SetThemeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import com.google.common.truth.Truth.assertThat

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val getLanguage: GetLanguageUseCase = mock()
    private val getTheme: GetThemeUseCase = mock()
    private val setLanguage: SetLanguageUseCase = mock()
    private val setTheme: SetThemeUseCase = mock()

    private lateinit var viewModel: SettingsViewModel
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `default state should be ENGLISH and LIGHT until flows emit`() = runTest {
        whenever(getLanguage()).thenReturn(flowOf()) // no emission
        whenever(getTheme()).thenReturn(flowOf())   // no emission

        viewModel = SettingsViewModel(getLanguage, getTheme, setLanguage, setTheme)

        assertThat(viewModel.language.value).isEqualTo(Language.ENGLISH)
        assertThat(viewModel.theme.value).isEqualTo(AppTheme.LIGHT)
    }

    @Test
    fun `language flow emission should update state`() = runTest {
        whenever(getLanguage()).thenReturn(flowOf(Language.GERMAN))
        whenever(getTheme()).thenReturn(flowOf(AppTheme.LIGHT))

        viewModel = SettingsViewModel(getLanguage, getTheme, setLanguage, setTheme)
        advanceUntilIdle()

        viewModel.language.test {
            assertThat(awaitItem()).isEqualTo(Language.GERMAN)
        }
    }

    @Test
    fun `theme flow emission should update state`() = runTest {
        whenever(getLanguage()).thenReturn(flowOf(Language.ENGLISH))
        whenever(getTheme()).thenReturn(flowOf(AppTheme.DARK))

        viewModel = SettingsViewModel(getLanguage, getTheme, setLanguage, setTheme)
        advanceUntilIdle()

        viewModel.theme.test {
            assertThat(awaitItem()).isEqualTo(AppTheme.DARK)
        }
    }

    @Test
    fun `changeLanguage should call setLanguageUseCase`() = runTest {
        whenever(getLanguage()).thenReturn(flowOf(Language.ENGLISH))
        whenever(getTheme()).thenReturn(flowOf(AppTheme.LIGHT))

        viewModel = SettingsViewModel(getLanguage, getTheme, setLanguage, setTheme)

        viewModel.changeLanguage(Language.GERMAN)
        advanceUntilIdle()

        verify(setLanguage).invoke(Language.GERMAN)
    }

    @Test
    fun `changeTheme should call setThemeUseCase`() = runTest {
        whenever(getLanguage()).thenReturn(flowOf(Language.ENGLISH))
        whenever(getTheme()).thenReturn(flowOf(AppTheme.LIGHT))

        viewModel = SettingsViewModel(getLanguage, getTheme, setLanguage, setTheme)

        viewModel.changeTheme(AppTheme.DARK)
        advanceUntilIdle()

        verify(setTheme).invoke(AppTheme.DARK)
    }
}