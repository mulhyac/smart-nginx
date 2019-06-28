package com.zejor.devops.nginx.controller;

import com.zejor.devops.nginx.domain.Linux;
import com.zejor.devops.nginx.service.LinuxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * linux接口管理
 */
@Controller
@RequestMapping("/admin/linux")
public class LinuxController {

    @Autowired
    private LinuxService linuxService;

    @PostMapping
    public ResponseEntity addLinux(@RequestBody Linux linux) {
        linuxService.addLinux(linux);
        return ResponseEntity.ok("");
    }

    @DeleteMapping(value = "/{linuxId}")
    public ResponseEntity deleteLinux(@PathVariable int linuxId) {
        linuxService.deleteLinux(linuxId);
        return ResponseEntity.ok("");
    }

    @PutMapping
    public ResponseEntity updateLinux(@RequestBody Linux linux) {
        linuxService.updateLinux(linux);
        return ResponseEntity.ok("");
    }

    @GetMapping(value = "/{linuxId}")
    public ResponseEntity getLinux(@PathVariable int linuxId) {
        return ResponseEntity.ok(linuxService.getLinux(linuxId));
    }

    @GetMapping(value = "/list/{pageNumber}/{pageSize}")
    public ResponseEntity listLinux(@PathVariable int pageNumber, @PathVariable int pageSize) {
        return ResponseEntity.ok(linuxService.listLinux(pageNumber, pageSize));
    }
}
