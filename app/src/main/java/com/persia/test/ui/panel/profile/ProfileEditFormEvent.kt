package com.persia.test.ui.panel.profile

sealed class ProfileEditFormEvent {

    data class FirstnameChanged(val firstname: String) : ProfileEditFormEvent()
    data class LastnameChanged(val lastname: String) : ProfileEditFormEvent()

    object Submit : ProfileEditFormEvent()
}