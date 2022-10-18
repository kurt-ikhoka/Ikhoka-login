package com.ikhoka.login.ui

import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.LifecycleOwner
import com.ikhoka.R
import com.ikhoka.core.Resource
import com.ikhoka.core.collectIn
import com.ikhoka.core.ext.fadeTo
import com.ikhoka.core.ui.BaseComponent
import com.ikhoka.login.meta.LoginState
import kotlinx.coroutines.flow.Flow

class LoadingComponent(owner: LifecycleOwner, private val view: ViewGroup) :
    BaseComponent<Resource<LoginState>>(owner) {
    override fun collect(visibilityFlow: Flow<Boolean>, dataFlow: Flow<Resource<LoginState>>) {
        visibilityFlow.collectIn(owner) { visible ->
            view.fadeTo(visible)
        }
    }
}