package com.learn.realworldscenarios.corelevel.handleNullable;


import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

/**
 * @author prabhakar, @Date 11-08-2025
 */

public class Users {

    private String name;

    public Users(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    // Simple null-check (clear & safe)
//    public String getNameOrDefault(Users user) {
//        if (user == null || user.getName() == null) {
//            return "No user found Default Check";
//        }
//        return user.getName();
//    }

    // Objects.toString (very concise — returns default only for null)
//    public String getNameOrDefault(Users user) {
//        return Objects.toString(user == null ? null : user.getName(), "No user found Objects");
//    }

    // Using Optional (functional style; also handle blank names)
    // ofNullable(); takes value that might be null
    // orElse(); returns the default value if the original value is null

//    public String getNameOrDefault(Users user) {
//        return Optional.ofNullable(user)
//                .map(Users::getName)
//                .filter(name -> name != null && !name.isBlank()) // isBlank() needs Java 11+; use trim() for Java 8
//                .orElse("No user found Optional");
//    }


    /**
     * we have a user object, and we need to fetch name from it. but sometimes
     * that object might have null in the name. In such cases, instead of returning
     * null, we should return a default message like "No user found".
     */

    public static void main(String[] args) {

        Users user = new Users("user");
        Users user2 = new Users(null);

        String name1 = Optional.ofNullable(user.getName()).orElse("No user found");
        String name2 = Optional.ofNullable(user2.getName()).orElse("No user found Direct Optional");
        //String name2 = user2.getNameOrDefault(user2);

        System.out.println("User1 name: " + name1);
        System.out.println("User2 name: " + name2);
    }
}
