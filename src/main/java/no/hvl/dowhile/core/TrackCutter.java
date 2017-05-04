package no.hvl.dowhile.core;

import no.hvl.dowhile.utility.TrackTools;
import org.alternativevision.gpx.beans.Track;
import org.alternativevision.gpx.beans.Waypoint;

import java.util.ArrayList;
import java.util.Date;

/**
 * Processing a GPX file. Removing unnecessary data.
 */
public class TrackCutter {
    private final OperationManager OPERATION_MANAGER;
    private GpxFile gpxFile;
    private TrackInfo trackInfo;

    /**
     * Constructor taking the current OperationManager instance to get info from it.
     */
    public TrackCutter(final OperationManager OPERATION_MANAGER) {
        this.OPERATION_MANAGER = OPERATION_MANAGER;
    }

    /**
     * Processing the file to remove unnecessary data.
     */
    public void process() {
        filterOnTimeStarted(OPERATION_MANAGER.getOperation().getStartTime());
    }

    /**
     * Determines a maximum latitude and longitude, and uses to create a radius
     */
    public void setRadius() {

    }

    /**
     * Sets a time span for the track
     */
    private void setTimeSpan() {

    }

    /**
     * Removes all track points that are outside of the given radius
     */
    private void filterOnRadius() {

    }

    /**
     * Removes all track points that were created before a given time
     */
    public void filterOnTimeStarted(Date startTime) {
        Track track = TrackTools.getTrackFromGPXFile(gpxFile.getGpx());
        ArrayList<Waypoint> trackPoints = track.getTrackPoints();
        ArrayList<Waypoint> pointsToRemove = new ArrayList<>();
        long startTimeMillis = startTime.getTime();

        for (Waypoint waypoint : trackPoints) {
            long pointTimeMillis = waypoint.getTime().getTime();
            if (pointTimeMillis < startTimeMillis) {
                pointsToRemove.add(waypoint);
            }
        }
        trackPoints.removeAll(pointsToRemove);
        track.setTrackPoints(trackPoints);
    }

    /**
     * Get the current GpxFile.
     *
     * @return the current GpxFile.
     */
    public GpxFile getGpxFile() {
        return gpxFile;
    }

    /**
     * Set the current GpxFile.
     *
     * @param gpxFile the current GpxFile to be set.
     */
    public void setGpxFile(GpxFile gpxFile) {
        this.gpxFile = gpxFile;
    }

    /**
     * Get info about the current track.
     * @return info about the current track.
     */
    public TrackInfo getTrackInfo() {
        return trackInfo;
    }

    /**
     * Set info about the current track.
     * @param trackInfo info about the current track.
     */
    public void setTrackInfo(TrackInfo trackInfo) {
        this.trackInfo = trackInfo;
    }
}