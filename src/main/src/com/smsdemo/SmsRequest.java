package com.smsdemo;


import java.util.ArrayList;
import java.util.List;

public class SmsRequest {
    //手机号列表
    private List<String> phones = new ArrayList<>();
    //短信模版
    private SmsTemplate template;
    //模版参数
    private List<String> parameters = new ArrayList<>();


    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public SmsTemplate getTemplate() {
        return template;
    }

    public void setTemplate(SmsTemplate template) {
        this.template = template;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }
}
