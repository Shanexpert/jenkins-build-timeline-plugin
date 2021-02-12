package uk.ac.bris.adt;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import hudson.model.Run;

/**
 * Model object to represent a build.  This is simpler than using the Jenkins Run object
 * directly.  Properties are all strings to make it simpler when this is JSONified for passing
 * to the client, and we can ensure dates are in standard and expected format.
 */
public class Build {

  /**
   * ID of the run.
   */
  private final String id;

  /**
   * Datetime the run was expected to start, in ISO8601 format.
   */
  private final String expectedStart;

  /**
   * Datetime the run actually started, in ISO8601 format.
   */
  private final String actualStart;

  /**
   * Datetime the run ended, in ISO8601 format.
   */
  private final String end;

  /**
   * Result of run as a string.  See {@link hudson.model.Result}
   */
  private final String result;

  /**
   * Construct object, using data from the supplied Run object.
   *
   * @param run Jenkins Run object used to construct this object.
   */
  public Build(final Run run) {
    this.id = run.getId();
    this.expectedStart = timestampToISO8601(run.getTimeInMillis());
    this.actualStart = timestampToISO8601(run.getStartTimeInMillis());
    this.end = timestampToISO8601(run.getStartTimeInMillis() + run.getDuration());
    this.result = run.getResult().toString().toLowerCase();

  }

  private String timestampToISO8601(final long timestamp) {
    return LocalDateTime.ofInstant(
        Instant.ofEpochMilli(timestamp),
        TimeZone.getDefault().toZoneId()
    ).format(DateTimeFormatter.ISO_DATE_TIME);
  }

  /**
   * @link #id
   */
  public String getId() {
    return id;
  }

  /**
   * @link #expectedStartDateTime
   */
  public String getExpectedStart() {
    return expectedStart;
  }

  /**
   * @link #actualStartDateTime
   */
  public String getActualStart() {
    return actualStart;
  }

  /**
   * @link #endDateTime
   */
  public String getEnd() {
    return end;
  }

  /**
   * @link #result
   */
  public String getResult() {
    return result;
  }
}
