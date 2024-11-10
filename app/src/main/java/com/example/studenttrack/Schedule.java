package com.example.studenttrack;

public class Schedule {
    private String id;
    private String className;
    private String classTime;

    public Schedule() {

    }

    public Schedule(String id, String className, String classTime) {
        this.id = id;
        this.className = className;
        this.classTime = classTime;
    }

    public String getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public String getClassTime() {
        return classTime;
    }
}
