package com.tushar.map.ui.dashboard.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.tushar.map.BuildConfig
import com.tushar.map.R
import com.tushar.map.databinding.FragmentMapBinding
import com.tushar.map.ui.base.BaseFragment
import com.tushar.map.ui.dashboard.viewmodel.DashboardViewModel
import com.tushar.map.utils.BitmapUtil
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapFragment : BaseFragment<DashboardViewModel, FragmentMapBinding>(), OnMapReadyCallback{

    private lateinit var mMap: GoogleMap
    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    private var locationPermissionGranted = false
    // The entry point to the Places API.
    private lateinit var placesClient: PlacesClient
    override val viewModel: DashboardViewModel by activityViewModels()
    override fun getLayoutBinding()= FragmentMapBinding.inflate(layoutInflater)
    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var lastKnownLocation: Location? = null
    private var likelyPlaceNames: Array<String?> = arrayOfNulls(0)
    private var likelyPlaceAddresses: Array<String?> = arrayOfNulls(0)
    private var likelyPlaceAttributions: Array<List<*>?> = arrayOfNulls(0)
    private var likelyPlaceLatLngs: Array<LatLng?> = arrayOfNulls(0)


    override fun setupView(view: View) {

        // Construct a PlacesClient

        placesClient = activity?.let {

            Places.initialize(it, BuildConfig.MAPS_API_KEY)
            Places.createClient(it)
        }!!

        activity?.let {
            // Construct a FusedLocationProviderClient.
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(it)
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment?.getMapAsync(this)

        //fragmentBinding.ivPlaces.setOnClickListener { showCurrentPlace() }

        fragmentBinding.ivPlaces.setOnClickListener { showCustomerMakerPlace() }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        /*// Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(
            MarkerOptions()
            .position(sydney)
            .title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
*/
        this.mMap.setInfoWindowAdapter(object : InfoWindowAdapter {
            // Return null here, so that getInfoContents() is called next.

            override fun getInfoContents(marker: Marker): View {
                // Inflate the layouts for the info window, title and snippet.
                val infoWindow: View = layoutInflater.inflate(
                    R.layout.custom_info_contents, fragmentBinding.root, false
                )
                val title = infoWindow.findViewById<TextView>(R.id.title)
                title.text = marker.title
                val snippet = infoWindow.findViewById<TextView>(R.id.snippet)
                snippet.text = marker.snippet
                return infoWindow
            }

            override fun getInfoWindow(arg0: Marker): View? {
                return null
            }
        })

        // Prompt the user for permission.
        getLocationPermission();
    }

    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (activity?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION)
            }
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
            // Turn on the My Location layer and the related control on the map.
            updateLocationUI();

            // Get the current location of the device and set the position of the map.
            getDeviceLocation()
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
        }
        updateLocationUI()
        getDeviceLocation()
    }

    private fun updateLocationUI() {
        if (this.mMap == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                this.mMap?.isMyLocationEnabled = true
                this.mMap?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                this.mMap?.isMyLocationEnabled = false
                this.mMap?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            this.mMap?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))

                            this.mMap?.addMarker(
                                MarkerOptions()
                                    .title("My Location")
                                    .position(LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude)))
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        this.mMap?.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        this.mMap?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun showCustomerMakerPlace() {

        this.mMap?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(getString(R.string.pmc_building_lat).toDouble(),
                    getString(R.string.pmc_building_long).toDouble()), DEFAULT_ZOOM.toFloat()))

        this.mMap?.addMarker(
            MarkerOptions()
                .title(getString(R.string.pmc_building))
                .position(LatLng(getString(R.string.pmc_building_lat).toDouble(), getString(R.string.pmc_building_long).toDouble()))
                .icon(BitmapUtil.getBitmapDescriptor(requireActivity().applicationContext, R.drawable.ic_pmc_building)))

        this.mMap?.addMarker(
            MarkerOptions()
                .title(getString(R.string.bal_Gandharva))
                .position(LatLng(getString(R.string.bal_Gandharva_lat).toDouble(), getString(R.string.bal_Gandharva_long).toDouble()))
                .icon(BitmapUtil.getBitmapDescriptor(requireActivity().applicationContext, R.drawable.ic_theater))
        )

        this.mMap?.addMarker(
            MarkerOptions()
                .title(getString(R.string.sambhaji_garden))
                .position(LatLng(getString(R.string.sambhaji_garden_lat).toDouble(), getString(R.string.sambhaji_garden_long).toDouble()))
                .icon(BitmapUtil.getBitmapDescriptor(requireActivity().applicationContext, R.drawable.ic_sambhaji_garden))
        )

        this.mMap?.addMarker(
            MarkerOptions()
                .title(getString(R.string.junglee_maharaj_mandir))
                .position(LatLng(getString(R.string.junglee_maharaj_mandir_lat).toDouble(), getString(R.string.junglee_maharaj_mandir_long).toDouble()))
                .icon(BitmapUtil.getBitmapDescriptor(requireActivity().applicationContext, R.drawable.junglee_maharaj_mandir))
        )

        this.mMap?.addMarker(
            MarkerOptions()
                .title(getString(R.string.shaniwar_wada))
                .position(LatLng(getString(R.string.shaniwar_wada_lat).toDouble(), getString(R.string.shaniwar_wada_long).toDouble()))
                .icon(BitmapUtil.getBitmapDescriptor(requireActivity().applicationContext, R.drawable.ic_fort))
        )

    }

    @SuppressLint("MissingPermission")
    private fun showCurrentPlace() {
        if (this.mMap == null) {
            return
        }
        if (locationPermissionGranted) {
            // Use fields to define the data types to return.
            val placeFields = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

            // Use the builder to create a FindCurrentPlaceRequest.
            val request = FindCurrentPlaceRequest.newInstance(placeFields)

            placesClient.findCurrentPlace(request)
                .addOnSuccessListener { response: FindCurrentPlaceResponse ->

                    // Set the count, handling cases where less than 5 entries are returned.
                    val count = if (response != null && response.placeLikelihoods.size < M_MAX_ENTRIES) {
                        response.placeLikelihoods.size
                    } else {
                        M_MAX_ENTRIES
                    }
                    var i = 0
                    for (placeLikelihood in response.placeLikelihoods) {
                        // Build a list of likely places to show the user.
                        likelyPlaceNames[i] = placeLikelihood.place.name
                        likelyPlaceAddresses[i] = placeLikelihood.place.address
                        likelyPlaceAttributions[i] = placeLikelihood.place.attributions
                        likelyPlaceLatLngs[i] = placeLikelihood.place.latLng
                        i++
                        if (i > count - 1) {
                            break
                        }
                    }

                    // Show a dialog offering the user the list of likely places, and add a
                    // marker at the selected place.
                    openPlacesDialog()
                }.addOnFailureListener { exception: Exception ->
                    if (exception is ApiException) {
                        val apiException: ApiException = exception as ApiException
                        Log.e(TAG, "Place not found: " + apiException.getStatusCode())
                    }
                }

        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.")

            // Add a default marker, because the user hasn't selected a place.
            this.mMap?.addMarker(
                MarkerOptions()
                .title(getString(R.string.default_info_title))
                .position(defaultLocation)
                .snippet(getString(R.string.default_info_snippet)))

            // Prompt the user for permission.
            getLocationPermission()
        }
    }

    private fun openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        val listener = DialogInterface.OnClickListener { dialog, which -> // The "which" argument contains the position of the selected item.
            val markerLatLng = likelyPlaceLatLngs[which]
            var markerSnippet = likelyPlaceAddresses[which]
            if (likelyPlaceAttributions[which] != null) {
                markerSnippet = """
                $markerSnippet
                ${likelyPlaceAttributions[which]}
                """.trimIndent()
            }

            // Add a marker for the selected place, with an info window
            // showing information about that place.
            this.mMap?.addMarker(MarkerOptions()
                .title(likelyPlaceNames[which])
                .position(markerLatLng!!)
                .snippet(markerSnippet))


            // Position the map's camera at the location of the marker.
            this.mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                DEFAULT_ZOOM.toFloat()))
        }

        // Display the dialog.
        AlertDialog.Builder(requireActivity())
            .setTitle(R.string.pick_place)
            .setItems(likelyPlaceNames, listener)
            .show()
    }

    companion object {
        private val TAG = MapFragment::class.java.simpleName
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        // Used for selecting the current place.
        private const val M_MAX_ENTRIES = 5
    }
}