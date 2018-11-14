package cn.succy.alarm.receiver;

import java.util.List;
 
/**
 * 分页实体类，以便后期数据做分页
 * @author ranzhonggeng
 *
 */
public class AlarmTotal {
    private int total; 
    private List<Contact> rows; 
    
    
    public AlarmTotal() {
    }
    public AlarmTotal(int total, List<Contact> rows) {
        this.total = total;
        this.rows = rows;
    }
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    public List<Contact> getRows() {
        return rows;
    }
    public void setRows(List<Contact> rows) {
        this.rows = rows;
    }
    
}
