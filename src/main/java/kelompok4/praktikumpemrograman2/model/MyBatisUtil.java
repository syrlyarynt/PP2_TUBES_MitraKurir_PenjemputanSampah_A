package kelompok4.praktikumpemrograman2.model;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MyBatisUtil {
    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            System.out.println("Initializing MyBatisUtil...");
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            System.out.println("SqlSessionFactory created successfully");
        } catch (IOException e) {
            System.out.println("ERROR initializing MyBatisUtil: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static SqlSession getSqlSession() {
        System.out.println("Getting new SQL session...");
        return sqlSessionFactory.openSession();
    }
}