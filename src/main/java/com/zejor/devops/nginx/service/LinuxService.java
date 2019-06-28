package com.zejor.devops.nginx.service;

import com.zejor.devops.nginx.domain.Linux;

import java.util.List;

public interface LinuxService {

    void addLinux(Linux linux);

    void deleteLinux(int linuxId);

    void updateLinux(Linux linux);

    Linux getLinux(int linuxId);

    List<Linux> listLinux(int pageNumber, int pageSize);

}
