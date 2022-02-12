package org.ev.resources;

import com.google.gson.Gson;
import org.ev.db.Database;
import org.ev.domain.tasks.Task;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.*;

@Path("/tasks")
public class TaskResource {
    @GET
    @Path("/list")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response list() {
        String query = "select * from tasks";
        List<Task> tasks = new ArrayList<>();

        try {
            tasks = Database.select(query, Task.class);
        } catch (SQLException | ClassNotFoundException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try { Database.closeConnection(); } catch (SQLException e) {}
        }

        return Response.ok(new Gson().toJson(tasks), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") int id) {

        String query = "select * from tasks where id = " + id;
        List<Task> tasks = new ArrayList<>();

        try {
            tasks = Database.select(query, Task.class);
        } catch (SQLException | ClassNotFoundException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try { Database.closeConnection(); } catch (SQLException e) {}
        }

        if (tasks.size() == 0) {
            return Response.status(404)
                    .type(MediaType.APPLICATION_JSON)
                    .entity("{\"message\": \"Not found\"}")
                    .build();
        }

        return Response.ok(new Gson().toJson(tasks.get(0)), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(String json) {
        JSONObject jsonObject = new JSONObject(json);
        int result = 0;

        String query = "INSERT INTO tasks (title, description, finished) VALUES (?, ?, ?)";

        try {
            result = Database.execute(query, new Object[] {
                    jsonObject.get("title"),
                    jsonObject.get("description"),
                    jsonObject.get("finished")
            });
        } catch (SQLException | ClassNotFoundException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try { Database.closeConnection(); } catch (SQLException e) {}
        }

        if (result == 0)
            return Response.status(500)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(new JSONObject("{\"message\":\"Database error\"}").toString()).build();

        return Response.status(201)
                .type(MediaType.APPLICATION_JSON)
                .entity(new JSONObject("{\"result\":\"success\"}").toString())
                .build();
    }

    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(String json, @PathParam("id") int id) {
        JSONObject jsonObject = new JSONObject(json);
        int result = 0;

        if (jsonObject.has("id")) {
            jsonObject.remove("id");
        }

        if (jsonObject.length() == 0) {
            return Response.status(400)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(new JSONObject("{\"message\":\"Empty data\"}").toString()).build();
        }

        StringJoiner pairs = new StringJoiner(",");
        Object[] values = new Object[jsonObject.length()];
        Iterator<String> iterator = jsonObject.keys();
        int counter = 0;
        while (iterator.hasNext()) {
            String key = iterator.next();
            pairs.add(String.format("%s = ?", key));
            values[counter++] = jsonObject.get(key);
        }

        String query = String.format(
                "UPDATE tasks SET %s where id = %d",
                pairs.toString(),
                id
        );

        try {
            result = Database.execute(query, values);
        } catch (SQLException | ClassNotFoundException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try { Database.closeConnection(); } catch (SQLException e) {}
        }

        if (result == 0)
            return Response.status(500)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(new JSONObject("{\"message\":\"Database error\"}").toString()).build();

        return Response.status(200)
                .type(MediaType.APPLICATION_JSON)
                .entity(new JSONObject("{\"result\":\"success\"}").toString())
                .build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        int result = 0;

        String query = "DELETE FROM tasks where id = ?";

        try {
            result = Database.execute(query, new Object[] {id});
        } catch (SQLException | ClassNotFoundException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try { Database.closeConnection(); } catch (SQLException e) {}
        }

        if (result == 0)
            return Response.status(500)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(new JSONObject("{\"message2\":\"Database error\"}").toString()).build();

        return Response.status(200)
                .type(MediaType.APPLICATION_JSON)
                .entity(new JSONObject("{\"result\":\"success\"}").toString())
                .build();
    }
}
