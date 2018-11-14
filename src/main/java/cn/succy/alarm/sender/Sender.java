package cn.succy.alarm.sender;

import java.text.ParseException;

import cn.succy.alarm.template.TemplateModel;

public interface Sender {
    void send(TemplateModel model) throws ParseException;
}
