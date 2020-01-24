/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.util.Objects;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
/**
 *
 * @author Mustafa Khaled
 */
public class Student extends RecursiveTreeObject<Student>{
    public IntegerProperty id;
    public StringProperty name , phone ,track;

    public Student() {
    }

    public Student(int id, String name, String phone, String track) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.track = new SimpleStringProperty(track);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id = new SimpleIntegerProperty(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone = new SimpleStringProperty(phone);
    }

    public String getTrack() {
        return track.get();
    }

    public void setTrack(String track) {
        this.track = new SimpleStringProperty(track);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id.get());
        hash = 67 * hash + Objects.hashCode(this.name.get());
        hash = 67 * hash + Objects.hashCode(this.phone.get());
        hash = 67 * hash + Objects.hashCode(this.track.get());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Student other = (Student) obj;
        if (this.id.get()!=other.id.get()) {
            return false;
        }
        if (!Objects.equals(this.name.get(), other.name.get())) {
            return false;
        }
        if (!Objects.equals(this.phone.get(), other.phone.get())) {
            return false;
        }
        if (!Objects.equals(this.track.get(), other.track.get())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MUFIX{" + id.get() + "-" + name.get() + "-" + phone.get() + "-" + track.get() + '}';
    }
    public boolean ToObj(String s){
        s=s.substring(6, s.length()-1);
        String a[]=s.split("-");
        if(a.length==4){
            setId(Integer.parseInt(a[0]));
            setName(a[1]);
            setPhone(a[2]);
            setTrack(a[3]);
            return true;
        }
            return false;
    }
    public static void main(String[] args) {
        new Student().ToObj("MUFIX{1-Mustafa Khaled-0111111-Java}");
    }
}
