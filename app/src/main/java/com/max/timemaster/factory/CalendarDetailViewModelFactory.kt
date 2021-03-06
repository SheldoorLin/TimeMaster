package com.max.timemaster.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.max.timemaster.calendar.CalendarDetailViewModel
import com.max.timemaster.MessageViewModel
import com.max.timemaster.data.TimeMasterRepository


@Suppress("UNCHECKED_CAST")
class CalendarDetailViewModelFactory constructor(
    private val timeMasterRepository: TimeMasterRepository,
    private val string: String
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(CalendarDetailViewModel::class.java) ->
                    CalendarDetailViewModel(timeMasterRepository,string)
                isAssignableFrom(MessageViewModel::class.java) ->
                    MessageViewModel(
                        timeMasterRepository,
                        string
                    )
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
