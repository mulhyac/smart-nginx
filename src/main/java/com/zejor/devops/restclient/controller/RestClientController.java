package com.zejor.devops.restclient.controller;

import com.zejor.devops.restclient.domain.Api;
import com.zejor.devops.restclient.domain.ApiEnv;
import com.zejor.devops.restclient.domain.ApiRequest;
import org.nutz.dao.Dao;
import org.nutz.http.Header;
import org.nutz.http.Http;
import org.nutz.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/restcli")
public class RestClientController {

    @Autowired
    private Dao dao;

    @GetMapping("/{envId}/{apiId}/{requestId}")
    public ResponseEntity request(@PathVariable int envId, @PathVariable int apiId, @PathVariable int requestId) {
        ApiRequest request = dao.fetch(ApiRequest.class, requestId);
        ApiEnv env = dao.fetch(ApiEnv.class, envId);
        Api api = dao.fetch(Api.class, apiId);
        String url = env.getEnvCtx() + api.getApiUri();
        Response response = null;
        switch (request.getRequestMethod()) {
            case 1:
                //get
                response = Http.get(url);
                break;
            case 2:
                //post
                Header header = Header.create();
                header.addv("Content-Type", request.getRequestContentType());
                response = Http.post3(url, request.getRequestBody(), header, Integer.MAX_VALUE);
                break;
            case 3:
                break;
            case 4:
                break;
        }
        String content=response.getContent("UTF-8");
        return ResponseEntity.ok(content);
    }

}
