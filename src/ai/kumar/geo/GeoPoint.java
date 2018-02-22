package ai.kumar.geo;


/**
 * Geolocation storage may vary using different data structures for the points.
 * The reason to have different implementation is to save memory for the point storage.
 * With each version of a point storage comes a accuracy level which can be returned by the object.
 */
public interface GeoPoint {

    public static final double meter = 90.0d / 1.0e7d; // this is actually the definition of 'meter': 10 million meter shall be the distance from the equator to the pole

    /**
     * get the latitude of the point
     * @return
     */
    public double lat();

    /**
     * get the longitude of the point
     * @return
     */
    public double lon();

    /**
     * get the implementation-dependent accuracy of the latitude
     * @return
     */
    public double accuracyLat();

    /**
     * get the implementation-dependent accuracy of the longitude
     * @return
     */
    public double accuracyLon();

    /**
     * compute the hash code of a coordinate
     * this produces identical hash codes for locations that are close to each other
     */
    @Override
    public int hashCode();

    /**
     * equality test that is needed to use the class inside HashMap/HashSet
     */
    @Override
    public boolean equals(final Object o);

    /**
     * compute the distance between two points using the Haversine Algorithm
     * https://en.wikipedia.org/wiki/Haversine_formula
     * @param othr the other point
     * @return the distance of this point and the other point in meter
     */
    public double distance(final GeoPoint othr);
    
    /**
     * printout format of the point
     * @return
     */
    @Override
    public String toString();

}
