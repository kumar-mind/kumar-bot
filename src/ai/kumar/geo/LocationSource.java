package ai.kumar.geo;

public enum LocationSource {
    
    USER,       // the (loklak) user has set the location, this is a hint that this is a rich tweet.
    REPORT,     // location came from another source in identical way. This may be a IoT import.
    PLACE,      // location came from translation of the given place name, which is in the context of messages an invisible meta-information.
    ANNOTATION; // location was detected and annotated from visible text content
    
}
