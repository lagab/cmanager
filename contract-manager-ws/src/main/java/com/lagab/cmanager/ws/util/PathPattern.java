package com.lagab.cmanager.ws.util;

/**
 * @author gabriel
 * @since 26/07/2018.
 */
public class PathPattern {
    /**
     * Entity id pattern.
     */
    public static final String ID_PATTERN = "[0-9]+";

    /**
     * Entity name pattern.
     */
    public static final String NAME_PATTERN = "\\D[\\w-]*[\\w]";

    /**
     * Default constructor.
     */
    private PathPattern() {
        // Refers to static patterns.
    }

}
