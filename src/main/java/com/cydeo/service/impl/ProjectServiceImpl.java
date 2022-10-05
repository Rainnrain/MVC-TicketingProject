package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.Status;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl extends  AbstractMapService <ProjectDTO, String> implements ProjectService {

    private final TaskService taskService;

    public ProjectServiceImpl(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public ProjectDTO save(ProjectDTO project) {
        if(project.getProjectStatus()==null)project.setProjectStatus(Status.OPEN);
        super.save(project.getProjectCode(), project);
        return project;
    }

    @Override
    public ProjectDTO findById(String projectCode) {
        return super.findById(projectCode);

    }

    @Override
    public List<ProjectDTO> findAll() {
        return super.findAll();
    }

    @Override
    public void deleteById(String projectCode) {
         super.deleteById(projectCode);
    }

    @Override
    public void update(ProjectDTO object) {

        if(object.getProjectStatus()==null)
            object.setProjectStatus(findById(object.getProjectCode()).getProjectStatus());

        super.update(object.getProjectCode(), object);
    }

    @Override
    public void complete(ProjectDTO project) {
        project.setProjectStatus(Status.COMPLETE);
    }

    public List<ProjectDTO> getCountedListOfProjectDTO(UserDTO manager){
        List<ProjectDTO> projects=
            findAll().stream()
           .filter(p->p.getAssignedManager().equals(manager))
                    .map(project->{

                        List<TaskDTO> taskList= taskService.findTasksByManager(manager);

                        int completedTaskCounts=(int) taskList.stream().filter(p->p.getProject().equals(project) && p.getTaskStatus()==Status.COMPLETE).count();
                        int unfinishedTaskCounts=(int)taskList.stream().filter(p->p.getProject().equals(project) && p.getTaskStatus()!=Status.COMPLETE).count();
                        project.setCompleteTaskCounts(completedTaskCounts);
                        project.setUnfinishedTaskCounts(unfinishedTaskCounts);

                        return project;
                    }).collect(Collectors.toList());




        return projects;
    }
}
