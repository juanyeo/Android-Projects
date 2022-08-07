package com.example.fast_alarm;

public class AlarmDisplayModel {
    int hour;
    int minute;
    Boolean onOff;

    String timeText;
    String amPmText;

    public AlarmDisplayModel(int hour, int minute, Boolean onOff) {
        this.hour = hour;
        this.minute = minute;
        this.onOff = onOff;

        updateText(hour, minute);
    }

    public void updateText(int hour, int minute) {
        this.timeText = makeTimeText(hour, minute);
        this.amPmText = makeAmPmText(hour);
    }

    private String makeTimeText(int hour, int minute) {
        int h = (hour > 12) ? hour-12 : hour;
        String fh = "%02d".format(String.valueOf(h));
        String m = "%02d".format(String.valueOf(minute));

        return fh + ":" + m;
    }

    private String makeAmPmText(int hour) {
        if (hour > 12) {
            return "PM";
        } else {
            return "AM";
        }
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setOnOff(Boolean onOff) {
        this.onOff = onOff;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public Boolean getOnOff() {
        return onOff;
    }

    public String getTimeText() {
        return timeText;
    }

    public String getAmPmText() {
        return amPmText;
    }
}
