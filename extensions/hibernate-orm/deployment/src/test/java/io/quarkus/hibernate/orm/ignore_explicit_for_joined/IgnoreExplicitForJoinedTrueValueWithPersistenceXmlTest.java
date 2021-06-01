package io.quarkus.hibernate.orm.ignore_explicit_for_joined;

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

public class IgnoreExplicitForJoinedTrueValueWithPersistenceXmlTest {

    @RegisterExtension
    static QuarkusUnitTest runner = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class)
                    .addClass(MyEntity.class)
                    .addAsManifestResource("META-INF/persistence-discriminator-ignore-explicit-for-joined-true-value.xml",
                            "persistence.xml")
                    .addAsResource("application-datasource-only.properties", "application.properties"));

    @Inject
    EntityManager em;

    @BeforeEach
    public void activateRequestContext() {
        Arc.container().requestContext().activate();
    }

    @Test
    public void testTrueValue() {
        Map<String, Object> properties = em.getEntityManagerFactory().getProperties();

        // the PU is templatePU from the persistence.xml, not the default entity manager from application.properties
        assertEquals("templatePU", properties.get("hibernate.ejb.persistenceUnitName"));
        assertEquals("true", properties.get("hibernate.discriminator.ignore_explicit_for_joined"));
    }

    @AfterEach
    public void terminateRequestContext() {
        Arc.container().requestContext().terminate();
    }

}
