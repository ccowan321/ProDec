package syllander.domain;

import org.springframework.stereotype.Service;
import syllander.data.EventRepository;
import syllander.models.Event;

import java.text.ParseException;
import java.util.List;

@Service
public class EventService {
    private EventRepository repository;

    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public List<Event> generateEventList(String input) throws ParseException{ // dont really think there is much checking to do for this script
        return repository.generateEventList(input);
    }
}
