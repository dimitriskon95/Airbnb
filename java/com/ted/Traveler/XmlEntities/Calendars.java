package com.ted.Traveler.XmlEntities;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ted.Traveler.Database.Calendar;

@XmlRootElement(name = "calendars")
@XmlAccessorType(XmlAccessType.FIELD)
public class Calendars {

    @XmlElement(name = "calendar")
    private List<Calendar> calendars = null;

	public List<Calendar> getCalendars() {
		return calendars;
	}

	public void setCalendars(List<Calendar> calendars) {
		this.calendars = calendars;
	}
    
}