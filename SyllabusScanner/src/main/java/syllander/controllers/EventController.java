package syllander.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import syllander.domain.EventService;
import syllander.models.Event;

import java.text.ParseException;
import java.util.List;

@Controller
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/")
public class EventController {
    private EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @PostMapping("/generateEvents")
    public List<Event> generateEventList(@RequestBody String data) throws ParseException {
        return service.generateEventList(data);
    }
}
