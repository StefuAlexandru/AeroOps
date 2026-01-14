package flights_management.aeroops.enums;

public enum Status {
    PLANNED,ON_TIME,DELAYED,CANCELLED,DEPARTED;

    public boolean isDeparted(){
        return this == DEPARTED;
    }

    public boolean isCancelled(){
        return this == CANCELLED;
    }

    public boolean isPlanned(){
        return this == PLANNED;
    }

    public boolean isOnTime(){
        return this == ON_TIME;
    }

    public boolean isDelayed(){
        return this == DELAYED;
    }
}
