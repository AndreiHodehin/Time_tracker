package com.example.trecking_time.view;

import com.example.trecking_time.entity.dto.Activity;
import com.example.trecking_time.entity.dto.ResultingInfo;
import com.example.trecking_time.entity.Task;
import com.example.trecking_time.entity.dto.UserDto;
import com.example.trecking_time.service.interfaces.ActivityService;
import com.example.trecking_time.service.interfaces.TaskService;
import com.example.trecking_time.service.interfaces.UserService;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.util.stream.Collectors;

@Route("")
@Component
@Scope("prototype")
@PageTitle("Tracking time")
@PermitAll
public class ListView extends VerticalLayout {
    private final transient AuthenticationContext authContext;
    private final ActivityService activityService;
    private final TaskService taskService;
    private final UserService userService;
    private LocalDate currentDay;
    private Grid<Activity> activityGrid;
    private Grid<ResultingInfo> resultGrid ;
    private ComboBox<String> activitiesBox;
    private DatePicker datePicker;
    private List<Activity> activityList;
    private final List<String> taskNamesList = new ArrayList<>();
    private final List<ResultingInfo> resultingInfos;
    private GridSortOrder<Activity> order;

    private final UserDto user;


    public ListView(ActivityService activityService,
                    TaskService taskService,
                    UserService userService,
                    AuthenticationContext authContext) {
        this.authContext = authContext;
        this.activityService = activityService;
        this.taskService = taskService;
        this.userService = userService;
        this.user = userService.getUserByName(authContext);

        createDatePicker();

        activityList = activityService.findAllActivityByDayAndUserId(currentDay, user.getId());
        taskNamesList.addAll(taskService.findAllTasks().stream()
                .map(Task::getName)
                .toList());
        resultingInfos = new ArrayList<>();

        fillResultInfos();
        createHeader();
        createWorkingPanel();
        createTotalResult();
        createDatePickerPanel();
        createRecordsGrid();
    }

    private void createHeader() {
        H2 logo = new H2("Tracking time system");
        H4 date = new H4(LocalDate.now().format(DateTimeFormatter.ISO_DATE));

        Button logout = new Button("Logout", click ->
                this.authContext.logout());


        HorizontalLayout top = new HorizontalLayout(logo,date,logout);
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

    private void createWorkingPanel() {

        activitiesBox = new ComboBox<>("Choose activity");
        activitiesBox.setItems(taskNamesList);

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
            String actionName = activitiesBox.getValue();
            Double duration = Double.valueOf(durationField.getValue());
            if(actionName == null) {
                showNotification(notif,"Choose activity");
            } else if(activityInAction(actionName)){
                showNotification(notif,"Activity in progress");
            } else {
                Activity newActivity = new Activity(
                        null,
                        actionName,
                        LocalDate.now(),
                        LocalTime.now(Clock.tickSeconds(ZoneId.systemDefault())),
                        null,
                        duration,
                        true,
                        user.getId()
                );
                activityService.addActivity(newActivity);
                refreshListsDueToDay(currentDay, user.getId());
            }
        });

        Button stopBtn = new Button("Stop");
        stopBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

        stopBtn.addClickListener(click -> {
            String actionName = activitiesBox.getValue();
            if(actionName == null) {
                showNotification(notif,"Choose activity");
            } else {
               Activity activity = findActivity(actionName,currentDay);
                if(activity != null) {
                    if(!activity.isInAction()) {
                        showNotification(notif,"Activity not in action");
                    } else {
                        activity.setEndTime(LocalTime.now(Clock.tickSeconds(ZoneId.systemDefault())));
                        activity.setInAction(false);
                        activityService.updateActivity(activity);
                        refreshListsDueToDay(currentDay,user.getId());
                    }
                } else {
                    showNotification(notif,"Activity have not been started");
                }
            }
        });

        TextField anotherTaskFill = new TextField();
        anotherTaskFill.setPlaceholder("Add new task");

        Button addTask = new Button("Add task");
        addTask.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addTask.addClickListener(click -> {
            String taskName = anotherTaskFill.getValue();
            anotherTaskFill.clear();
            if(taskName != null) {
                taskNamesList.add(taskName);
                taskService.createTask(new Task(taskName));
                refreshActivityBox();
            }
        });

        Button deleteTask = new Button("Delete task");
        deleteTask.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteTask.addClickListener(click -> {
            String taskName = activitiesBox.getValue();
            if(taskName != null) {
                taskNamesList.remove(taskName);
                taskService.deleteTaskByName(taskName);
                refreshActivityBox();
            }
        });

