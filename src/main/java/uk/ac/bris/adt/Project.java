package uk.ac.bris.adt;

import java.util.List;

import hudson.model.Job;
import hudson.util.RunList;

import static java.util.stream.Collectors.toList;

/**
 * lass to represent a Project, which we JSONify and pass to the JS client.
 */
public class Project {

  private final String name;

  private final List<Build> builds;

  /**
   * Constructed from a Job, and builds are filtered with the supplied buildFilter.
   * @param job
   * @param buildFilter
   */
  public Project(final Job<?, ?> job, BuildFilter buildFilter) {
    this.name = job.getFullName();
    RunList<?> runs = job.getBuilds();
    this.builds = runs.stream()
        .filter(run -> run.getStartTimeInMillis() > buildFilter.getStartMillis())
        .filter(run -> run.getStartTimeInMillis() + run.getDuration() < buildFilter.getEndMillis())
        .filter(run -> run.getDuration() > buildFilter.getMinDurationMillis())
        .map(Build::new)
        .collect(toList());
  }

  public String getName() {
    return name;
  }

  public List<Build> getBuilds() {
    return builds;
  }

}
