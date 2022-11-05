package com.persia.test.ui.panel.products.brand.list

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.persia.test.R
import com.persia.test.databinding.FragmentBrandListBinding
import com.persia.test.ui.panel.PanelActivity
import kotlin.random.Random


const val CHANNEL_ID = "brand_list"

class BrandListFragment : Fragment() {

    private val viewModel: BrandListViewModel by viewModels()

    private lateinit var _binding: FragmentBrandListBinding
    val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_brand_list, container, false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.brandListMakeNotifBtn.setOnClickListener {
            createNotif()
        }
    }

    private fun createNotif() {
        val intent = Intent(context, PanelActivity::class.java)
        val notificationManager = activity!!.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        val notificationID = Random.nextInt() // make random so the old notifs won't be overridden

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // val pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setContentTitle("brand list")
            .setContentText("test msg")
            .setSmallIcon(R.drawable.ic_baseline_category_24)
            .setAutoCancel(true) // notification is deleted when clicked on
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "channelName"
        val channel = NotificationChannel(CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_HIGH).apply {
            description = " test desc"
            enableLights(true)
            lightColor = Color.BLUE
        }
        notificationManager.createNotificationChannel(channel)
    }
}