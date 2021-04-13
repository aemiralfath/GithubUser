package com.aemiralfath.githubuser.model.service

import android.content.Intent
import android.widget.RemoteViewsService
import com.aemiralfath.githubuser.view.widget.StackRemoteViewsFactory

class StackWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(p0: Intent?): RemoteViewsFactory {
        return StackRemoteViewsFactory(this.applicationContext)
    }
}