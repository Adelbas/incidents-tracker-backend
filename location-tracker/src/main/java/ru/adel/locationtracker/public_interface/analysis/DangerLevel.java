package ru.adel.locationtracker.public_interface.analysis;

/**
 * Degree of danger of a negative event.
 * <p>
 * Ordered from the least to the most dangerous so that the natural enum order can be used
 * for comparisons and escalation logic (introduced for clustering in iteration 2).
 */
public enum DangerLevel {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}
