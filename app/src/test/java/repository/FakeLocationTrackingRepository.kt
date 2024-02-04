package repository

import com.example.gpstracker.repository.LocationTrackingRepository
import com.example.gpstracker.ui.track.LocationData
import com.google.android.gms.maps.model.LatLng

class FakeLocationTrackingRepository: LocationTrackingRepository() {

    private val coordinates = LatLng(0.0, 0.0)

    override suspend fun getCurrentLocation(callback: (LocationData?) -> Unit) {
        callback(LocationData(coordinates.latitude, coordinates.longitude))
    }
}