package com.zagori.mediaviewer.interfaces;

/**
 * Interface used to format custom objects into an image url.
 */
public interface Formatter<T> {
    /**
     * Formats an image url representation of the object.
     *
     * @param t The object that needs to be formatted into url.
     * @return An url of image.
     */
    String format(T t);
}
