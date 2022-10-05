package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.enums.Status;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/task")
public class TaskController {

    private final UserService userService;
    private final ProjectService projectService;
    private final TaskService taskService;

    public TaskController(UserService userService, ProjectService projectService, TaskService taskService) {
        this.userService = userService;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @GetMapping("/create")
    public String createTask(Model model) {
    model.addAttribute("task", new TaskDTO());
    model.addAttribute("projectList", projectService.findAll());
    model.addAttribute("employeeList", userService.findEmployee());
    model.addAttribute("taskList", taskService.findAll());

        return "/task/create";
    }

    @PostMapping("/create")
    public String saveTask(@ModelAttribute("task") TaskDTO task){
        taskService.save(task);
        System.out.println(task);
        return "redirect:/task/create";
    }

    @GetMapping("/update/{id}")
    public String updateTask(@PathVariable("id") Long id, Model model){
        model.addAttribute("task", taskService.findById(id));
        model.addAttribute("projectList", projectService.findAll());
        model.addAttribute("employeeList", userService.findEmployee());
        model.addAttribute("taskList", taskService.findAll());

        return "/task/update";
    }

//    @PostMapping("/update")
//    public String updatesForTask(@ModelAttribute("task") TaskDTO task){
//
//        taskService.update(task);
//        return "redirect:/task/create";
//    }

//    @PostMapping("/update/{taskId}")
//    public String updatesForTask(@PathVariable("taskId") Long id, @ModelAttribute("task") TaskDTO task){
//        task.setId(id);
//        taskService.update(task);
//        return "redirect:/task/create";
//    }

    @PostMapping("/update/{id}")
    public String updatesForTask( TaskDTO task){
        taskService.update(task);
        return "redirect:/task/create";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        taskService.deleteById(id);
        return "redirect:/task/create";
    }

    @GetMapping("/employee/pending-tasks")
    public String employeePendingTasks(Model model){
        model.addAttribute("tasks", taskService.findAllTasksByStatusIsNot(Status.COMPLETE));
        return "/task/pending-tasks";
    }

    @GetMapping("/employee/archive")
    public String employeeArchivedTasks(Model model){
        model.addAttribute("tasks", taskService.findAllTasksByStatus(Status.COMPLETE));
        return "/task/archive";
    }
    @GetMapping("/employee/edit/{id}")
    public String employeeEditTask (@PathVariable Long id, Model model){
        model.addAttribute("task", taskService.findById(id));
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("employees", userService.findEmployee());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("tasks", taskService.findAllTasksByStatusIsNot(Status.COMPLETE));

        return "/task/status-update";
    }

    @PostMapping("/employee/update/{id}")
    public String employeeUpdateTask(TaskDTO task){

        taskService.updateStatus(task);


        return "redirect:/task/employee/pending-tasks";
    }
}

