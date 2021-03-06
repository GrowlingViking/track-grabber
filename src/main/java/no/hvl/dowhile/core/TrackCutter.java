package no.hvl.dowhile.core;

import com.hs.gpxparser.modal.Track;
import com.hs.gpxparser.modal.TrackSegment;
import com.hs.gpxparser.modal.Waypoint;
import no.hvl.dowhile.utility.TrackTools;

import java.util.ArrayList;
import java.util.Date;

/**
 * Processes a GPX file by removing unnecessary data.
 */
public class TrackCutter {
    private final OperationManager OPERATION_MANAGER;
    private GpxFile gpxFile;
    private TrackInfo trackInfo;

    /**
     * Constructor taking the current OperationManager instance to get info from it.
     *
     * @param OPERATION_MANAGER the TrackCutter's OperationManager.
     */
    public TrackCutter(final OperationManager OPERATION_MANAGER) {
        this.OPERATION_MANAGER = OPERATION_MANAGER;
    }

    /**
     * Processes the file by removing unnecessary data.
     */
    void process() {
        filterOnTimeStarted(OPERATION_MANAGER.getOperation().getStartTime());
    }

    /**
     * Removes all track points that were created before a given time.
     *
     * @param startTime The start time of the operation.
     */
    public void filterOnTimeStarted(Date startTime) {
        Track track = TrackTools.getTrackFromGPXFile(gpxFile.getGpx());
        ArrayList<Waypoint> trackPoints = TrackTools.getAllTrackPoints(track);
        ArrayList<Waypoint> pointsToRemove = new ArrayList<>();
        long startTimeMillis = startTime.getTime();

        for (Waypoint waypoint : trackPoints) {
            long pointTimeMillis = waypoint.getTime().getTime();
            if (pointTimeMillis < startTimeMillis) {
                pointsToRemove.add(waypoint);
            }
        }
        trackPoints.removeAll(pointsToRemove);
        TrackSegment trackSegment = new TrackSegment();
        trackSegment.setWaypoints(trackPoints);
        ArrayList<TrackSegment> trackSegments = new ArrayList<>();
        trackSegments.add(trackSegment);
        track.setTrackSegments(trackSegments);
    }

    /**
     * Gets the current GpxFile.
     *
     * @return the current GpxFile.
     */
    public GpxFile getGpxFile() {
        return gpxFile;
    }

    /**
     * Sets the current GpxFile.
     *
     * @param gpxFile the current GpxFile to be set.
     */
    public void setGpxFile(GpxFile gpxFile) {
        this.gpxFile = gpxFile;
    }

    /**
     * Sets info about the current track.
     *
     * @param trackInfo info about the current track.
     */
    public void setTrackInfo(TrackInfo trackInfo) {
        this.trackInfo = trackInfo;
    }
}
