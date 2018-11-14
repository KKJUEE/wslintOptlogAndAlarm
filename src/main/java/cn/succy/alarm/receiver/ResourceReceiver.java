package cn.succy.alarm.receiver;

import java.util.HashSet;
import java.util.Set;

import cn.succy.alarm.resources.AlarmResource;

/**
 * 数据库告警获取接口
 * @author ranzhonggeng
 *
 */
public class ResourceReceiver {
    private Set<AlarmResource> alarmResourceSet = new HashSet<>();
    
    public Set<String> getAlarmId() {
        Set<String> alarmId = new HashSet<>();
        for (AlarmResource alarm : alarmResourceSet) {
        	alarmId.add(alarm.getId());
        }

        return alarmId;
    }

    public Set<String> getAlarmName() {
        Set<String> alarmName = new HashSet<>();
        for (AlarmResource alarm : alarmResourceSet) {
        	alarmName.add(alarm.getAlarmName());
        }

        return alarmName;
    }

    public Set<String> getAlarmAppName() {
        Set<String> alarmAppName = new HashSet<>();
        for (AlarmResource alarm : alarmResourceSet) {
        	alarmAppName.add(alarm.getAppName());
        }

        return alarmAppName;
    }


    @Override
    public String toString() {
    	return "AlarmReceiver{" +
                "alarmResourceSet=" + alarmResourceSet +
                '}';
    }
}
