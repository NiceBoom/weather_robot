package com.robot.controller;


import com.robot.enity.Admin;
import com.robot.enity.Result;
import com.robot.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {


    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public Result login(@RequestBody Admin admin){
        Boolean login = adminService.login(admin);
    }
}
