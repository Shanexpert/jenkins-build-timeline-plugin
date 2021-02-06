package uk.ac.bris.adt;

import java.util.List;

import hudson.model.AbstractProject;
import hudson.util.RunList;

import static java.util.stream.Collectors.toList;

public class Project {

  private String name;
  private List<Build> builds;

  public Project(final AbstractProject<?, ?> abstractProject, BuildFilter buildFilter) {
    this.name = abstractProject.getFullName();
    RunList<?> runs = abstractProject.getBuilds();
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
