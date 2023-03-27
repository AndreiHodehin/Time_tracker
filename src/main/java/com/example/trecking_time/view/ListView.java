package com.example.trecking_time.view;

import com.example.trecking_time.entity.Activity;
import com.example.trecking_time.entity.ResultingInfo;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Route("")
public class ListView extends VerticalLayout {
    private final List<String> taskNamesList = new ArrayList<>();
    {
        taskNamesList.add("Writing");
        taskNamesList.add("Speaking");
        taskNamesList.add("Reading");
    }
    private LocalDate currentDay;
    private Grid<Activity> grid ;
    private Grid<ResultingInfo> resultGrid ;
    ComboBox<String> activities;
    private final List<Activity> activityList;
    private final List<ResultingInfo> resultingInfos;


    public ListView() {
        activityList = new ArrayList<>();
        resultingInfos = new ArrayList<>();
        currentDay = LocalDate.now();


        createHeader();
        createWorkingPanel();
        createTotalResult();
        createRecordsGrid();
    }


    private void createWorkingPanel() {

        activities = new ComboBox<>("Choose activity");
        activities.setItems(taskNamesList);

        TextField durationField = new TextField();
        durationField.setValue("2.0");
        durationField.setMaxWidth(100, Unit.PIXELS);

        Notification notif = new Notification();
        notif.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notif.setDuration(3000);
        notif.setPosition(Notification.Position.TOP_CENTER);

        Button startBtn = new Button("Start");
        startBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        startBtn.addClickListener(click -> {
            String actionName = activities.getValue();
            Double duration = Double.valueOf(durationField.getValue());
            if(actionName == null) {
                showNotification(notif,"Choose activity");
            } else if(activityInAction(actionName)){
                showNotification(notif,"Activity in progress");
            } else {
                Activity newActivity = new Activity(
                        actionName,
                        LocalDate.now(),
                        LocalTime.now(),
                        null,
                        duration,
                        true
                );
                activityList.add(newActivity);
                updateGrid();
            }
        });

        Button stopBtn = new Button("Stop");
        stopBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

        stopBtn.addClickListener(click -> {
            String actionName = activities.getValue();
            if(actionName.isEmpty()) {
                showNotification(notif,"Choose activity");
            } else {
               Activity activity = findActivity(actionName,currentDay);
                if(activity != null) {
                    if(!activity.isInAction()) {
                        showNotification(notif,"Activity not in action");
                    } else {
                        activity.setEndTime(LocalTime.now());
                        activity.setInAction(false);
                        updateResultGrid();
                    }
                } else {
                    showNotification(notif,"Activity have not been started");
                }
                updateGrid();
            }
        });

        TextField anotherTaskFill = new TextField();
        anotherTaskFill.setPlaceholder("Add new task");

        Button addTask = new Button("Add task");
        addTask.addClickListener(click -> {
            String taskName = anotherTaskFill.getValue();
            if(taskName != null) {
               refreshActivityBox(taskName);
            }
        });

        HorizontalLayout panel = new HorizontalLayout();
        panel.add(activities,durationField,startBtn,stopBtn,anotherTaskFill,addTask);
        panel.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        panel.setAlignItems(Alignment.END);

        add(panel);
    }



    private void createHeader() {
        H2 logo = new H2("Tracking time system");
        H4 date = new H4(LocalDate.now().format(DateTimeFormatter.ISO_DATE));


        HorizontalLayout top = new HorizontalLayout(logo,date);
        top.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        top.expand(logo);
        top.setWidthFull();
        top.addClassNames(
                LumoUtility.Padding.NONE,
                LumoUtility.Padding.MEDIUM,
                LumoUtility.Background.CONTRAST_5,
                LumoUtility.Border.ALL,
                LumoUtility.BorderColor.CONTRAST_40,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.BoxShadow.SMALL);

        add(top);
    }

    private void createRecordsGrid() {
        grid = new Grid<>(Activity.class);
        grid.setColumns("name","day","startTime","endTime","expectationDuration","inAction");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setItems(activityList);
        grid.setAllRowsVisible(true);

        add(grid);
    }

    private void createTotalResult() {
        resultGrid = new Grid<>(ResultingInfo.class);

        resultGrid.setColumns("taskName", "totalDuration","success");
        resultGrid.getColumns().forEach(col -> col.setAutoWidth(true));

        resultGrid.setItems(resultingInfos);
        resultGrid.setAllRowsVisible(true);
        add(resultGrid);
    }

    private void fillResultInfos() {

        resultingInfos.clear();
        Map<String, List<Activity>> activityMap = activityList.stream().collect(Collectors.groupingBy(Activity::getName));

        for (Map.Entry<String,List<Activity>> set:activityMap.entrySet()) {
            String taskName = set.getKey();
            Duration totalDuration = Duration.ZERO;
            for (Activity act:set.getValue()) {
                if(act.getEndTime() != null && act.getStartTime() != null) {
                    Duration currentDuration = Duration.between(act.getStartTime(), act.getEndTime());
                    totalDuration = totalDuration.plus(currentDuration);
                }
            }
            Double avgTarget = set.getValue().stream().collect(Collectors.averagingDouble(Activity::getExpectationDuration));
            String success;
            if(totalDuration.toHours() >= avgTarget) {
                success = "Well done";
            } else {
                success = "Bad";
            }
            resultingInfos.add(new ResultingInfo(taskName,totalDuration,success));
        }

    }

    private Activity findActivity(String actionName, LocalDate currentDay) {
        List<Activity> activities = activityList.stream()
                .filter(activity -> activity.getDay().equals(currentDay))
                .filter(activity -> activity.getName().equals(actionName))
                .toList();
        if(activities.isEmpty()) {
            return null;
        } else {
            return activities.get(activities.size()-1);
        }
    }

    private boolean activityInAction(String actionName) {
       Activity activity = findActivity(actionName,currentDay);
        if(activity != null) {
            return activity.isInAction();
        }
        return false;
    }

    private void updateGrid() {
        grid.setItems(activityList);
    }

    private void updateResultGrid() {
        fillResultInfos();
        resultGrid.setItems(resultingInfos);
    }
    private void showNotification(Notification notif,String msg) {
        notif.setText(msg);
        notif.open();
    }
    private void refreshActivityBox(String taskName) {
        activities.clear();
        taskNamesList.add(taskName);
        activities.setItems(taskNamesList);
    }

}
