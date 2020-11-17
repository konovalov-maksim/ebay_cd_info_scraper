package konovalov.ebayscraper.core.entities;

public enum Status {
    NEW("New"),
    LOADING("Items searching"),
    ERROR("Error"),
    COMPLETED("Completed");

    String statusName;

    Status(String statusName) {
        this.statusName = statusName;
    }

    public String getName() {
        return statusName;
    }
}
