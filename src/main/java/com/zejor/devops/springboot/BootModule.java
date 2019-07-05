package com.zejor.devops.springboot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BootModule {

    private String bootTarget;

    private String bootPom;

    private String bootName;

}
