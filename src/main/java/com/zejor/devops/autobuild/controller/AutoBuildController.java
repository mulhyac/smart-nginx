package com.zejor.devops.autobuild.controller;

import com.zejor.devops.autobuild.service.AutoBuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/autobuild")
public class AutoBuildController {

    @Autowired
    private AutoBuildService autoBuildService;

    @GetMapping("/{buildId}")
    public String build(@PathVariable int buildId) {
        autoBuildService.build(buildId);
        return "AutoBuildService";
    }


}
