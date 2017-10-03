package de.syslord.slidegen.editor.ui.base;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

@UIScope
@SpringComponent
public class EventBus {

	private static final Logger logger = LoggerFactory.getLogger(EventBus.class);

	/*
	 * Per event(-class) we can have multiple listeners who will be invoked when the event is fired. But the
	 * mere existance of an instance of a listener does not mean that it must be invoked; because only views
	 * that are attached (clientside visible) must react to an event. So a 'supervising' view must be provided
	 * for each listener, and only when this view is attached, the event will be fired.
	 */
	// TODO could be nicer: Map<Consumer<?>, BaseView<?>> -> EventListener<EVENT>
	private Map<Class<?>, Map<Consumer<?>, BaseView<?>>> eventListeners = Maps.newHashMap();

	private Set<BaseView<?>> attached = Sets.newHashSet();

	public void attach(BaseView<?> view) {
		attached.add(view);
	}

	public void detach(BaseView<?> view) {
		attached.remove(view);
	}

	private boolean isViewAttached(BaseView<?> view) {
		return attached.contains(view);
	}

	public <EVENT extends EventBusEvent> void register(BaseView<?> view, Class<EVENT> clazz, Consumer<EVENT> listener) {
		if (!eventListeners.containsKey(clazz)) {
			eventListeners.put(clazz, Maps.newHashMap());
		}

		eventListeners.get(clazz).put(listener, view);
	}

	public <EVENT extends EventBusEvent> void unregister(BaseView<?> view, Class<EVENT> clazz, Consumer<EVENT> listener) {
		if (!eventListeners.containsKey(clazz)) {
			logger.warn("No event of type {} present!", clazz);
			return;
		}
		eventListeners.get(clazz).remove(listener);
	}

	@SuppressWarnings("unchecked")
	public <EVENT extends EventBusEvent> void fire(EVENT event) {
		Map<Consumer<?>, BaseView<?>> map = eventListeners.get(event.getClass());

		map.entrySet().stream()
			.filter(entry -> isViewAttached(entry.getValue()))
			// we are sure we only put Consumer<EVENT> into Class<EVENT> keys.
			.map(entry -> (Consumer<EVENT>) entry.getKey())
			.forEach(consumer -> consumer.accept(event));
	}

}
