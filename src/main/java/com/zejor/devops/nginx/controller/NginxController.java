package com.zejor.devops.nginx.controller;

import com.zejor.devops.nginx.bean.NginxServer;
import com.zejor.devops.nginx.bean.NginxLocation;
import com.zejor.devops.nginx.bean.NginxUpstream;
import com.zejor.devops.nginx.domain.Nginx;
import com.zejor.devops.nginx.service.NginxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * nginx接口管理
 */
@Controller
@RequestMapping("/admin/nginx")
public class NginxController {

    @Autowired
    private NginxService nginxService;

    @GetMapping(value = "/{nginxId}/server")
    public ResponseEntity listServer(@PathVariable int nginxId) {
        return ResponseEntity.ok(nginxService.listNginxServer(nginxId));
    }

    @PostMapping(value = "/{nginxId}/server")
    public ResponseEntity addServer(@PathVariable int nginxId, @RequestBody NginxServer nginxServer) {
        nginxService.addServerListener(nginxId, nginxServer);
        return ResponseEntity.ok(nginxService.listNginxServer(nginxId));
    }

    @DeleteMapping(value = "/{nginxId}/server/{name}/{port}")
    public ResponseEntity deleteServer(@PathVariable int nginxId, NginxServer nginxServer) {
        boolean delete = nginxService.deleteServerListener(nginxId, nginxServer);
        return ResponseEntity.ok(delete);
    }

    @GetMapping(value = "/{nginxId}/server/{name}/{port}")
    public ResponseEntity listLocation(@PathVariable int nginxId, NginxServer nginxServer) {
        return ResponseEntity.ok(nginxService.listLocation(nginxId, nginxServer));
    }

    @PostMapping(value = "/{nginxId}/server/{name}/{port}")
    public ResponseEntity addLocation(@PathVariable int nginxId, NginxServer nginxServer, @RequestBody NginxLocation nginxLocation) {
        return ResponseEntity.ok(nginxService.addLocation(nginxId, nginxServer, nginxLocation));
    }

    @DeleteMapping(value = "/{nginxId}/server/{name}/{port}/{path}")
    public ResponseEntity deleteLocation(@PathVariable int nginxId, NginxServer nginxServer, NginxLocation location) {
        location.setPath(location.getPath().replace("xxoo", "/"));
        return ResponseEntity.ok(nginxService.deleteLocation(nginxId, nginxServer, location));
    }

    @GetMapping(value = "/{nginxId}/upstream")
    public ResponseEntity listUpstream(@PathVariable int nginxId) {
        return ResponseEntity.ok(nginxService.listUpstream(nginxId));
    }

    @PostMapping(value = "/{nginxId}/upstream")
    public ResponseEntity addUpstream(@PathVariable int nginxId, @RequestBody NginxUpstream nginxUpstream) {
        nginxService.addUpstream(nginxId, nginxUpstream);
        return ResponseEntity.ok("");
    }

    @DeleteMapping(value = "/{nginxId}/upstream/{upstream}")
    public ResponseEntity deleteUpstream(@PathVariable int nginxId, @PathVariable String upstream) {
        nginxService.deleteUpstream(nginxId, upstream);
        return ResponseEntity.ok("");
    }

    @GetMapping(value = "/{nginxId}/upstream/{upstream}")
    public ResponseEntity getUpstream(@PathVariable int nginxId, @PathVariable String upstream) {
        return ResponseEntity.ok(nginxService.getUpstream(nginxId, upstream));
    }

//    @GetMapping(value = "/{nginxId}/start")
//    public ResponseEntity startNginx(@PathVariable int nginxId) {
//        return ResponseEntity.ok(nginxService.startNginx(nginxId));
//    }
//
//    @GetMapping(value = "/{nginxId}/stop")
//    public ResponseEntity stopNginx(@PathVariable int nginxId) {
//        return ResponseEntity.ok(nginxService.startNginx(nginxId));
//    }
//
//    @GetMapping(value = "/{nginxId}/reload")
//    public ResponseEntity reloadNginx(@PathVariable int nginxId) {
//        return ResponseEntity.ok(nginxService.startNginx(nginxId));
//    }
//
//    @GetMapping(value = "/{nginxId}/install")
//    public ResponseEntity installNginx(@PathVariable int nginxId) {
//        return ResponseEntity.ok(nginxService.startNginx(nginxId));
//    }
//
//    @GetMapping(value = "/{nginxId}/uninstall")
//    public ResponseEntity uninstallNginx(@PathVariable int nginxId) {
//        return ResponseEntity.ok(nginxService.startNginx(nginxId));
//    }
//
//    @GetMapping(value = "/{nginxId}/check")
//    public ResponseEntity checkNginx(@PathVariable int nginxId) {
//        return ResponseEntity.ok(nginxService.startNginx(nginxId));
//    }

    @PostMapping//(value = "/nginx")
    public ResponseEntity addNginx(@RequestBody Nginx nginx) {
        nginxService.addNginx(nginx);
        return ResponseEntity.ok("");
    }

    @DeleteMapping(value = "/{nginxId}")
    public ResponseEntity deleteNginx(@PathVariable int nginxId) {
        nginxService.deleteNginx(nginxId);
        return ResponseEntity.ok("");
    }

    @PutMapping//(value = "/nginx")
    public ResponseEntity updateNginx(@RequestBody Nginx nginx) {
        nginxService.updateNginx(nginx);
        return ResponseEntity.ok("");
    }

    @GetMapping(value = "/{nginxId}")
    public ResponseEntity getNginx(@PathVariable int nginxId) {
        return ResponseEntity.ok(nginxService.getNginx(nginxId));
    }

    @GetMapping(value = "/list/{pageNumber}/{pageSize}")
    public ResponseEntity listNginx(@PathVariable int pageNumber, @PathVariable int pageSize) {
        return ResponseEntity.ok(nginxService.listNginx(pageNumber, pageSize));
    }

}
