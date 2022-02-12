package org.ev.domain.tasks;

public class Task {
    private int id;
    private String title;
    private String description;
    private boolean finished = false;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Override
    public String toString() {
        return "\"Task [Id " + id + "Title " + title + ", Description = " + description + ", Finished = " + finished + "]";
    }
}
