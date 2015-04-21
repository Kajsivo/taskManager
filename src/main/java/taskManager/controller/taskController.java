package taskManager.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class taskController {

    @RequestMapping(value="/create", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> create(@RequestBody String requestEntity) {

        return new ResponseEntity<String>(requestEntity, HttpStatus.OK);
    }
}
