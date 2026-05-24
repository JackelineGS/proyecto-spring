package com.springboot.proyectospring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
public class DropConstraintTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void dropConstraint() {
        try {
            jdbcTemplate.execute("ALTER TABLE citas DROP CONSTRAINT citas_id_paquete_key");
            System.out.println("Constraint dropped successfully");
        } catch (Exception e) {
            System.out.println("Could not drop constraint (maybe already dropped?): " + e.getMessage());
        }
    }
}
