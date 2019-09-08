package com.apress.spring.springbootjournal.domain;

import javax.management.ValueExp;
import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// DB에 저장 가능한 JPA 엔티티
@Entity
public class Journal {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    private String title;
    private Date created;
    private String summary;

    @Transient // 값을 저장하지 않고 무시
    private SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

    public Journal(String title, String summary, String date) throws ParseException {
        this.title = title;
        this.summary = summary;
        this.created = format.parse(date);
    }

    Journal(){}

    public long getId(){
        return id;
    }
    public void setId(long id){
        this.id = id;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public Date getCreated(){
        return created;
    }
    public void setCreated(Date created){
        this.created = created;
    }
    public String getSummary(){
        return summary;
    }
    public void setSummary(String summary){
        this.summary = summary;
    }
    public String getCreatedAsShort(){
        return format.format(created);
    }
    // 레코드를 보기 편하게 출력하기 위한 toString 재정의
    public String toString() {
        StringBuilder value = new StringBuilder("JournalEntity(");
        value.append("id: ");
        value.append(id);
        value.append(",제목: ");
        value.append(title);
        value.append(",요약: ");
        value.append(summary);
        value.append(",일자: ");
        value.append(getCreatedAsShort());
        value.append(")");
        return value.toString();
    }
}
