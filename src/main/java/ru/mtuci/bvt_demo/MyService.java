package ru.mtuci.bvt_demo;

import org.springframework.stereotype.Service;

@Service
public class MyService {
    public String getMessage() {
        return "Hello from MyService!";
    }
}
