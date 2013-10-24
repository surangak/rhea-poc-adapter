/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.rheapocadapter.advice;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.rheapocadapter.handler.ClientRegistryService;

/**
 *
 */

public class PatientServiceAdvice implements MethodInterceptor {

	protected final Log log = LogFactory.getLog(PatientServiceAdvice.class);

	public Object invoke(MethodInvocation invocation) throws Throwable {
		ClientRegistryService clientService = new ClientRegistryService();
		boolean update = false;
		Patient patient = null;
		PatientService ps = Context.getPatientService();
		if (invocation.getMethod().getName().equals("savePatient")) {
			patient = (Patient) invocation.getArguments()[0];
			ps = Context.getPatientService();
			if (ps.getPatientByExample(patient) != null) {
				update = true;

			}
		}
		Object o = invocation.proceed();

		if (invocation.getMethod().getName().equals("savePatient")) {

			if (!update) {
				log.info("Save Patient to Client Registry");
				clientService.registerNewPatient(patient);
			} else {
				log.info("Update Patient to Client Registry");
				clientService.updateClientDetails(patient);

			}

		}
		return o;
	}

}
