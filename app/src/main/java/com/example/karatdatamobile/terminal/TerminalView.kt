package com.example.karatdatamobile.terminal

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface TerminalView: MvpView {
    fun onDataChange()
}