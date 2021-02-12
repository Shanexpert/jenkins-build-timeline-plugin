package uk.ac.bris.adt;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Class to represent values used to filter builds/runs.
 */
public class BuildFilter {

  /**
   * Minimum duration of run.
   */
  private final int minDurationMin;

  /**
   * Start datetime of run.
   */
  private final LocalDateTime start;

  /**
   * End datetime of run.
   */
  private final LocalDateTime end;

  public BuildFilter(
      final String start,
      final String end,
      final String minDurationMin
  ) {
    this.start = LocalDateTime.parse(start);
    this.end = LocalDateTime.parse(end);
    this.minDurationMin = Integer.parseInt(minDurationMin);
  }

  /**
   * @link #minDurationMin
   */
  public int getMinDurationMin() {
    return minDurationMin;
  }

  public int getMinDurationMillis() {
    return minDurationMin * 60 * 1000;
  }

  /**
   * @link #start
   */
  public LocalDateTime getStart() {
    return start;
  }

  public long getStartMillis() {
    return start.toEpochSecond(ZoneOffset.UTC) * 1000;
  }

  /**
   * @link #end
   */
  public LocalDateTime getEnd() {
    return end;
  }

  public long getEndMillis() {
    return end.toEpochSecond(ZoneOffset.UTC) * 1000;
  }
}
