package com.max.timemaster.data

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.facebook.AccessToken
import com.facebook.login.LoginResult
import com.google.firebase.auth.FirebaseUser

interface TimeMasterDataSource {
    suspend fun getSelectEvent(greaterThan: Long, lessThan: Long): Result<List<CalendarEvent>>

    suspend fun postEvent(calendarEvent: CalendarEvent): Result<Boolean>

    suspend fun postUser(user: User): Result<Boolean>

    suspend fun postDate(myDate: MyDate): Result<Boolean>

    suspend fun postFavorite(dateFavorite: DateFavorite): Result<Boolean>

    suspend fun postCost(dateCost: DateCost): Result<Boolean>

    suspend fun updateDate(myDate: MyDate): Result<Boolean>

    suspend fun upUserExp(exp: Long): Result<Boolean>

    suspend fun syncImage(uri: Uri): Result<String>

    suspend fun handleFacebookAccessToken(token: AccessToken?): Result<FirebaseUser?>

    fun getLiveAllEvent(): MutableLiveData<List<CalendarEvent>>

    fun getLiveUser(): MutableLiveData<User>

    fun getLiveMyDate(): MutableLiveData<List<MyDate>>

    fun getLiveDateFavorite(): MutableLiveData<List<DateFavorite>>

    fun getLiveDateCost(): MutableLiveData<List<DateCost>>
}
