package JavaBasement;

import java.io.*;

/**
 * Created by F on 2018/4/12.
 */
public class SerializableTest {
    static class Person implements Serializable{
        private String name;
        private String sex;
        public Person(String name,String sex){
            this.name = name;
            this.sex = sex;
        }
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }



        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }
    public static void main(String[] args)throws IOException,ClassNotFoundException{


        //从文件中读取该对象返回
        ObjectInputStream ois1 = new ObjectInputStream(new FileInputStream("hello.txt"));
        Person p1 = (Person) ois1.readObject();
        System.out.println("fan:" + p1.hashCode());

        ObjectInputStream ois2 = new ObjectInputStream(new FileInputStream("hello.txt"));
        Person p2 = (Person)ois2.readObject();
        System.out.println("fan1:" + p2.hashCode());

    }
}
