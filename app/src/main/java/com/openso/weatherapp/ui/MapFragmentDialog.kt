package com.openso.weatherapp.ui

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.carto.styles.AnimationStyle
import com.carto.styles.AnimationStyleBuilder
import com.carto.styles.AnimationType
import com.carto.styles.MarkerStyleBuilder
import com.openso.weatherapp.R
import com.openso.weatherapp.databinding.LayoutMapBinding
import org.neshan.common.model.LatLng
import org.neshan.mapsdk.internal.utils.BitmapUtils
import org.neshan.mapsdk.model.Marker
import java.lang.ClassCastException

class MapFragmentDialog : DialogFragment() {

    private lateinit var binding: LayoutMapBinding

    // marker animation style
    private lateinit var animSt: AnimationStyle

    private var mCallbackActivity: GetLatLng? = null

    private lateinit var latLng : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = LayoutMapBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.map.setZoom(4f, 0f)

        binding.map.setOnMapClickListener {
            val marker: Marker = createMarker(it)
            binding.map.addMarker(marker)

            latLng = "${it.latitude}, ${it.longitude}"

            requireActivity().runOnUiThread {

                kotlin.run {

                    binding.checkCity.visibility = View.VISIBLE

                    binding.checkCity.setOnClickListener {

                        mCallbackActivity?.onSelected(latLng)
                        dismiss()

                    }
                }
            }

        }






    }



    private fun createMarker(loc: LatLng): Marker {

        clearMarkers()
        // Creating animation for marker. We should use an object of type AnimationStyleBuilder, set
        // all animation features on it and then call buildStyle() method that returns an object of type
        // AnimationStyle
        val animStBl = AnimationStyleBuilder()
        animStBl.fadeAnimationType = AnimationType.ANIMATION_TYPE_SMOOTHSTEP
        animStBl.sizeAnimationType = AnimationType.ANIMATION_TYPE_SPRING
        animSt = animStBl.buildStyle()

        // Creating marker style. We should use an object of type MarkerStyleCreator, set all features on it
        // and then call buildStyle method on it. This method returns an object of type MarkerStyle
        val markStCr = MarkerStyleBuilder()
        markStCr.size = 30f
        markStCr.bitmap = BitmapUtils.createBitmapFromAndroidBitmap(
            BitmapFactory.decodeResource(
                resources, R.drawable.ic_marker_blue
            )
        )
        // AnimationStyle object - that was created before - is used here
        markStCr.animationStyle = animSt
        val markSt = markStCr.buildStyle()

        // Creating marker
        return Marker(loc, markSt)

    }

    private fun clearMarkers() {
        binding.map.clearMarkers()
    }

    interface GetLatLng {

        fun onSelected(latLng: String)

    }



    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            mCallbackActivity = activity as GetLatLng

        } catch (e: ClassCastException) {
            Log.d("MyDialog", "Activity doesn't implement the ISelectedData interface");
        }

    }



}