        HorizontalLayout panel = new HorizontalLayout();
        panel.add(activitiesBox,durationField,startBtn,stopBtn,anotherTaskFill,addTask,deleteTask);
        panel.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        panel.setAlignItems(Alignment.END);

        add(panel);
    }

    private void createDatePickerPanel() {
        HorizontalLayout datePickerPanel = new HorizontalLayout(datePicker);

        Button chooseDate = new Button("Select day");
        chooseDate.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        chooseDate.addClickListener(click -> {
            LocalDate selectedDay = datePicker.getValue();
            refreshListsDueToDay(selectedDay, user.getId());
        });

        datePickerPanel.setAlignItems(Alignment.END);
        datePickerPanel.add(chooseDate);
        add(datePickerPanel);
    }
    private void createDatePicker() {

        datePicker = new DatePicker("Choose date");
        datePicker.setValue(LocalDate.now(Clock.tickSeconds(ZoneId.systemDefault())));
        currentDay = datePicker.getValue();

    }

    private void createRecordsGrid() {
        activityGrid = new Grid<>(Activity.class);

        activityGrid.setColumns("name","day","startTime","endTime","expectationDuration","inAction");
        activityGrid.getColumns().forEach(col -> col.setAutoWidth(true));
        Grid.Column<Activity> orderColumn = activityGrid.getColumnByKey("startTime");
        order = new GridSortOrder<>(orderColumn, SortDirection.DESCENDING);
        activityGrid.sort(List.of(order));
        activityGrid.setItems(activityList);
        activityGrid.setAllRowsVisible(true);

        add(activityGrid);
    }

    private void createTotalResult() {
        resultGrid = new Grid<>(ResultingInfo.class,false);

        resultGrid.setColumns("taskName", "totalDuration","success");
        resultGrid.getColumnByKey("taskName").setFooter("Total");
        resultGrid.getColumnByKey("totalDuration").setFooter(getAvgDuration());

        resultGrid.getColumns().forEach(col -> col.setAutoWidth(true));

        resultGrid.setItems(resultingInfos);
        resultGrid.setAllRowsVisible(true);

        add(resultGrid);
    }

    private String getAvgDuration() {
        long duration =  resultingInfos.stream().mapToLong(result -> result.getTotalDuration().toMinutes()).sum();
        return String.format("%d:%d",duration/60,duration%60);
    }

    private void fillResultInfos() {

        resultingInfos.clear();
        Map<String, List<Activity>> activityMap = activityList.stream()
                .collect(Collectors.groupingBy(Activity::getName));

        for (Map.Entry<String,List<Activity>> set:activityMap.entrySet()) {
            String taskName = set.getKey();
            Duration totalDuration = getTotalDuration(set);
            double avgTarget = set.getValue().stream().collect(Collectors.averagingDouble(Activity::getExpectationDuration)) * 60;
            double rate = totalDuration.toMinutes() / avgTarget;
            String success = String.format("Your rate: %.2f", rate);
            resultingInfos.add(new ResultingInfo(taskName,totalDuration,success));
        }

    }

    private Activity findActivity(String actionName, LocalDate selectedDay) {
        List<Activity> activities = activityList.stream()
                .filter(activity -> activity.getDay().equals(selectedDay))
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
        activityGrid.setItems(activityList);
        activityGrid.sort(List.of(order));
    }

    private void updateResultGrid() {
        fillResultInfos();
        resultGrid.getColumnByKey("totalDuration").setFooter(getAvgDuration());
        resultGrid.setItems(resultingInfos);
    }
    private void showNotification(Notification notif,String msg) {
        notif.setText(msg);
        notif.open();
    }

    private void refreshListsDueToDay(LocalDate selectedDay,Long id) {
        activityList = activityService.findAllActivityByDayAndUserId(selectedDay,id);
        updateGrid();
        updateResultGrid();
    }

    private void refreshActivityBox() {
        activitiesBox.clear();
        activitiesBox.setItems(taskNamesList);
    }

    private Duration getTotalDuration(Map.Entry<String, List<Activity>> set) {
        Duration totalDuration = Duration.ZERO;
        for (Activity act: set.getValue()) {
            if(act.getEndTime() != null && act.getStartTime() != null) {
                Duration currentDuration = Duration.between(act.getStartTime(), act.getEndTime());
                totalDuration = totalDuration.plus(currentDuration);
            }
        }
        return totalDuration;
    }
}
