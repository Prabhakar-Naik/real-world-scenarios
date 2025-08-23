package com.learn.realworldscenarios.advancelevel.pagination.controller;

import com.learn.realworldscenarios.advancelevel.pagination.dto.UserDto;
import com.learn.realworldscenarios.advancelevel.pagination.service.UserService;
import com.learn.realworldscenarios.advancelevel.utils.PaginatedResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author prabhakar, @Date 18-08-2025
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService svc;

    public UserController(UserService svc) { this.svc = svc; }

    @GetMapping(value = "/getRecords")
    public ResponseEntity<PaginatedResponse<UserDto>> list(
            @RequestParam(required = false) String cursor,
            @RequestParam(required = false, defaultValue = "50") int limit
    ) {
        PaginatedResponse<UserDto> page = svc.getUsers(cursor, limit);

        var headers = new HttpHeaders();
        page.getNextCursor().ifPresent(next -> {
            String nextUrl = ServletUriComponentsBuilder.fromCurrentRequest()
                    .replaceQueryParam("cursor", URLEncoder.encode(next, StandardCharsets.UTF_8))
                    .replaceQueryParam("limit", limit)
                    .toUriString();
            headers.add(HttpHeaders.LINK, "<" + nextUrl + ">; rel=\"next\"");
        });

        return ResponseEntity.ok().headers(headers).body(page);
    }

    // convenience endpoint to generate test users
    @PostMapping(value = "/generate")
    public ResponseEntity<String> generate(@RequestParam(defaultValue = "1000") int count) {
        int created = svc.generateTestUsers(count);
        return ResponseEntity.ok("created=" + created);
    }


}
