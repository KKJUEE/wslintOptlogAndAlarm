package cn.succy.alarm.provider;

import java.text.ParseException;

import cn.succy.alarm.resources.AlarmResource;

/**
 * 定义从sql数据库中取得告警数据
 * @author ranzhonggeng
 *
 */
public interface ResourceProvider {
    AlarmResource getReceiver() throws ParseException;
}
