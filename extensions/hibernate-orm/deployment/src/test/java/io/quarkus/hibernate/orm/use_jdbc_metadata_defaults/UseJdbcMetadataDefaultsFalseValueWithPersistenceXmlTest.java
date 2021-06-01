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
import io.quarkus.hibernate.orm.xml.persistence.MyEntity;
import io.quarkus.test.QuarkusUnitTest;

public class UseJdbcMetadataDefaultsFalseValueWithPersistenceXmlTest {

    @RegisterExtension
    static QuarkusUnitTest runner = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addClass(MyEntity.class)
                    .addAsManifestResource("META-INF/persistence-use-jdbc-metadata-defaults-false-value.xml", "persistence.xml")
                    .addAsResource("application-datasource-only.properties", "application.properties"));

    @Inject
    EntityManager em;

    @BeforeEach
    public void activateRequestContext() {
        Arc.container().requestContext().activate();
    }

    @Test
    public void testFalseValue() {
        Map<String, Object> properties = em.getEntityManagerFactory().getProperties();

        // the PU is templatePU from the persistence.xml, not the default entity manager from application.properties
        assertEquals("templatePU", properties.get("hibernate.ejb.persistenceUnitName"));
        assertEquals("false", properties.get("hibernate.temp.use_jdbc_metadata_defaults"));
    }

    @AfterEach
    public void terminateRequestContext() {
        Arc.container().requestContext().terminate();
    }

}
