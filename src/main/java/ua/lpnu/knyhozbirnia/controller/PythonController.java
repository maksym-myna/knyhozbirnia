package ua.lpnu.knyhozbirnia.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import ua.lpnu.knyhozbirnia.service.PythonService;

@RestController
@RequestMapping("/python/")
@AllArgsConstructor
public class PythonController {
    private final PythonService pythonService;

    @GetMapping("/populate/")
    public SseEmitter populate() throws InterruptedException {
       return pythonService.populate();
    }

    @GetMapping("/etl/")
    public SseEmitter performETL() throws InterruptedException {
        return pythonService.performETL();
    }
}