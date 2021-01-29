package com.code_template.controller;

import com.code_template.annotation.Audit;
import com.code_template.util.ReturnObject;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/template", produces = "application/json;charset=UTF-8")
public class TemplateController {

    @Audit
    @GetMapping("/{id}")
    public ReturnObject getTemplateInfo(@PathVariable int id){

    }

    @Audit
    @PostMapping("")
    public ReturnObject postTemplate(){

    }

    Audit
    @Update()
}
