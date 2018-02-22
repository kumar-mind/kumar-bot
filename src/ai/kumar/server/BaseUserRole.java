
package ai.kumar.server;

public enum BaseUserRole {

    ANONYMOUS,   // everyone who is not logged in
    USER,        // users who have logged in
    PRIVILEGED,  // users with special rights, i.e. moderators
    ADMIN        // maximum right, that user is allowed to do everything
}
