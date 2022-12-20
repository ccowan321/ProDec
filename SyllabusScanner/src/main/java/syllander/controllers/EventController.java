package syllander.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import syllander.domain.EventService;
import syllander.models.Event;

import java.text.ParseException;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api")
public class EventController {
    private EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @PostMapping
    public List<Event> generateEventList(@RequestBody String data) throws ParseException {
        System.out.println(data);
        return service.generateEventList(data);
    }

    @GetMapping
    public ResponseEntity<String> test(){
        return new ResponseEntity<String>("test", HttpStatus.OK);
    }
}
