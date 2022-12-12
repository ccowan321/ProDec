import syllander.data.EventRepository;
import syllander.models.Event;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) throws ParseException {
        EventRepository test = new EventRepository();
        List<Event> result = test.generateEventList("\n test 2/1/20 \n \n project 10/20/15 \n \n");
        for (Event e: result){
            System.out.println(e.getEventName());
            System.out.println(e.getLocalDate());
        }
    }
}
