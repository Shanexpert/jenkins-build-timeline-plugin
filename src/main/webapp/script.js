const DATE_FORMAT = "YYYY-MM-DD"
const TIME_FORMAT = "HH:mm"

var urlParams = new URLSearchParams(window.location.search);

var defaultStart = moment().subtract(1, 'days');
var defaultEnd = moment()

var startDateEl = getEl("startDate") 
var startTimeEl = getEl("startTime") 
var endDateEl = getEl("endDate") 
var endTimeEl = getEl("endTime") 
var minDurationEl = getEl("minDuration")
var containerEl = getEl('visualization');
var loadingEl = getEl('loading');
var visualizationEl = getEl("visualization")

var startPicker = new Pikaday({ field: startDateEl });
var endPicker = new Pikaday({ field: endDateEl });


setDefaults(startDateEl, defaultStart.format(DATE_FORMAT))
setDefaults(startTimeEl, defaultStart.format(TIME_FORMAT))
setDefaults(endDateEl, defaultEnd.format(DATE_FORMAT))
setDefaults(endTimeEl, defaultEnd.format(TIME_FORMAT))
setDefaults(minDurationEl, 5)

var timeline = new vis.Timeline(containerEl);

timeline.on("click", function (event) {
  var props = timeline.getEventProperties(event)
  if (props.event.item) {
    window.open("../job/" + props.event.item.replaceAll("/","/job/").replaceAll("#","/") + "/console");
  }
})

timeline.on("rangechange", function(data) {
  startDateEl.value = moment(data.start).format(DATE_FORMAT);
  startTimeEl.value = moment(data.start).format(TIME_FORMAT);
  endDateEl.value = moment(data.end).format(DATE_FORMAT);
  endTimeEl.value = moment(data.end).format(TIME_FORMAT);
})

updateTimeline()

/**
 * Sets the value of the given element to the URL parameter with that name, or
 * if that is empty, sets the supplied default value.
 *
 * @param name
 * @param defaultValue
 */
function setDefaults(el, defaultValue) {
  if (urlParams.get(el.id)) {
    el.value = urlParams.get(el.id)
  } else {
    el.value = defaultValue
  }
}

function getEl(name) {
  return document.getElementById(name);
}

function updateTimeline() {
  loadingEl.style.display="inline"
  visualizationEl.addClassName("blur")
  
  var startString = startDateEl.value + "T" + startTimeEl.value + ":00"
  var endString = endDateEl.value + "T" + endTimeEl.value + ":00"
  
  var urlParams = new URLSearchParams()
  urlParams.append("startDate", startDateEl.value);
  urlParams.append("startTime", startTimeEl.value);
  urlParams.append("endDate", endDateEl.value);
  urlParams.append("endTime", endTimeEl.value);
  urlParams.append("minDuration", minDurationEl.value);
  window.history.replaceState({}, '', location.pathname + "?" + urlParams);

  timeline.setOptions({
    groupOrder: function (a, b) {
      return a.value - b.value;
    },
    orientation: 'both',
    cluster: {
      maxItems: 6,
      titleTemplate: " "
    },
    stack: false,
    start: startString,
    end: endString
  });


  jenkins.getData(startString, endString, minDurationEl.value, function (data) {
    loadingEl.style.display="none"
    visualizationEl.removeClassName("blur")
    groupsList= [];
    itemsList = []
    data.responseObject().forEach(project => {
     
      groupsList.push({
        content: project.name,
        id: project.name
      })
     
      project.builds.forEach(build => {
        itemsList.push({
          start: build.actualStart,
          end: build.end,
          group: project.name,
          content: "",
          id: project.name + "#" + build.id,
          className: build.result,
          title: "#" + build.id
            + "<br />Status: " + build.result
           + "<br/>Start: " + moment(build.actualStart).format("YYYY-MM-DD HH:mm:ss") +
           "<br/>Duration: " + formatDuration(moment.duration(moment(build.end).diff(build.actualStart)))
        })
      })

    })

    timeline.setGroups(new vis.DataSet(groupsList));
    timeline.setItems(new vis.DataSet(itemsList));
  })
}

/**
 * Utility function to format a moment duration as human readable.
 * Surprisngly this isn't built into moment.
 *
 * @param duration
 */
function formatDuration(duration) {
  let parts = [];

  // return nothing when the duration is falsy or not correctly parsed (P0D)
  if(!duration || duration.toISOString() === "P0D") return;

  if(duration.years() >= 1) {
      const years = Math.floor(duration.years());
      parts.push(years+" "+(years > 1 ? "years" : "year"));
  }

  if(duration.months() >= 1) {
      const months = Math.floor(duration.months());
      parts.push(months+" "+(months > 1 ? "months" : "month"));
  }

  if(duration.days() >= 1) {
      const days = Math.floor(duration.days());
      parts.push(days+" "+(days > 1 ? "days" : "day"));
  }

  if(duration.hours() >= 1) {
      const hours = Math.floor(duration.hours());
      parts.push(hours+" "+(hours > 1 ? "hrs" : "hr"));
  }

  if(duration.minutes() >= 1) {
      const minutes = Math.floor(duration.minutes());
      parts.push(minutes+" "+(minutes > 1 ? "mins" : "min"));
  }

  if(duration.seconds() >= 1) {
      const seconds = Math.floor(duration.seconds());
      parts.push(seconds+" "+(seconds > 1 ? "secs" : "sec"));
  }

  return parts.join(", ");
}
