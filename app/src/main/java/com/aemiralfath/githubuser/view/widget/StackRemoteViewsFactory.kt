package com.aemiralfath.githubuser.view.widget

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.aemiralfath.githubuser.R
import com.aemiralfath.githubuser.model.db.FavoriteUserApplication
import com.aemiralfath.githubuser.model.db.entity.FavoriteUser
import com.bumptech.glide.Glide

class StackRemoteViewsFactory(
    private val context: Context
) : RemoteViewsService.RemoteViewsFactory {

    private var widgetItem = ArrayList<FavoriteUser>()
    private var identityToken = Binder.clearCallingIdentity()
    private val favoriteUserApplication = FavoriteUserApplication()

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        widgetItem.clear()
        favoriteUserApplication.repository.setListFavoriteUser()
        widgetItem = favoriteUserApplication.repository.favoriteUserList as ArrayList<FavoriteUser>
        Log.d("WIDGETITEM", widgetItem.size.toString())
    }

    override fun onDestroy() {
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun getCount(): Int = widgetItem.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)
        Log.d("WIDGET", widgetItem[position].avatar.toString())

        try {
            rv.setImageViewBitmap(
                R.id.img_widget,
                Glide.with(context).asBitmap()
                    .load(widgetItem[position].avatar).submit()
                    .get()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val extras = bundleOf(
            FavoriteUserWidget.EXTRA_ITEM to position
        )

        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(p0: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}