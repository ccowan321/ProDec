package syllander.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import syllander.controllers.EventController;
import syllander.data.EventRepository;
import syllander.domain.EventService;

import java.text.ParseException;

@SpringBootApplication
@ComponentScan(basePackageClasses = {EventController.class, EventService.class, EventRepository.class})
public class App {
    public static void main(String[] args){
        SpringApplication.run(App.class,args);
//        EventRepository test = new EventRepository();
//        List<Event> result = test.generateEventList("\n test 2/1/20 \n \n project 10/20/15 \n \n");
//        for (Event e: result){
//            System.out.println(e.getEventName());
//            System.out.println(e.getLocalDate());
//        }
    }
}
