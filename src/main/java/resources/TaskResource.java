package resources;

import domain.tasks.TaskService;
import dto.TaskCreateDTO;
import dto.TaskUpdateDTO;
import exceptions.InvalidArgumentsException;
import exceptions.NotFoundException;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/tasks")
public class TaskResource {
    private final TaskService taskService;

    @Inject
    public TaskResource(TaskService taskService) {
        this.taskService = taskService;
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response all() {
        return Response.ok(taskService.getAll()).build();
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response view(@PathParam("id") Integer id) {
        try {
            return Response.ok(taskService.findOne(id)).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(e).build();
        }
    }

    @POST
    @Valid
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@Valid TaskCreateDTO dto) {
        try {
            taskService.create(dto);
            return Response.noContent().status(201).build();
        } catch (InvalidArgumentsException e) {
            return Response.status(400).entity(e).build();
        }
    }

    @PATCH
    @Valid
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Integer id, @Valid TaskUpdateDTO dto) {
        try {
            taskService.update(id, dto);
            return Response.noContent().status(200).build();
        } catch (NotFoundException e) {
            return Response.status(400).entity(e).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Integer id) {
        try {
            taskService.remove(id);
            return Response.noContent().status(200).build();
        } catch (NotFoundException e) {
            return Response.status(404).entity(e).build();
        }
    }
}
