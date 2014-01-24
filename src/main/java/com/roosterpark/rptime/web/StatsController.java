package com.roosterpark.rptime.web;

import com.roosterpark.rptime.aspect.ControllerCounter;
import com.roosterpark.rptime.model.Worker;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StatsController {
    
    @Inject
    ControllerCounter counter;
    
	@RequestMapping(value = "/stats/service", method = GET)
	@ResponseBody
	public Map<String, AtomicInteger> getServiceCount() {
        return counter.getServiceCalls();
	}
}
