package org.openmrs.module.rheapocadapter;

import static org.junit.Assert.*;


import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Encounter;
import org.openmrs.api.context.Context;

import org.openmrs.module.rheapocadapter.impl.HL7MessageTransformer;
import org.openmrs.module.rheapocadapter.service.MessageTransformer;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.Verifies;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;


public class HL7MessageTransformerTest extends BaseModuleContextSensitiveTest {
    @Before
    public void runBeforeEachTest() throws Exception {
        new Configuration().setProperty(Environment.HBM2DDL_AUTO, "create-drop")   ;
        executeDataSet("src/test/resources/initialData.xml");
    }
    @Test
    @Verifies(value = "should create ORUR01 message for encounter(", method = "encodingEncounterToMessage(...)")
    public void createEncounters_shouldCreateORUR01MessageForEncounter()
            throws Exception {
        MessageTransformer messageTransformer = new HL7MessageTransformer();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "");
        HttpServletResponse response = new MockHttpServletResponse();
        Encounter encounter= Context.getEncounterService().getEncounter(1);
        //Need to fix null encounter provider for this test
//        assertNotNull(encounter.getProvider());
        String message = (String) messageTransformer
                .encodingEncounterToMessage(encounter);

        assertNotNull(message);
        assertTrue(!message.equalsIgnoreCase(""));

    }


}
