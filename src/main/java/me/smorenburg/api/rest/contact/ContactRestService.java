package me.smorenburg.api.rest.contact;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${jwt.routes.apiendpoint}")
public class ContactRestService {

    @RequestMapping(path = "/contact", method = RequestMethod.POST)
    public static ResponseEntity<Object> contact() {
        return ResponseEntity.ok().build();
    }
}
