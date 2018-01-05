package com.chart.struts.action;


import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

@Action(value = "hello",results = {
        @Result(name = "helloPage",location = "/hello-world.jsp")
})
public class IndexAction extends ActionSupport{

    @Override
    public String execute(){
        return "helloPage";
    }
}
