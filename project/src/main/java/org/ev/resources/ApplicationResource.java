package org.ev.resources;

import org.ev.domain.tasks.Task;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Path("/rest")
public class ApplicationResource {
    @GET
    @Path("/sayhi")
    public Response sayHi() {
        HttpURLConnection conn;
        try {
            URL url = new URL("http://localhost:8080/api/tasks/list");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String str = "";
            while (bufferedReader.ready()) {
                str += bufferedReader.readLine();
            }

            JSONArray jsonArray = new JSONArray(str);

            return Response.status(Response.Status.OK).entity(jsonArray.getJSONObject(0).getString("title")).build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Response.ok().build();
    }
}
