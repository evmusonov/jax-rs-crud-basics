package dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.DefaultValue;

public class TaskCreateDTO {
    @NotEmpty
    public String title;
    @NotEmpty
    public String description;
    public Boolean finished = false;
}
