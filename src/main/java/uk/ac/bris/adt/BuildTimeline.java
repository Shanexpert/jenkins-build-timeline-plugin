package uk.ac.bris.adt;

import java.util.List;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.ManagementLink;
import hudson.model.RootAction;
import jenkins.model.Jenkins;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.bind.JavaScriptMethod;

import static java.util.stream.Collectors.toList;

@Extension
@Symbol("build-timeline")
public class BuildTimeline implements RootAction {
  @Override
  public String getIconFileName() {
    return "notepad.png";
  }

  @Override
  public String getUrlName() {
    return "build-timeline";
  }

  public String getDisplayName() {
    return "Build Timeline";
  }

  @JavaScriptMethod
  public List<Project> getData(
      final String startDate,
      final String endDate,
      final String minDurationMinutes
  ) {
    final BuildFilter buildFilter = new BuildFilter(startDate, endDate, minDurationMinutes);
    List<Project> projects =
        Jenkins.get().getAllItems(AbstractProject.class).stream()
            .map(x -> new Project(x, buildFilter))
            .filter(x -> !x.getBuilds().isEmpty())
            .collect(toList());
    System.out.println(projects);
    return projects;
  }

}
