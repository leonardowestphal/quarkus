package io.quarkus.hibernate.orm.use_jdbc_metadata_defaults;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.arc.Arc;
import io.quarkus.hibernate.orm.PersistenceUnit;
import io.quarkus.hibernate.orm.multiplepersistenceunits.model.config.inventory.Plane;
import io.quarkus.hibernate.orm.multiplepersistenceunits.model.config.user.User;
import io.quarkus.test.QuarkusUnitTest;

public class UseJdbcMetadataDefaultsFalseValueWithMultiplePUsTest {

    @RegisterExtension
    static QuarkusUnitTest runner = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addClass(User.class)
                    .addClass(Plane.class)
                    .addAsResource("application-multiple-pu-use-jdbc-metadata-defaults-false-value.properties",
                            "application.properties"));

    @PersistenceUnit("users")
    @Inject
    EntityManager emUsers;

    @PersistenceUnit("inventory")
    @Inject
    EntityManager emInventory;

    @BeforeEach
    public void activateRequestContext() {
        Arc.container().requestContext().activate();
    }

    @Test
    public void testFalseValue() {
        Map<String, Object> usersProperties = emUsers.getEntityManagerFactory().getProperties();
        assertEquals("false", usersProperties.get("hibernate.temp.use_jdbc_metadata_defaults"));

        Map<String, Object> inventoryProperties = emUsers.getEntityManagerFactory().getProperties();
        assertEquals("false", inventoryProperties.get("hibernate.temp.use_jdbc_metadata_defaults"));
    }

    @AfterEach
    public void terminateRequestContext() {
        Arc.container().requestContext().terminate();
    }
}
