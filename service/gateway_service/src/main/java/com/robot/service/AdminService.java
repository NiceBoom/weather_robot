package com.robot.service;

import com.robot.enity.Admin;

public interface AdminService {
    //验证用户账号密码，并
    Boolean login(Admin admin);
}
