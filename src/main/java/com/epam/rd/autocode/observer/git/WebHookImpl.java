package com.epam.rd.autocode.observer.git;

import com.epam.rd.autocode.observer.git.Event.Type;
import java.util.ArrayList;
import java.util.List;

public class WebHookImpl implements WebHook {

	private final String branch;
	private final Type type;
	private final List<Event> events;

	public WebHookImpl(String branch, Type type) {
		this.branch = branch;
		this.type = type;
		this.events = new ArrayList<>();
	}

	@Override
	public String branch() {
		return branch;
	}

	@Override
	public Type type() {
		return type;
	}

	@Override
	public List<Event> caughtEvents() {
		return events;
	}

	@Override
	public void onEvent(Event event) {
		if (this.branch.equals(event.branch()) && this.type == event.type()) {
			events.add(event);
		}
	}
}
