package ai.kumar.geo;

public abstract class AbstractGeoPoint implements GeoPoint {

    /**
     * compute the distance between two points using the Haversine Algorithm
     * https://en.wikipedia.org/wiki/Haversine_formula
     * @param othr the other point
     * @return the distance of this point and the other point in meter
     */
    public double distance(final GeoPoint othr) {
        return distance(this.lat(), this.lon(), othr.lat(), othr.lon());
    }
    
    public static double distance(final double lat1, final double lon1, final double lat2, final double lon2) {        
        double dlat = (lat2 - lat1) * D2R; double dlon = (lon2 - lon1) * D2R;
        double a = Math.pow(Math.sin(dlat / 2.0d), 2.0d) +
                   Math.cos(lat1 * D2R) * Math.cos(lat2 * D2R) * Math.pow(Math.sin(dlon / 2.0d), 2.0d);
        double c = 2.0d * Math.atan2(Math.sqrt(a), Math.sqrt(1.0d - a));
        return EQUATOR_EARTH_RADIUS * c;
    }

    static final double EQUATOR_EARTH_RADIUS = 6378137.0d;
    static final double D2R = (Math.PI / 180.0d);

}
