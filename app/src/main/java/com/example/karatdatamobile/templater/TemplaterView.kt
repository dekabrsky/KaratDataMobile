package com.example.karatdatamobile.templater

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface TemplaterView: MvpView {
    fun showLoadSign()
    fun hideLoadSign()
    fun showDialog()
    fun showOnSuccessWrite(msg: String)
}