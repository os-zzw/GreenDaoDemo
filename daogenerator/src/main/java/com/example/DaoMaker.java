package com.example;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class DaoMaker {
    public static void main(String[] args) {
        //生成数据库的实体类 xxEntity 对应的是 数据库的表
        Schema schema = new Schema(1, "com.student.entity");
        addStudent(schema);
        schema.setDefaultJavaPackageDao("com.student.dao");
        try {
            new DaoGenerator().generateAll(schema, "E:\\AndroidWorkSpace\\GreenDaoDemo\\app\\src\\main\\java-gen");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //创建数据库的表
    private static void addStudent(Schema schema) {
        Entity entity = schema.addEntity("Student");//创建数据库的表
        entity.addIdProperty();//主键 是int类型
        entity.addStringProperty("name");
        entity.addStringProperty("address");
        entity.addIntProperty("age");
    }
}
