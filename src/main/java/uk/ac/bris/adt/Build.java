package uk.ac.bris.adt;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import hudson.model.Run;

public class Build {

  private final String id;

  private final String expectedStart;

  private final String actualStart;

  private final String end;

  private final String result;


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
