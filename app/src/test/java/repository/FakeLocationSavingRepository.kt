package repository

import com.example.gpstracker.repository.LocationSavingRepository
import com.example.gpstracker.ui.track.LocationData
import com.google.android.gms.maps.model.LatLng

class FakeLocationSavingRepository: LocationSavingRepository() {

    var locationsSaved = mutableListOf<LocationData>()
    private val coordinates = LatLng(0.0, 0.0)

    override suspend fun saveLocationToFirebase() {
        locationsSaved.add(LocationData(coordinates.latitude, coordinates.longitude))
    }

    override suspend fun isLocationSavedFromRoomDatabase(
        latitude: Double,
        longitude: Double,
        time: Long
    ): Boolean {
        locationsSaved.add(LocationData(coordinates.latitude, coordinates.longitude))
        return true
    }
}