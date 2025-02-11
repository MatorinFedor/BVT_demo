package ru.mtuci.bvt_demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MyRestController {

    private final MyService myService;

    public MyRestController(MyService myService) {
        this.myService = myService;
    }

    @GetMapping("/hello")
    public String getMessage() {
        return myService.getMessage();
    }

    @GetMapping("/echo")
    public String echo(@RequestParam String str) {
        return str;
    }

}
